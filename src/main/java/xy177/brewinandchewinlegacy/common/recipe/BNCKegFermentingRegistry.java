package xy177.brewinandchewinlegacy.common.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

public final class BNCKegFermentingRegistry {
    private static final int FAST_FERMENTING = 4800;
    private static final int NORMAL_FERMENTING = 9600;
    private static final float MEDIUM_EXP = 1.0F;
    private static final List<BNCKegFermentingRecipe> RECIPES = new ArrayList<>();
    private static final List<Runnable> CHANGE_LISTENERS = new ArrayList<>();
    private static boolean defaultsRegistered;

    private BNCKegFermentingRegistry() {
    }

    public static void registerDefaults() {
        if (defaultsRegistered) {
            return;
        }
        defaultsRegistered = true;

        register("jerky", new ItemStack(BNCItems.JERKY, 3), NORMAL_FERMENTING, 4,
            "ore:bncJerkyMeat",
            "ore:bncJerkyMeat",
            "ore:bncJerkyMeat");

        register("kimchi", new ItemStack(BNCItems.KIMCHI, 2), NORMAL_FERMENTING, 4,
            "ore:cropCabbage",
            "ore:listAllveggie",
            "ore:bncKelp");

        register("kippers", new ItemStack(BNCItems.KIPPERS, 2), NORMAL_FERMENTING, 4,
            "ore:listAllfishraw",
            "ore:listAllfishraw",
            "ore:bncDriedKelp");

        registerFluidResult("beer", BNCKegFluid.BEER, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 3,
            "minecraft:wheat",
            "minecraft:wheat_seeds",
            "minecraft:brown_mushroom");

        registerFluidResult("vodka", BNCKegFluid.VODKA, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 3,
            "ore:cropPotato",
            "minecraft:wheat",
            "minecraft:wheat_seeds");

        registerFluidResult("mead", BNCKegFluid.MEAD, 1000, BNCKegFluid.HONEY, 1000, NORMAL_FERMENTING, 3,
            "minecraft:wheat",
            "minecraft:wheat_seeds",
            "ore:bncSweetBerries");

        registerFluidResult("rice_wine", BNCKegFluid.RICE_WINE, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 3,
            "ore:cropRice",
            "minecraft:brown_mushroom");

        registerFluidResult("egg_grog", BNCKegFluid.EGG_GROG, 1000, BNCKegFluid.MILK, 1000, NORMAL_FERMENTING, 3,
            "ore:listAllEgg",
            "ore:cropCabbage",
            "ore:bncSugar");

        registerFluidResult("strongroot_ale", BNCKegFluid.STRONGROOT_ALE, 1000, BNCKegFluid.BEER, 1000, FAST_FERMENTING, 3,
            "ore:cropBeetroot",
            "ore:cropPotato",
            "minecraft:brown_mushroom",
            "brewinandchewinlegacy:jerky");

        registerFluidResult("saccharine_rum", BNCKegFluid.SACCHARINE_RUM, 1000, BNCKegFluid.MEAD, 1000, FAST_FERMENTING, 4,
            "ore:bncSweetBerries",
            "minecraft:reeds",
            "minecraft:melon");

        registerFluidResult("bloody_mary", BNCKegFluid.BLOODY_MARY, 1000, BNCKegFluid.VODKA, 1000, FAST_FERMENTING, 4,
            "ore:cropTomato",
            "ore:cropCabbage",
            "ore:bncSweetBerries");

        registerFluidResult("steel_toe_stout", BNCKegFluid.STEEL_TOE_STOUT, 1000, BNCKegFluid.STRONGROOT_ALE, 1000, FAST_FERMENTING, 1,
            "minecraft:iron_ingot",
            "ore:bncCrimsonFungus",
            "minecraft:nether_wart",
            "minecraft:wheat");

        registerFluidResult("glittering_grenadine", BNCKegFluid.GLITTERING_GRENADINE, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 2,
            "ore:bncGlowBerries",
            "minecraft:glowstone_dust",
            "ore:bncGlowInkSac");

        registerFluidResult("pale_jane", BNCKegFluid.PALE_JANE, 1000, BNCKegFluid.RICE_WINE, 1000, FAST_FERMENTING, 4,
            "ore:bncHoneyItem",
            "farmersdelight:tree_bark",
            "ore:bncPaleFlower",
            "ore:bncSugar");

        registerFluidResult("salty_folly", BNCKegFluid.SALTY_FOLLY, 1000, BNCKegFluid.VODKA, 1000, FAST_FERMENTING, 2,
            "ore:bncSeaPickle",
            "ore:bncDriedKelp",
            "ore:bncSeagrass");

        registerFluidResult("dread_nog", BNCKegFluid.DREAD_NOG, 1000, BNCKegFluid.EGG_GROG, 1000, FAST_FERMENTING, 1,
            ominousBottleIngredient(),
            "ore:bncTurtleEgg",
            "minecraft:fermented_spider_eye");

        registerFluidResult("red_rum", BNCKegFluid.RED_RUM, 1000, BNCKegFluid.BLOODY_MARY, 1000, FAST_FERMENTING, 5,
            "ore:bncCrimsonFungus",
            "minecraft:nether_wart",
            "minecraft:fermented_spider_eye",
            "ore:bncShroomlight");

        registerFluidResult("withering_dross", BNCKegFluid.WITHERING_DROSS, 1000, BNCKegFluid.SALTY_FOLLY, 1000, NORMAL_FERMENTING, 5,
            "ore:bncWitherRose",
            "minecraft:dye:0",
            "minecraft:nether_wart",
            "minecraft:bone");

        if (BNCItems.KOMBUCHA != null) {
            registerFluidResult("kombucha", BNCKegFluid.KOMBUCHA, 1000, BNCKegFluid.GREEN_TEA, 1000, NORMAL_FERMENTING, 3,
                "ore:cropCarrot",
                "ore:bncSweetBerries",
                "teastory:black_tea_leaf");
        }

        registerFluidResult("flaxen_cheese", BNCKegFluid.FLAXEN_CHEESE, 1000, BNCKegFluid.MILK, 1000, NORMAL_FERMENTING, 4,
            "minecraft:brown_mushroom",
            "minecraft:pumpkin_seeds",
            "ore:bncSugar");

        registerFluidResult("scarlet_cheese", BNCKegFluid.SCARLET_CHEESE, 1000, BNCKegFluid.MILK, 1000, NORMAL_FERMENTING, 5,
            "ore:bncCrimsonFungus",
            "minecraft:nether_wart",
            "ore:bncSugar");

        registerWithFluid("pickled_pickles", new ItemStack(BNCItems.PICKLED_PICKLES, 2), BNCKegFluid.HONEY, 250, NORMAL_FERMENTING, 2,
            "ore:bncSeaPickle",
            "ore:bncSeaPickle",
            "ore:bncGlowBerries");

        registerWithFluid("cocoa_fudge", new ItemStack(BNCItems.COCOA_FUDGE), BNCKegFluid.MILK, 500, NORMAL_FERMENTING, 2,
            "ore:bncSugar",
            "minecraft:dye:3",
            "minecraft:dye:3");
    }

