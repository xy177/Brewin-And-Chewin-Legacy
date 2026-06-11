package xy177.brewinandchewinlegacy.integration.jei;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BNCCheeseAgingJeiRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public BNCCheeseAgingJeiRecipe(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return java.util.Collections.emptyList();
    }
}
