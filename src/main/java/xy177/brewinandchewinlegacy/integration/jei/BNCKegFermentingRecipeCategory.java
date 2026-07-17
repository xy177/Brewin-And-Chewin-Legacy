package xy177.brewinandchewinlegacy.integration.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;

public class BNCKegFermentingRecipeCategory implements IRecipeCategory<BNCKegFermentingJeiRecipe> {
    private static final ResourceLocation BG = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/jei/keg.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated leftBubble;
    private final IDrawableAnimated rightBubble;
    private final IDrawable timeIcon;
    private final IDrawable expIcon;

    public BNCKegFermentingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BG, 12, 13, 136, 56);
        this.icon = helper.createDrawableIngredient(new ItemStack(BNCBlocks.KEG_ITEM));
        this.arrow = helper.createAnimatedDrawable(helper.createDrawable(BG, 171, 4, 23, 16), 200, IDrawableAnimated.StartDirection.LEFT, false);
        this.leftBubble = helper.createAnimatedDrawable(helper.createDrawable(BG, 170, 75, 9, 24), 50, IDrawableAnimated.StartDirection.BOTTOM, false);
        this.rightBubble = helper.createAnimatedDrawable(helper.createDrawable(BG, 180, 75, 9, 24), 50, IDrawableAnimated.StartDirection.BOTTOM, false);
        this.timeIcon = helper.createDrawable(BG, 170, 21, 8, 11);
        this.expIcon = helper.createDrawable(BG, 170, 32, 9, 9);
    }

    @Override
    public String getUid() {
        return BNCJeiRecipeTypes.FERMENTING;
    }

    @Override
    public String getTitle() {
        return I18n.format("brewinandchewinlegacy.jei.fermenting.title");
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
    public void setRecipe(IRecipeLayout recipeLayout, BNCKegFermentingJeiRecipe recipeWrapper, IIngredients ingredients) {
        List<List<ItemStack>> inputs = recipeWrapper.getInputLists();
        int[][] positions = new int[][] {{28, 0}, {46, 0}, {28, 18}, {46, 18}};
        for (int i = 0; i < inputs.size() && i < positions.length; i++) {
            recipeLayout.getItemStacks().init(i, true, positions[i][0], positions[i][1]);
            recipeLayout.getItemStacks().set(i, inputs.get(i));
        }

        recipeLayout.getItemStacks().init(4, false, 104, 38);
        recipeLayout.getItemStacks().set(4, recipeWrapper.getOutput());

        if (recipeWrapper.hasFluidInput()) {
            recipeLayout.getItemStacks().init(5, true, 4, 4);
            recipeLayout.getItemStacks().set(5, recipeWrapper.getFluidInputStacks());
        }

        if (recipeWrapper.hasCatalyst()) {
            recipeLayout.getItemStacks().init(6, true, 72, 38);
            recipeLayout.getItemStacks().set(6, recipeWrapper.getCatalystStacks());
        }

        if (recipeWrapper.hasFluidOutput()) {
            recipeLayout.getItemStacks().init(7, false, 104, 4);
            recipeLayout.getItemStacks().set(7, recipeWrapper.getFluidOutputStacks());
        }

        recipeLayout.getItemStacks().addTooltipCallback(new FermentingTooltipCallback(recipeWrapper));
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        arrow.draw(minecraft, 67, 10);
        leftBubble.draw(minecraft, 90, 3);
        rightBubble.draw(minecraft, 127, 3);
        timeIcon.draw(minecraft, 70, 2);
        expIcon.draw(minecraft, 69, 21);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return new ArrayList<>();
    }

    private static class FermentingTooltipCallback implements ITooltipCallback<ItemStack> {
        private final BNCKegFermentingJeiRecipe recipe;

        private FermentingTooltipCallback(BNCKegFermentingJeiRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
            if (slotIndex == 5 && recipe.hasFluidInput()) {
                tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.fluid_input",
                    recipe.getBaseFluidDisplayName(),
                    recipe.getBaseFluidAmount()));
            } else if (slotIndex == 6 && recipe.hasCatalyst()) {
                tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.pouring",
                    recipe.getPouringAmount(),
                    recipe.getResultFluidDisplayName()));
            } else if (slotIndex == 7 && recipe.hasFluidOutput()) {
                tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.fluid_output",
                    recipe.getResultFluidDisplayName(),
                    recipe.getResultFluidAmount()));
            }
        }
    }
}
