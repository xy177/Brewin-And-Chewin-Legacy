package xy177.brewinandchewinlegacy.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;

public class BNCCheeseAgingRecipeCategory implements IRecipeCategory<BNCCheeseAgingJeiRecipe> {
    private static final ResourceLocation BG = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/jei/cheese_ripening.png");

    private final IDrawable background;
    private final IDrawable icon;

    public BNCCheeseAgingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BG, 8, 9, 102, 41);
        this.icon = helper.createDrawableIngredient(new ItemStack(BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM));
    }

    @Override
    public String getUid() {
        return BNCJeiRecipeTypes.CHEESE_AGING;
    }

    @Override
    public String getTitle() {
        return I18n.format("brewinandchewinlegacy.jei.cheese_aging.title");
    }

    @Override
    public String getModName() {
        return BrewinAndChewinLegacy.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BNCCheeseAgingJeiRecipe recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 0, 16);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getInput());
        recipeLayout.getItemStacks().init(1, false, 84, 16);
        recipeLayout.getItemStacks().set(1, recipeWrapper.getOutput());
    }
}
