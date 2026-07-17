package xy177.brewinandchewinlegacy.common.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRegistry;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFluid;
import xy177.brewinandchewinlegacy.common.registry.BNCFluids;

@ZenRegister
@ZenClass("mods.brewinandchewinlegacy.Keg")
public final class ZenKegRecipes {
    private static final float DEFAULT_EXPERIENCE = 1.0F;

    private ZenKegRecipes() {
    }

    @ZenMethod
    public static boolean addItemRecipe(String name, IIngredient[] ingredients, IItemStack output,
                                        int fermentTime, int temperature) {
        return addItemRecipeAdvanced(name, ingredients, output, null, fermentTime, temperature, DEFAULT_EXPERIENCE);
    }

    @ZenMethod
    public static boolean addItemRecipeWithFluid(String name, IIngredient[] ingredients, IItemStack output,
                                                 ILiquidStack inputFluid, int fermentTime, int temperature) {
        return addItemRecipeAdvanced(name, ingredients, output, inputFluid, fermentTime, temperature, DEFAULT_EXPERIENCE);
    }

    @ZenMethod
    public static boolean addItemRecipeAdvanced(String name, IIngredient[] ingredients, IItemStack output,
                                                ILiquidStack inputFluid, int fermentTime, int temperature,
                                                float experience) {
        String[] ingredientTokens = toIngredientTokens(ingredients);
        ItemStack result = output == null ? ItemStack.EMPTY : CraftTweakerMC.getItemStack(output).copy();
        FluidStack baseFluid = toFluidStack(inputFluid);
        if (ingredientTokens == null || result.isEmpty() || !validateCommon(name, fermentTime, temperature, experience)) {
            return false;
        }

        boolean added = BNCKegFermentingRegistry.registerScriptItemRecipe(
            name,
            result,
            fluidId(baseFluid),
            fluidAmount(baseFluid),
            experience,
            fermentTime,
            temperature,
            ingredientTokens
        );
        return reportAdd(name, added);
    }

    @ZenMethod
    public static boolean addFluidRecipe(String name, IIngredient[] ingredients, ILiquidStack outputFluid,
                                         ILiquidStack inputFluid, int fermentTime, int temperature) {
        return addFluidRecipeAdvanced(name, ingredients, outputFluid, inputFluid, fermentTime, temperature, DEFAULT_EXPERIENCE);
    }

    @ZenMethod
    public static boolean addFluidRecipeWithoutInput(String name, IIngredient[] ingredients, ILiquidStack outputFluid,
                                                     int fermentTime, int temperature) {
        return addFluidRecipeAdvanced(name, ingredients, outputFluid, null, fermentTime, temperature, DEFAULT_EXPERIENCE);
    }

    @ZenMethod
    public static boolean addFluidRecipeWithContainer(String name, IIngredient[] ingredients,
                                                      ILiquidStack outputFluid, ILiquidStack inputFluid,
                                                      IItemStack pouringContainer, IItemStack pouringResult,
                                                      int pouringAmount, int fermentTime, int temperature) {
        return addFluidRecipeWithContainerAdvanced(name, ingredients, outputFluid, inputFluid,
            pouringContainer, pouringResult, pouringAmount, fermentTime, temperature, DEFAULT_EXPERIENCE);
    }

    @ZenMethod
    public static boolean addFluidRecipeWithoutInputWithContainer(String name, IIngredient[] ingredients,
                                                                  ILiquidStack outputFluid,
                                                                  IItemStack pouringContainer,
                                                                  IItemStack pouringResult, int pouringAmount,
                                                                  int fermentTime, int temperature) {
        return addFluidRecipeWithContainerAdvanced(name, ingredients, outputFluid, null,
            pouringContainer, pouringResult, pouringAmount, fermentTime, temperature, DEFAULT_EXPERIENCE);
    }

    @ZenMethod
    public static boolean addFluidRecipeWithContainerAdvanced(String name, IIngredient[] ingredients,
                                                              ILiquidStack outputFluid, ILiquidStack inputFluid,
                                                              IItemStack pouringContainer, IItemStack pouringResult,
                                                              int pouringAmount, int fermentTime, int temperature,
                                                              float experience) {
        String[] ingredientTokens = toIngredientTokens(ingredients, true);
        FluidStack resultFluid = toFluidStack(outputFluid);
        FluidStack baseFluid = toFluidStack(inputFluid);
        ItemStack container = pouringContainer == null
            ? ItemStack.EMPTY
            : CraftTweakerMC.getItemStack(pouringContainer).copy();
        ItemStack result = pouringResult == null
            ? ItemStack.EMPTY
            : CraftTweakerMC.getItemStack(pouringResult).copy();
        if (ingredientTokens == null || resultFluid == null
            || !validateCommon(name, fermentTime, temperature, experience)) {
            return false;
        }
        if (container.isEmpty() || result.isEmpty()) {
            CraftTweakerAPI.logError("Custom keg pouring requires both an empty container and a filled result");
            return false;
        }
        if (container.getCount() != 1) {
            CraftTweakerAPI.logError("A custom keg pouring container must represent exactly one item");
            return false;
        }
        if (pouringAmount <= 0 || pouringAmount > BNCKegFluid.CAPACITY) {
            CraftTweakerAPI.logError("Custom keg pouring amount must be between 1 and " + BNCKegFluid.CAPACITY + " mB");
            return false;
        }

        boolean added = BNCKegFermentingRegistry.registerScriptFluidRecipe(
            name,
            fluidId(resultFluid),
            resultFluid.amount,
            fluidId(baseFluid),
            fluidAmount(baseFluid),
            container,
            result,
            pouringAmount,
            experience,
            fermentTime,
            temperature,
            ingredientTokens
        );
        return reportAdd(name, added);
    }