    public static List<BNCKegFermentingRecipe> getRecipes() {
        return Collections.unmodifiableList(new ArrayList<>(RECIPES));
    }

    public static boolean registerScriptItemRecipe(String recipeId, ItemStack result, String baseFluid,
                                                   int baseFluidAmount, float experience, int fermentTime,
                                                   int temperature, String... ingredients) {
        registerDefaults();
        if (result == null || result.isEmpty()
            || result.getCount() > Math.min(result.getMaxStackSize(), 64)
            || !isValidRecipe(experience, fermentTime, temperature, ingredients, true)) {
            return false;
        }
        String normalizedBaseFluid = baseFluid == null ? BNCKegFluid.EMPTY : baseFluid;
        if (normalizedBaseFluid.isEmpty() != (baseFluidAmount <= 0) || baseFluidAmount > BNCKegFluid.CAPACITY) {
            return false;
        }

        ResourceLocation id = parseRecipeId(recipeId);
        if (id == null) {
            return false;
        }
        replaceRecipe(new BNCKegFermentingRecipe(id, result.copy(), normalizedBaseFluid,
            Math.max(0, baseFluidAmount), experience, fermentTime, temperature, ingredients));
        return true;
    }

    public static boolean registerScriptFluidRecipe(String recipeId, String resultFluid, int resultFluidAmount,
                                                     String baseFluid, int baseFluidAmount, float experience,
                                                     int fermentTime, int temperature, String... ingredients) {
        return registerScriptFluidRecipe(recipeId, resultFluid, resultFluidAmount, baseFluid, baseFluidAmount,
            ItemStack.EMPTY, ItemStack.EMPTY, 0, experience, fermentTime, temperature, ingredients);
    }

