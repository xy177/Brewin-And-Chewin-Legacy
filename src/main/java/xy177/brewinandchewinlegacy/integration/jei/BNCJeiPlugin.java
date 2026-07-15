package xy177.brewinandchewinlegacy.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeRegistryPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xy177.brewinandchewinlegacy.client.gui.BNCKegGui;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRecipe;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRegistry;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

@JEIPlugin
public class BNCJeiPlugin implements IModPlugin {
    private static final List<BNCKegFermentingJeiRecipe> ACTIVE_FERMENTING_RECIPES = new ArrayList<>();
    private static IJeiRuntime jeiRuntime;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
            new BNCKegFermentingRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
            new BNCCheeseAgingRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {
        BNCKegFermentingRegistry.registerDefaults();
        ACTIVE_FERMENTING_RECIPES.clear();
        ACTIVE_FERMENTING_RECIPES.addAll(createFermentingRecipes());
        registry.addRecipes(new ArrayList<>(ACTIVE_FERMENTING_RECIPES), BNCJeiRecipeTypes.FERMENTING);
        registry.addRecipes(createCheeseAgingRecipes(), BNCJeiRecipeTypes.CHEESE_AGING);
        registry.addRecipeCatalyst(new ItemStack(BNCBlocks.KEG_ITEM), BNCJeiRecipeTypes.FERMENTING);
        registry.addRecipeClickArea(BNCKegGui.class, 80, 25, 23, 18, BNCJeiRecipeTypes.FERMENTING);
        addIngredientInfo(registry);
        registry.addRecipeRegistryPlugin(new PriorityPlugin());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
        BNCKegFermentingRegistry.addChangeListener(BNCJeiPlugin::refreshFermentingRecipes);
        refreshFermentingRecipes();
    }

    private static List<BNCKegFermentingJeiRecipe> createFermentingRecipes() {
        List<BNCKegFermentingJeiRecipe> recipes = new ArrayList<>();
        for (BNCKegFermentingRecipe recipe : BNCKegFermentingRegistry.getRecipes()) {
            BNCKegFermentingJeiRecipe jeiRecipe = new BNCKegFermentingJeiRecipe(recipe);
            if (!jeiRecipe.getOutput().isEmpty() || jeiRecipe.getOutputFluidStack() != null) {
                recipes.add(jeiRecipe);
            }
        }
        return recipes;
    }

    private static void refreshFermentingRecipes() {
        if (jeiRuntime == null) {
            return;
        }
        IRecipeRegistry recipeRegistry = jeiRuntime.getRecipeRegistry();
        for (BNCKegFermentingJeiRecipe recipe : ACTIVE_FERMENTING_RECIPES) {
            recipeRegistry.removeRecipe(recipe, BNCJeiRecipeTypes.FERMENTING);
        }
        ACTIVE_FERMENTING_RECIPES.clear();
        ACTIVE_FERMENTING_RECIPES.addAll(createFermentingRecipes());
        for (BNCKegFermentingJeiRecipe recipe : ACTIVE_FERMENTING_RECIPES) {
            recipeRegistry.addRecipe(recipe, BNCJeiRecipeTypes.FERMENTING);
        }
    }

    private static List<BNCCheeseAgingJeiRecipe> createCheeseAgingRecipes() {
        List<BNCCheeseAgingJeiRecipe> recipes = new ArrayList<>();
        if (BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM != null && BNCBlocks.FLAXEN_CHEESE_WHEEL_ITEM != null) {
            recipes.add(new BNCCheeseAgingJeiRecipe(
                new ItemStack(BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM),
                new ItemStack(BNCBlocks.FLAXEN_CHEESE_WHEEL_ITEM)));
        }
        if (BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM != null && BNCBlocks.SCARLET_CHEESE_WHEEL_ITEM != null) {
            recipes.add(new BNCCheeseAgingJeiRecipe(
                new ItemStack(BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM),
                new ItemStack(BNCBlocks.SCARLET_CHEESE_WHEEL_ITEM)));
        }
        return recipes;
    }

    private static void addIngredientInfo(IModRegistry registry) {
        addInfo(registry, BNCItems.SWEET_BERRIES, "brewinandchewinlegacy.jei.info.sweet_berries");
        addInfo(registry, BNCItems.KELP, "brewinandchewinlegacy.jei.info.algae");
        addInfo(registry, BNCItems.SEA_PICKLE, "brewinandchewinlegacy.jei.info.algae");
        addInfo(registry, BNCItems.SEAGRASS, "brewinandchewinlegacy.jei.info.algae");
        addInfo(registry, BNCItems.TURTLE_EGG, "brewinandchewinlegacy.jei.info.turtle_egg");
    }

    private static void addInfo(IModRegistry registry, Item item, String key) {
        if (item != null) {
            registry.addIngredientInfo(new ItemStack(item), ItemStack.class, key);
        }
    }

    private static class PriorityPlugin implements IRecipeRegistryPlugin {
        @Override
        public <V> List<String> getRecipeCategoryUids(IFocus<V> focus) {
            if (focus == null || focus.getMode() != IFocus.Mode.OUTPUT || !(focus.getValue() instanceof ItemStack)) {
                return Collections.emptyList();
            }

            Item item = ((ItemStack) focus.getValue()).getItem();
            if (item == BNCItems.BEER
                || item == BNCItems.VODKA
                || item == BNCItems.MEAD
                || item == BNCItems.RICE_WINE
                || item == BNCItems.EGG_GROG
                || item == BNCItems.STRONGROOT_ALE
                || item == BNCItems.SACCHARINE_RUM
                || item == BNCItems.BLOODY_MARY
                || item == BNCItems.STEEL_TOE_STOUT
                || item == BNCItems.GLITTERING_GRENADINE
                || item == BNCItems.PALE_JANE
                || item == BNCItems.SALTY_FOLLY
                || item == BNCItems.DREAD_NOG
                || item == BNCItems.RED_RUM
                || item == BNCItems.WITHERING_DROSS
                || item == BNCItems.KOMBUCHA
                || item == BNCItems.JERKY
                || item == BNCItems.KIMCHI
                || item == BNCItems.KIPPERS
                || item == BNCItems.COCOA_FUDGE
                || item == BNCItems.PICKLED_PICKLES
                || item == BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM
                || item == BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM) {
                return Collections.singletonList(BNCJeiRecipeTypes.FERMENTING);
            }
            if (item == BNCBlocks.FLAXEN_CHEESE_WHEEL_ITEM
                || item == BNCBlocks.SCARLET_CHEESE_WHEEL_ITEM) {
                return Collections.singletonList(BNCJeiRecipeTypes.CHEESE_AGING);
            }
            return Collections.emptyList();
        }

        @Override
        public <T extends IRecipeWrapper, V> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
            return Collections.emptyList();
        }

        @Override
        public <T extends IRecipeWrapper> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory) {
            return Collections.emptyList();
        }
    }
}
