package xy177.brewinandchewinlegacy.common.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class BNCKegFermentingRecipe {
    private final ResourceLocation id;
    private final List<String> ingredients;
    private final ItemStack result;
    private final String resultFluid;
    private final int resultFluidAmount;
    private final String baseFluid;
    private final int baseFluidAmount;
    private final ItemStack pouringContainer;
    private final ItemStack pouringResult;
    private final int pouringAmount;
    private final float experience;
    private final int fermentTime;
    private final int temperature;

    public BNCKegFermentingRecipe(ResourceLocation id, ItemStack result, int fermentTime, int temperature, String... ingredients) {
        this(id, result, BNCKegFluid.EMPTY, 0, BNCKegFluid.EMPTY, 0,
            ItemStack.EMPTY, ItemStack.EMPTY, 0, 1.0F, fermentTime, temperature, ingredients);
    }

    public BNCKegFermentingRecipe(ResourceLocation id, ItemStack result, String baseFluid, int baseFluidAmount, float experience, int fermentTime, int temperature, String... ingredients) {
        this(id, result, BNCKegFluid.EMPTY, 0, baseFluid, baseFluidAmount,
            ItemStack.EMPTY, ItemStack.EMPTY, 0, experience, fermentTime, temperature, ingredients);
    }

    public BNCKegFermentingRecipe(ResourceLocation id, String resultFluid, int resultFluidAmount, String baseFluid, int baseFluidAmount, float experience, int fermentTime, int temperature, String... ingredients) {
        this(id, resultFluid, resultFluidAmount, baseFluid, baseFluidAmount,
            ItemStack.EMPTY, ItemStack.EMPTY, 0, experience, fermentTime, temperature, ingredients);
    }

    public BNCKegFermentingRecipe(ResourceLocation id, String resultFluid, int resultFluidAmount,
                                  String baseFluid, int baseFluidAmount, ItemStack pouringContainer,
                                  ItemStack pouringResult, int pouringAmount, float experience,
                                  int fermentTime, int temperature, String... ingredients) {
        this(id, ItemStack.EMPTY, resultFluid, resultFluidAmount, baseFluid, baseFluidAmount,
            pouringContainer, pouringResult, pouringAmount, experience, fermentTime, temperature, ingredients);
    }

    private BNCKegFermentingRecipe(ResourceLocation id, ItemStack result, String resultFluid,
                                   int resultFluidAmount, String baseFluid, int baseFluidAmount,
                                   ItemStack pouringContainer, ItemStack pouringResult, int pouringAmount,
                                   float experience, int fermentTime, int temperature, String... ingredients) {
        this.id = id;
        this.ingredients = Arrays.asList(ingredients);
        this.result = result == null ? ItemStack.EMPTY : result.copy();
        this.resultFluid = resultFluid == null ? BNCKegFluid.EMPTY : resultFluid;
        this.resultFluidAmount = resultFluidAmount;
        this.baseFluid = baseFluid == null ? BNCKegFluid.EMPTY : baseFluid;
        this.baseFluidAmount = baseFluidAmount;
        this.pouringContainer = pouringContainer == null ? ItemStack.EMPTY : pouringContainer.copy();
        this.pouringResult = pouringResult == null ? ItemStack.EMPTY : pouringResult.copy();
        this.pouringAmount = pouringAmount;
        this.experience = experience;
        this.fermentTime = fermentTime;
        this.temperature = temperature;
    }

    public ResourceLocation getId() {
        return id;
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public boolean hasItemOutput() {
        return !result.isEmpty();
    }

    public boolean hasFluidOutput() {
        return !resultFluid.isEmpty() && resultFluidAmount > 0;
    }

    public String getResultFluid() {
        return resultFluid;
    }

    public int getResultFluidAmount() {
        return resultFluidAmount;
    }

    public boolean requiresFluid() {
        return !baseFluid.isEmpty() && baseFluidAmount > 0;
    }

    public String getBaseFluid() {
        return baseFluid;
    }

    public int getBaseFluidAmount() {
        return baseFluidAmount;
    }

    public boolean hasCustomPouring() {
        return !pouringContainer.isEmpty() && !pouringResult.isEmpty() && pouringAmount > 0;
    }

    public ItemStack getPouringContainer() {
        return pouringContainer.copy();
    }

    public ItemStack getPouringResult() {
        return pouringResult.copy();
    }

    public int getPouringAmount() {
        return pouringAmount;
    }

    public boolean matchesPouringContainer(ItemStack stack) {
        return hasCustomPouring() && stack != null && !stack.isEmpty()
            && ItemStack.areItemsEqual(pouringContainer, stack)
            && ItemStack.areItemStackTagsEqual(pouringContainer, stack);
    }

    public float getExperience() {
        return experience;
    }

    public int getFermentTime() {
        return fermentTime;
    }

    public int getTemperature() {
        return temperature;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public boolean matches(ItemStack[] inputs) {
        List<ItemStack> remaining = new ArrayList<>();
        for (ItemStack stack : inputs) {
            if (!stack.isEmpty()) {
                remaining.add(stack);
            }
        }
        if (remaining.size() != ingredients.size()) {
            return false;
        }

        for (String ingredient : ingredients) {
            int matchIndex = findMatch(remaining, ingredient);
            if (matchIndex < 0) {
                return false;
            }
            remaining.remove(matchIndex);
        }
        return remaining.isEmpty();
    }

    public boolean canOutputTo(ItemStack outputSlot) {
        if (!hasItemOutput()) {
            return true;
        }
        if (outputSlot.isEmpty()) {
            return true;
        }
        if (!ItemStack.areItemsEqual(outputSlot, result) || !ItemStack.areItemStackTagsEqual(outputSlot, result)) {
            return false;
        }
        return outputSlot.getCount() + result.getCount() <= Math.min(outputSlot.getMaxStackSize(), 64);
    }

    private static int findMatch(List<ItemStack> stacks, String ingredient) {
        for (int i = 0; i < stacks.size(); i++) {
            if (matches(stacks.get(i), ingredient)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean matches(ItemStack stack, String ingredient) {
        if (ingredient.startsWith("ore:")) {
            for (ItemStack oreStack : OreDictionary.getOres(ingredient.substring(4))) {
                if (OreDictionary.itemMatches(oreStack, stack, false)) {
                    return true;
                }
            }
            return false;
        }
        if ("potion:poison".equals(ingredient)) {
            return stack.getItem() == Items.POTIONITEM && PotionUtils.getPotionFromItem(stack) == PotionTypes.POISON;
        }

        String[] parts = ingredient.split(":");
        if (parts.length < 2) {
            return false;
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
        if (item == null || stack.getItem() != item) {
            return false;
        }
        if (parts.length < 3 || "*".equals(parts[2])) {
            return true;
        }
        try {
            int metadata = Integer.parseInt(parts[2]);
            return metadata == OreDictionary.WILDCARD_VALUE || stack.getMetadata() == metadata;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