    public static boolean registerScriptFluidRecipe(String recipeId, String resultFluid, int resultFluidAmount,
                                                     String baseFluid, int baseFluidAmount,
                                                     ItemStack pouringContainer, ItemStack pouringResult,
                                                     int pouringAmount, float experience, int fermentTime,
                                                     int temperature, String... ingredients) {
        registerDefaults();
        String normalizedResultFluid = resultFluid == null ? BNCKegFluid.EMPTY : resultFluid;
        String normalizedBaseFluid = baseFluid == null ? BNCKegFluid.EMPTY : baseFluid;
        boolean hasPouringContainer = pouringContainer != null && !pouringContainer.isEmpty();
        boolean hasPouringResult = pouringResult != null && !pouringResult.isEmpty();
        if (normalizedResultFluid.isEmpty() || resultFluidAmount <= 0 || resultFluidAmount > BNCKegFluid.CAPACITY
            || normalizedBaseFluid.isEmpty() != (baseFluidAmount <= 0) || baseFluidAmount > BNCKegFluid.CAPACITY
            || hasPouringContainer != hasPouringResult
            || hasPouringContainer && (pouringContainer.getCount() != 1
                || pouringResult.getCount() > Math.min(pouringResult.getMaxStackSize(), 64)
                || pouringAmount <= 0 || pouringAmount > BNCKegFluid.CAPACITY)
            || !hasPouringContainer && pouringAmount != 0
            || !isValidRecipe(experience, fermentTime, temperature, ingredients)) {
            return false;
        }

        ResourceLocation id = parseRecipeId(recipeId);
        if (id == null) {
            return false;
        }
        replaceRecipe(new BNCKegFermentingRecipe(id, normalizedResultFluid, resultFluidAmount,
            normalizedBaseFluid, Math.max(0, baseFluidAmount),
            hasPouringContainer ? pouringContainer.copy() : ItemStack.EMPTY,
            hasPouringResult ? pouringResult.copy() : ItemStack.EMPTY,
            Math.max(0, pouringAmount), experience, fermentTime, temperature, ingredients));
        return true;
    }

    public static boolean removeRecipe(String recipeId) {
        registerDefaults();
        ResourceLocation id = parseRecipeId(recipeId);
        if (id == null) {
            return false;
        }
        Iterator<BNCKegFermentingRecipe> iterator = RECIPES.iterator();
        while (iterator.hasNext()) {
            if (id.equals(iterator.next().getId())) {
                iterator.remove();
                notifyRecipesChanged();
                return true;
            }
        }
        return false;
    }

    public static int removeRecipesByItemOutput(ItemStack output) {
        registerDefaults();
        if (output == null || output.isEmpty()) {
            return 0;
        }
        int removed = 0;
        Iterator<BNCKegFermentingRecipe> iterator = RECIPES.iterator();
        while (iterator.hasNext()) {
            BNCKegFermentingRecipe recipe = iterator.next();
            ItemStack result = recipe.getResult();
            if (recipe.hasItemOutput() && ItemStack.areItemsEqual(result, output)
                && ItemStack.areItemStackTagsEqual(result, output)) {
                iterator.remove();
                removed++;
            }
        }
        if (removed > 0) {
            notifyRecipesChanged();
        }
        return removed;
    }

    public static int removeRecipesByFluidOutput(String fluidId) {
        registerDefaults();
        if (fluidId == null || fluidId.isEmpty()) {
            return 0;
        }
        int removed = 0;
        Iterator<BNCKegFermentingRecipe> iterator = RECIPES.iterator();
        while (iterator.hasNext()) {
            BNCKegFermentingRecipe recipe = iterator.next();
            if (recipe.hasFluidOutput() && fluidId.equals(recipe.getResultFluid())) {
                iterator.remove();
                removed++;
            }
        }
        if (removed > 0) {
            notifyRecipesChanged();
        }
        return removed;
    }

    public static void addChangeListener(Runnable listener) {
        if (listener != null && !CHANGE_LISTENERS.contains(listener)) {
            CHANGE_LISTENERS.add(listener);
        }
    }

