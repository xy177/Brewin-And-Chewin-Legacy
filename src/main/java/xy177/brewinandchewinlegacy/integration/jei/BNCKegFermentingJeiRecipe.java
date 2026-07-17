package xy177.brewinandchewinlegacy.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.client.render.BNCFluidRenderer;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRecipe;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFluid;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;
import xy177.brewinandchewinlegacy.common.registry.BNCFluids;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

public class BNCKegFermentingJeiRecipe implements IRecipeWrapper {
    private static final ResourceLocation BG = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/jei/keg.png");

    private final BNCKegFermentingRecipe recipe;

    public BNCKegFermentingJeiRecipe(BNCKegFermentingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getJeiInputLists());
        FluidStack inputFluid = getInputFluidStack();
        if (inputFluid != null) {
            ingredients.setInput(VanillaTypes.FLUID, inputFluid);
        }
        FluidStack outputFluid = getOutputFluidStack();
        if (outputFluid != null) {
            ingredients.setOutput(VanillaTypes.FLUID, outputFluid);
        }
        ItemStack output = getOutput();
        if (!output.isEmpty()) {
            ingredients.setOutput(ItemStack.class, output);
        }
    }

    public List<List<ItemStack>> getInputLists() {
        List<List<ItemStack>> inputLists = new ArrayList<>();
        for (String ingredient : recipe.getIngredients()) {
            inputLists.add(resolveIngredient(ingredient));
        }
        return inputLists;
    }

    public List<List<ItemStack>> getJeiInputLists() {
        List<List<ItemStack>> inputLists = new ArrayList<>();
        if (hasFluidInput()) {
            inputLists.add(getFluidInputStacks());
        }
        inputLists.addAll(getInputLists());
        if (hasCatalyst()) {
            inputLists.add(getCatalystStacks());
        }
        return inputLists;
    }

    public ItemStack getOutput() {
        if (recipe.hasItemOutput()) {
            return recipe.getResult();
        }
        if (recipe.hasCustomPouring()) {
            return recipe.getPouringResult();
        }
        if (BNCKegFluid.isDrink(recipe.getResultFluid())) {
            return getDrinkStack(recipe.getResultFluid());
        }
        if (BNCKegFluid.FLAXEN_CHEESE.equals(recipe.getResultFluid()) && BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM != null) {
            return new ItemStack(BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM);
        }
        if (BNCKegFluid.SCARLET_CHEESE.equals(recipe.getResultFluid()) && BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM != null) {
            return new ItemStack(BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM);
        }
        return ItemStack.EMPTY;
    }

    public boolean hasFluidInput() {
        return recipe.requiresFluid();
    }

    public List<ItemStack> getFluidInputStacks() {
        if (!hasFluidInput()) {
            return Collections.emptyList();
        }
        return getFluidDisplayStacks(recipe.getBaseFluid(), recipe.getBaseFluidAmount());
    }

    public String getBaseFluid() {
        return recipe.getBaseFluid();
    }

    public int getBaseFluidAmount() {
        return recipe.getBaseFluidAmount();
    }

    public FluidStack getInputFluidStack() {
        return hasFluidInput() ? BNCFluids.stackFor(recipe.getBaseFluid(), recipe.getBaseFluidAmount()) : null;
    }

    public boolean hasCatalyst() {
        return recipe.hasFluidOutput() && !getCatalystStacks().isEmpty();
    }

    public List<ItemStack> getCatalystStacks() {
        if (!recipe.hasFluidOutput()) {
            return Collections.emptyList();
        }
        if (recipe.hasCustomPouring()) {
            return Collections.singletonList(recipe.getPouringContainer());
        }
        if (BNCKegFluid.isDrink(recipe.getResultFluid()) && BNCItems.TANKARD != null) {
            return Collections.singletonList(new ItemStack(BNCItems.TANKARD));
        }
        if (!BNCKegFluid.FLAXEN_CHEESE.equals(recipe.getResultFluid())
            && !BNCKegFluid.SCARLET_CHEESE.equals(recipe.getResultFluid())) {
            return Collections.emptyList();
        }
        Item honeycomb = resolveItem("futuremc:honeycomb");
        if (honeycomb != null) {
            return Collections.singletonList(new ItemStack(honeycomb));
        }
        return BNCItems.SYNTHETIC_BEESWAX == null ? Collections.emptyList() : Collections.singletonList(new ItemStack(BNCItems.SYNTHETIC_BEESWAX));
    }

    public int getPouringAmount() {
        return recipe.hasCustomPouring() ? recipe.getPouringAmount() : recipe.getResultFluidAmount();
    }

    public boolean hasFluidOutput() {
        return recipe.hasFluidOutput();
    }

    public String getResultFluid() {
        return recipe.getResultFluid();
    }

    public int getResultFluidAmount() {
        return recipe.getResultFluidAmount();
    }

    public String getBaseFluidDisplayName() {
        return getFluidDisplayName(recipe.getBaseFluid());
    }

    public String getResultFluidDisplayName() {
        return getFluidDisplayName(recipe.getResultFluid());
    }

    public List<ItemStack> getFluidOutputStacks() {
        if (!recipe.hasFluidOutput()) {
            return Collections.emptyList();
        }
        if (recipe.hasCustomPouring()) {
            return Collections.singletonList(recipe.getPouringResult());
        }
        return getFluidDisplayStacks(recipe.getResultFluid(), recipe.getResultFluidAmount());
    }

    public FluidStack getOutputFluidStack() {
        return recipe.hasFluidOutput() ? BNCFluids.stackFor(recipe.getResultFluid(), recipe.getResultFluidAmount()) : null;
    }

    public int getFermentTime() {
        return recipe.getFermentTime();
    }

    public int getTemperature() {
        return recipe.getTemperature();
    }

    public float getExperience() {
        return recipe.getExperience();
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FluidStack inputFluid = getInputFluidStack();
        if (inputFluid != null) {
            BNCFluidRenderer.drawTank(minecraft, inputFluid, 0, 2, 26, 30, 1000);
            minecraft.getTextureManager().bindTexture(BG);
            Gui.drawModalRectWithCustomSizedTexture(0, 2, 170, 45, 26, 30, 256, 256);
        }
        FluidStack outputFluid = getOutputFluidStack();
        if (outputFluid != null) {
            BNCFluidRenderer.drawTank(minecraft, outputFluid, 100, 2, 26, 30, 1000);
            minecraft.getTextureManager().bindTexture(BG);
            Gui.drawModalRectWithCustomSizedTexture(100, 2, 170, 45, 26, 30, 256, 256);
        }

        minecraft.getTextureManager().bindTexture(BG);
        int temperature = getTemperature();
        if (temperature <= 2) {
            Gui.drawModalRectWithCustomSizedTexture(33, 39, 178, 0, 9, 3, 256, 256);
        }
        if (temperature <= 1) {
            Gui.drawModalRectWithCustomSizedTexture(25, 39, 170, 0, 8, 3, 256, 256);
        }
        if (temperature >= 4) {
            Gui.drawModalRectWithCustomSizedTexture(50, 39, 195, 0, 9, 3, 256, 256);
        }
        if (temperature >= 5) {
            Gui.drawModalRectWithCustomSizedTexture(59, 39, 204, 0, 8, 3, 256, 256);
        }
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        if (hasFluidInput() && isInside(mouseX, mouseY, 0, 2, 26, 30)) {
            tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.fluid_input",
                getBaseFluidDisplayName(), getBaseFluidAmount()));
        } else if (hasFluidOutput() && isInside(mouseX, mouseY, 100, 2, 26, 30)) {
            tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.fluid_output",
                getResultFluidDisplayName(), getResultFluidAmount()));
        } else if (isInside(mouseX, mouseY, 67, 2, 22, 28)) {
            int fermentTime = getFermentTime();
            if (fermentTime >= 1200) {
                tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.time", fermentTime / 1200));
            } else {
                tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.time.seconds", fermentTime / 20));
            }
            if (getExperience() > 0.0F) {
                tooltip.add(I18n.format("brewinandchewinlegacy.jei.fermenting.tooltip.experience", getExperience()));
            }
        } else if (isInside(mouseX, mouseY, 24, 38, 44, 5)) {
            tooltip.add(I18n.format(getTemperatureKey()));
        }
        return tooltip;
    }

    private static List<ItemStack> resolveIngredient(String ingredient) {
        if (ingredient.startsWith("ore:")) {
            return new ArrayList<>(OreDictionary.getOres(ingredient.substring(4)));
        }
        if ("potion:poison".equals(ingredient)) {
            return Collections.singletonList(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.POISON));
        }

        List<ItemStack> stacks = new ArrayList<>();
        String[] parts = ingredient.split(":");
        if (parts.length >= 2) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
            if (item != null) {
                int meta = parts.length >= 3 && "*".equals(parts[2])
                    ? OreDictionary.WILDCARD_VALUE
                    : parts.length >= 3 ? Integer.parseInt(parts[2]) : 0;
                stacks.add(new ItemStack(item, 1, meta));
            }
        }
        return stacks;
    }

    private static List<ItemStack> getFluidDisplayStacks(String fluidId, int amount) {
        if (BNCKegFluid.MILK.equals(fluidId)) {
            Item milkBottle = resolveItem("farmersdelight:milk_bottle");
            if (milkBottle != null) {
                return Collections.singletonList(new ItemStack(milkBottle, Math.max(1, amount / 250)));
            }
            return Collections.singletonList(new ItemStack(Items.MILK_BUCKET, Math.max(1, amount / 1000)));
        }
        if (BNCKegFluid.WATER.equals(fluidId)) {
            return Collections.singletonList(new ItemStack(Items.WATER_BUCKET, Math.max(1, amount / 1000)));
        }
        if (BNCKegFluid.HONEY.equals(fluidId)) {
            Item honeyBottle = resolveItem("futuremc:honey_bottle");
            if (honeyBottle != null) {
                return Collections.singletonList(new ItemStack(honeyBottle, Math.max(1, amount / 250)));
            }
            if (BNCItems.ADULTERATED_HONEY != null) {
                return Collections.singletonList(new ItemStack(BNCItems.ADULTERATED_HONEY, Math.max(1, amount / 250)));
            }
        }
        if (BNCKegFluid.GREEN_TEA.equals(fluidId)) {
            Item greenTea = resolveItem("teastory:green_tea");
            if (greenTea != null) {
                int count = Math.max(1, amount / 250);
                List<ItemStack> stacks = new ArrayList<>();
                stacks.add(new ItemStack(greenTea, count, 0));
                stacks.add(new ItemStack(greenTea, count, 2));
                stacks.add(new ItemStack(greenTea, count, 3));
                stacks.add(new ItemStack(greenTea, count, 4));
                stacks.add(new ItemStack(greenTea, count, 5));
                return stacks;
            }
        }
        if (BNCKegFluid.isDrink(fluidId)) {
            ItemStack drink = getDrinkStack(fluidId);
            if (!drink.isEmpty()) {
                drink.setCount(Math.max(1, amount / 250));
                return Collections.singletonList(drink);
            }
        }
        if (BNCKegFluid.FLAXEN_CHEESE.equals(fluidId) && BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM != null) {
            return Collections.singletonList(new ItemStack(BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM, Math.max(1, amount / 1000)));
        }
        if (BNCKegFluid.SCARLET_CHEESE.equals(fluidId) && BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM != null) {
            return Collections.singletonList(new ItemStack(BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM, Math.max(1, amount / 1000)));
        }
        FluidStack fluid = BNCFluids.stackFor(fluidId, Math.max(1000, amount));
        if (fluid != null) {
            ItemStack bucket = FluidUtil.getFilledBucket(fluid);
            if (!bucket.isEmpty()) {
                bucket.setCount(Math.max(1, amount / 1000));
                return Collections.singletonList(bucket);
            }
        }
        return Collections.emptyList();
    }

    private static ItemStack getDrinkStack(String fluidId) {
        if (BNCKegFluid.BEER.equals(fluidId)) {
            return new ItemStack(BNCItems.BEER);
        }
        if (BNCKegFluid.VODKA.equals(fluidId)) {
            return new ItemStack(BNCItems.VODKA);
        }
        if (BNCKegFluid.MEAD.equals(fluidId)) {
            return new ItemStack(BNCItems.MEAD);
        }
        if (BNCKegFluid.RICE_WINE.equals(fluidId)) {
            return new ItemStack(BNCItems.RICE_WINE);
        }
        if (BNCKegFluid.EGG_GROG.equals(fluidId)) {
            return new ItemStack(BNCItems.EGG_GROG);
        }
        if (BNCKegFluid.STRONGROOT_ALE.equals(fluidId)) {
            return new ItemStack(BNCItems.STRONGROOT_ALE);
        }
        if (BNCKegFluid.SACCHARINE_RUM.equals(fluidId)) {
            return new ItemStack(BNCItems.SACCHARINE_RUM);
        }
        if (BNCKegFluid.BLOODY_MARY.equals(fluidId)) {
            return new ItemStack(BNCItems.BLOODY_MARY);
        }
        if (BNCKegFluid.STEEL_TOE_STOUT.equals(fluidId)) {
            return new ItemStack(BNCItems.STEEL_TOE_STOUT);
        }
        if (BNCKegFluid.GLITTERING_GRENADINE.equals(fluidId)) {
            return new ItemStack(BNCItems.GLITTERING_GRENADINE);
        }
        if (BNCKegFluid.PALE_JANE.equals(fluidId)) {
            return new ItemStack(BNCItems.PALE_JANE);
        }
        if (BNCKegFluid.SALTY_FOLLY.equals(fluidId)) {
            return new ItemStack(BNCItems.SALTY_FOLLY);
        }
        if (BNCKegFluid.DREAD_NOG.equals(fluidId)) {
            return new ItemStack(BNCItems.DREAD_NOG);
        }
        if (BNCKegFluid.RED_RUM.equals(fluidId)) {
            return new ItemStack(BNCItems.RED_RUM);
        }
        if (BNCKegFluid.WITHERING_DROSS.equals(fluidId)) {
            return new ItemStack(BNCItems.WITHERING_DROSS);
        }
        if (BNCKegFluid.KOMBUCHA.equals(fluidId) && BNCItems.KOMBUCHA != null) {
            return new ItemStack(BNCItems.KOMBUCHA);
        }
        return ItemStack.EMPTY;
    }

    private static Item resolveItem(String id) {
        String[] parts = id.split(":");
        if (parts.length != 2) {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
    }

    private static String getFluidDisplayName(String fluidId) {
        String localKey = "brewinandchewinlegacy.fluid." + fluidId;
        String localName = I18n.format(localKey);
        if (!localKey.equals(localName)) {
            return localName;
        }
        FluidStack stack = BNCFluids.stackFor(fluidId, 1);
        return stack == null ? fluidId : stack.getLocalizedName();
    }

    private String getTemperatureKey() {
        switch (getTemperature()) {
            case 1:
                return "brewinandchewinlegacy.container.keg.cold";
            case 2:
                return "brewinandchewinlegacy.container.keg.chilly";
            case 3:
                return "brewinandchewinlegacy.container.keg.normal";
            case 4:
                return "brewinandchewinlegacy.container.keg.warm";
            case 5:
                return "brewinandchewinlegacy.container.keg.hot";
            default:
                return "brewinandchewinlegacy.container.keg.normal";
        }
    }

    private static boolean isInside(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