    @ZenMethod
    public static boolean addFluidRecipeAdvanced(String name, IIngredient[] ingredients, ILiquidStack outputFluid,
                                                 ILiquidStack inputFluid, int fermentTime, int temperature,
                                                 float experience) {
        String[] ingredientTokens = toIngredientTokens(ingredients, true);
        FluidStack resultFluid = toFluidStack(outputFluid);
        FluidStack baseFluid = toFluidStack(inputFluid);
        if (ingredientTokens == null || resultFluid == null
            || !validateCommon(name, fermentTime, temperature, experience)) {
            return false;
        }

        boolean added = BNCKegFermentingRegistry.registerScriptFluidRecipe(
            name,
            fluidId(resultFluid),
            resultFluid.amount,
            fluidId(baseFluid),
            fluidAmount(baseFluid),
            experience,
            fermentTime,
            temperature,
            ingredientTokens
        );
        return reportAdd(name, added);
    }

    @ZenMethod
    public static boolean removeRecipe(String name) {
        boolean removed = BNCKegFermentingRegistry.removeRecipe(name);
        if (removed) {
            CraftTweakerAPI.logInfo("Removed Brewin' And Chewin' Legacy keg recipe " + name);
        } else {
            CraftTweakerAPI.logWarning("No Brewin' And Chewin' Legacy keg recipe matched id " + name);
        }
        return removed;
    }

    @ZenMethod
    public static int removeRecipesByItemOutput(IItemStack output) {
        ItemStack result = output == null ? ItemStack.EMPTY : CraftTweakerMC.getItemStack(output);
        int removed = BNCKegFermentingRegistry.removeRecipesByItemOutput(result);
        CraftTweakerAPI.logInfo("Removed " + removed + " Brewin' And Chewin' Legacy keg recipe(s) by item output");
        return removed;
    }

    @ZenMethod
    public static int removeRecipesByFluidOutput(ILiquidStack output) {
        FluidStack result = toFluidStack(output);
        int removed = result == null ? 0 : BNCKegFermentingRegistry.removeRecipesByFluidOutput(fluidId(result));
        CraftTweakerAPI.logInfo("Removed " + removed + " Brewin' And Chewin' Legacy keg recipe(s) by fluid output");
        return removed;
    }

    private static String[] toIngredientTokens(IIngredient[] ingredients) {
        return toIngredientTokens(ingredients, false);
    }

    private static String[] toIngredientTokens(IIngredient[] ingredients, boolean allowEmpty) {
        int minimum = allowEmpty ? 0 : 1;
        if (ingredients == null || ingredients.length < minimum || ingredients.length > 4) {
            CraftTweakerAPI.logError(allowEmpty
                ? "Fluid-output keg recipes require between 0 and 4 item ingredients"
                : "Item-output keg recipes require between 1 and 4 item ingredients");
            return null;
        }

        String[] tokens = new String[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            IIngredient ingredient = ingredients[i];
            if (ingredient == null || ingredient.getAmount() != 1) {
                CraftTweakerAPI.logError("Each keg ingredient must represent exactly one item");
                return null;
            }
            tokens[i] = toIngredientToken(ingredient);
            if (tokens[i] == null) {
                CraftTweakerAPI.logError("Unsupported keg ingredient: " + ingredient.toCommandString());
                return null;
            }
        }
        return tokens;
    }

    private static String toIngredientToken(IIngredient ingredient) {
        String command = ingredient.toCommandString();
        if (command == null) {
            return null;
        }
        command = command.trim();
        if (!command.startsWith("<") || !command.endsWith(">") || command.indexOf('>') != command.length() - 1) {
            return null;
        }

        String token = command.substring(1, command.length() - 1);
        if (token.startsWith("ore:")) {
            return token.length() > 4 ? token : null;
        }
        return token.indexOf(':') > 0 ? token : null;
    }

    private static FluidStack toFluidStack(ILiquidStack fluid) {
        if (fluid == null) {
            return null;
        }
        FluidStack stack = CraftTweakerMC.getLiquidStack(fluid);
        return stack == null || stack.getFluid() == null || stack.amount <= 0 ? null : stack.copy();
    }

    private static String fluidId(FluidStack fluid) {
        return fluid == null ? BNCKegFluid.EMPTY : BNCFluids.idFor(fluid.getFluid());
    }

    private static int fluidAmount(FluidStack fluid) {
        return fluid == null ? 0 : fluid.amount;
    }

    private static boolean validateCommon(String name, int fermentTime, int temperature, float experience) {
        if (name == null || name.trim().isEmpty()) {
            CraftTweakerAPI.logError("Keg recipe id cannot be empty");
            return false;
        }
        if (fermentTime <= 0) {
            CraftTweakerAPI.logError("Keg ferment time must be greater than zero ticks");
            return false;
        }
        if (temperature < 1 || temperature > 5) {
            CraftTweakerAPI.logError("Keg temperature must be between 1 and 5");
            return false;
        }
        if (experience < 0.0F) {
            CraftTweakerAPI.logError("Keg recipe experience cannot be negative");
            return false;
        }
        return true;
    }

    private static boolean reportAdd(String name, boolean added) {
        if (added) {
            CraftTweakerAPI.logInfo("Added Brewin' And Chewin' Legacy keg recipe " + name);
        } else {
            CraftTweakerAPI.logError("Failed to add Brewin' And Chewin' Legacy keg recipe " + name);
        }
        return added;
    }
}