    public static BNCKegFermentingRecipe findMatch(ItemStack[] inputs) {
        return findMatch(inputs, BNCKegFluid.EMPTY, 0);
    }

    public static BNCKegFermentingRecipe findMatch(ItemStack[] inputs, String fluidId, int fluidAmount) {
        for (BNCKegFermentingRecipe recipe : RECIPES) {
            if (recipe.matches(inputs) && matchesFluid(recipe, fluidId, fluidAmount)) {
                return recipe;
            }
        }
        return null;
    }

    public static BNCKegFermentingRecipe findCustomPouringRecipe(String fluidId, ItemStack container,
                                                                 int availableAmount) {
        registerDefaults();
        if (fluidId == null || fluidId.isEmpty() || container == null || container.isEmpty()
            || availableAmount <= 0) {
            return null;
        }
        for (BNCKegFermentingRecipe recipe : RECIPES) {
            if (recipe.hasFluidOutput()
                && recipe.hasCustomPouring()
                && fluidId.equals(recipe.getResultFluid())
                && availableAmount >= recipe.getPouringAmount()
                && recipe.matchesPouringContainer(container)) {
                return recipe;
            }
        }
        return null;
    }

    private static void register(String path, ItemStack result, int fermentTime, int temperature, String... ingredients) {
        RECIPES.add(new BNCKegFermentingRecipe(new ResourceLocation(BrewinAndChewinLegacy.MODID, path), result, fermentTime, temperature, ingredients));
    }

    private static void registerWithFluid(String path, ItemStack result, String baseFluid, int baseFluidAmount, int fermentTime, int temperature, String... ingredients) {
        RECIPES.add(new BNCKegFermentingRecipe(new ResourceLocation(BrewinAndChewinLegacy.MODID, path), result, baseFluid, baseFluidAmount, MEDIUM_EXP, fermentTime, temperature, ingredients));
    }

    private static void registerFluidResult(String path, String resultFluid, int resultFluidAmount, String baseFluid, int baseFluidAmount, int fermentTime, int temperature, String... ingredients) {
        RECIPES.add(new BNCKegFermentingRecipe(new ResourceLocation(BrewinAndChewinLegacy.MODID, path), resultFluid, resultFluidAmount, baseFluid, baseFluidAmount, MEDIUM_EXP, fermentTime, temperature, ingredients));
    }

    private static void replaceRecipe(BNCKegFermentingRecipe replacement) {
        for (int i = 0; i < RECIPES.size(); i++) {
            if (RECIPES.get(i).getId().equals(replacement.getId())) {
                RECIPES.set(i, replacement);
                notifyRecipesChanged();
                return;
            }
        }
        RECIPES.add(replacement);
        notifyRecipesChanged();
    }

    private static boolean isValidRecipe(float experience, int fermentTime, int temperature, String[] ingredients) {
        return isValidRecipe(experience, fermentTime, temperature, ingredients, false);
    }

    private static boolean isValidRecipe(float experience, int fermentTime, int temperature,
                                         String[] ingredients, boolean allowEmpty) {
        int minimumIngredients = allowEmpty ? 0 : 1;
        if (experience < 0.0F || fermentTime <= 0 || temperature < 1 || temperature > 5
            || ingredients == null || ingredients.length < minimumIngredients || ingredients.length > 4) {
            return false;
        }
        for (String ingredient : ingredients) {
            if (ingredient == null || ingredient.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchesFluid(BNCKegFermentingRecipe recipe, String fluidId, int fluidAmount) {
        if (!recipe.requiresFluid()) {
            return fluidAmount <= 0;
        }
        return recipe.getBaseFluid().equals(fluidId)
            && fluidAmount >= recipe.getBaseFluidAmount()
            && fluidAmount % recipe.getBaseFluidAmount() == 0;
    }

    private static ResourceLocation parseRecipeId(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return null;
        }
        try {
            String normalized = recipeId.trim();
            return normalized.indexOf(':') >= 0
                ? new ResourceLocation(normalized)
                : new ResourceLocation(BrewinAndChewinLegacy.MODID, normalized);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private static void notifyRecipesChanged() {
        for (Runnable listener : new ArrayList<>(CHANGE_LISTENERS)) {
            listener.run();
        }
    }

    private static String ominousBottleIngredient() {
        return OreDictionary.getOres("bncOminousBottle").isEmpty() ? "potion:poison" : "ore:bncOminousBottle";
    }
}
