package xy177.brewinandchewinlegacy.common.tile;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xy177.brewinandchewinlegacy.common.config.BNCConfig;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFluid;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRecipe;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRegistry;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;
import xy177.brewinandchewinlegacy.common.registry.BNCFluids;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

public class BNCKegTileEntity extends TileEntity implements IInventory, net.minecraft.util.ITickable, IFluidHandler {
    private static final int SLOT_COUNT = 6;
    private static final int RANGE = 2;

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int fermentTime;
    private int fermentTimeTotal;
    private int temperature = 3;
    private String fluidId = BNCKegFluid.EMPTY;
    private int fluidAmount;
    private String lastRecipeId = "";
    private boolean fermenting;

    @Override
    public void update() {
        if (world != null && !world.isRemote) {
            if (world.getTotalWorldTime() % 80L == 0L) {
                updateTemperature();
            }
            processContainerSlot();
            updateFermenting();
        }
    }

    public void updateTemperature() {
        if (world == null || pos == null) {
            temperature = 3;
            return;
        }

        int heat = 0;
        int cold = 0;
        for (BlockPos checkPos : BlockPos.getAllInBoxMutable(pos.add(-RANGE, -RANGE, -RANGE), pos.add(RANGE, RANGE, RANGE))) {
            IBlockState state = world.getBlockState(checkPos);
            if (BNCConfig.isHeatSource(state)) {
                heat++;
            }
            if (BNCConfig.isColdSource(state)) {
                cold++;
            }
        }

        if (heat >= 2) {
            temperature = 5;
        } else if (heat >= 1) {
            temperature = 4;
        } else if (cold >= 2) {
            temperature = 1;
        } else if (cold >= 1) {
            temperature = 2;
        } else {
            temperature = 3;
        }
        markDirty();
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[] {
            new FluidTankProperties(BNCFluids.stackFor(fluidId, fluidAmount), BNCKegFluid.CAPACITY)
        };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() == null || resource.amount <= 0) {
            return 0;
        }
        String incomingFluid = BNCFluids.idFor(resource.getFluid());
        int accepted = Math.min(resource.amount, BNCKegFluid.CAPACITY - fluidAmount);
        if (incomingFluid.isEmpty() || accepted <= 0 || fluidAmount > 0 && !incomingFluid.equals(fluidId)) {
            return 0;
        }
        if (doFill) {
            if (fluidAmount == 0) {
                fluidId = incomingFluid;
            }
            fluidAmount += accepted;
            syncToClient();
        }
        return accepted;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null || resource.getFluid() == null
            || !BNCFluids.idFor(resource.getFluid()).equals(fluidId)) {
            return null;
        }
        return drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (maxDrain <= 0 || fluidAmount <= 0) {
            return null;
        }
        FluidStack drained = BNCFluids.stackFor(fluidId, Math.min(maxDrain, fluidAmount));
        if (drained == null) {
            return null;
        }
        if (doDrain) {
            fluidAmount -= drained.amount;
            normalizeFluid();
            syncToClient();
        }
        return drained;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    public String getTemperatureName() {
        switch (temperature) {
            case 1:
                return new TextComponentTranslation("brewinandchewinlegacy.container.keg.cold").getFormattedText();
            case 2:
                return new TextComponentTranslation("brewinandchewinlegacy.container.keg.chilly").getFormattedText();
            case 4:
                return new TextComponentTranslation("brewinandchewinlegacy.container.keg.warm").getFormattedText();
            case 5:
                return new TextComponentTranslation("brewinandchewinlegacy.container.keg.hot").getFormattedText();
            default:
                return new TextComponentTranslation("brewinandchewinlegacy.container.keg.normal").getFormattedText();
        }
    }

    public int getComparatorOutput() {
        int occupied = 0;
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                occupied++;
            }
        }
        if (fluidAmount > 0) {
            occupied++;
        }
        return Math.round((occupied / (float) (SLOT_COUNT + 1)) * 15.0F);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("FermentTime", fermentTime);
        compound.setInteger("FermentTimeTotal", fermentTimeTotal);
        compound.setInteger("Temperature", temperature);
        compound.setString("FluidId", fluidId);
        compound.setInteger("FluidAmount", fluidAmount);
        compound.setString("LastRecipeId", lastRecipeId);
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stack.writeToNBT(stackTag);
                compound.setTag("Slot" + i, stackTag);
            }
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        fermentTime = compound.getInteger("FermentTime");
        fermentTimeTotal = compound.getInteger("FermentTimeTotal");
        temperature = compound.hasKey("Temperature", 3) ? compound.getInteger("Temperature") : 3;
        fluidId = compound.getString("FluidId");
        fluidAmount = compound.getInteger("FluidAmount");
        normalizeFluid();
        lastRecipeId = compound.getString("LastRecipeId");
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, compound.hasKey("Slot" + i, 10) ? new ItemStack(compound.getCompoundTag("Slot" + i)) : ItemStack.EMPTY);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        if (world != null) {
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public String getName() {
        return "container.brewinandchewinlegacy.keg";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = net.minecraft.inventory.ItemStackHelper.getAndSplit(inventory, index, count);
        if (!stack.isEmpty()) {
            markDirty();
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = net.minecraft.inventory.ItemStackHelper.getAndRemove(inventory, index);
        if (!stack.isEmpty()) {
            markDirty();
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world != null
            && world.getTileEntity(pos) == this
            && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != 5;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return fermentTime;
            case 1:
                return fermentTimeTotal;
            case 2:
                return temperature;
            case 3:
                return fluidAmount;
            case 4:
                return BNCFluids.networkCodeFor(fluidId);
            case 5:
                return fermenting ? 1 : 0;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                fermentTime = value;
                break;
            case 1:
                fermentTimeTotal = value;
                break;
            case 2:
                temperature = value;
                break;
            case 3:
                fluidAmount = Math.max(0, Math.min(BNCKegFluid.CAPACITY, value));
                if (fluidAmount == 0) {
                    fluidId = BNCKegFluid.EMPTY;
                }
                break;
            case 4:
                fluidId = BNCFluids.idFromNetworkCode(value);
                break;
            case 5:
                fermenting = value != 0;
                break;
            default:
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 6;
    }

    @Override
    public void clear() {
        inventory.clear();
        markDirty();
    }

    private void updateFermenting() {
        fermenting = false;
        BNCKegFermentingRecipe recipe = BNCKegFermentingRegistry.findMatch(getInputStacks(), fluidId, fluidAmount);
        if (recipe == null || !recipe.canOutputTo(inventory.get(5))) {
            coolDownProgress();
            return;
        }

        String recipeId = recipe.getId().toString();
        if (!recipeId.equals(lastRecipeId)) {
            fermentTime = 0;
            lastRecipeId = recipeId;
        }
        fermentTimeTotal = recipe.getFermentTime();

        if (!isValidTemperature(temperature, recipe.getTemperature())) {
            coolDownProgress();
            return;
        }

        fermenting = true;
        fermentTime++;
        if (fermentTime >= fermentTimeTotal) {
            finishRecipe(recipe);
        }
        markDirty();
    }

    private ItemStack[] getInputStacks() {
        return new ItemStack[] {
            inventory.get(0),
            inventory.get(1),
            inventory.get(2),
            inventory.get(3)
        };
    }

    private void finishRecipe(BNCKegFermentingRecipe recipe) {
        fermenting = false;
        if (recipe.hasFluidOutput()) {
            fluidId = recipe.getResultFluid();
            fluidAmount = recipe.getResultFluidAmount();
        } else {
            if (recipe.requiresFluid()) {
                fluidAmount -= recipe.getBaseFluidAmount();
                normalizeFluid();
            }
            ItemStack result = recipe.getResult();
            ItemStack output = inventory.get(5);
            if (output.isEmpty()) {
                inventory.set(5, result);
            } else {
                output.grow(result.getCount());
            }
        }

        for (int i = 0; i < 4; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    inventory.set(i, ItemStack.EMPTY);
                }
            }
        }

        fermentTime = 0;
        fermentTimeTotal = recipe.getFermentTime();
        lastRecipeId = "";
        world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.6F, 0.8F);
        markDirty();
    }

    private void processContainerSlot() {
        ItemStack container = inventory.get(4);
        if (container.isEmpty()) {
            return;
        }

        if (tryFillFromContainer(container) || tryExtractToContainer(container)) {
            markDirty();
        }
    }

    public boolean interactWithHeldItem(EntityPlayer player, EnumHand hand, ItemStack heldStack) {
        if (heldStack.isEmpty()) {
            return false;
        }

        ContainerFluid fill = getContainerFill(heldStack);
        if (fill != null) {
            if (!canAcceptFluid(fill.fluidId, fill.amount)) {
                return true;
            }
            if (fluidAmount == 0) {
                fluidId = fill.fluidId;
            }
            fluidAmount += fill.amount;
            consumeHeldItem(player, hand, heldStack, fill.remainder);
            playContainerTransferSound();
            syncToClient();
            return true;
        }

        if (isExtractionContainer(heldStack)) {
            ContainerFluid extract = getContainerExtraction(heldStack);
            if (extract != null && extract.fluidId.equals(fluidId) && fluidAmount >= extract.amount) {
                fluidAmount -= extract.amount;
                normalizeFluid();
                consumeHeldItem(player, hand, heldStack, extract.remainder);
                playContainerTransferSound();
                syncToClient();
            }
            return true;
        }

        return false;
    }

    private void playContainerTransferSound() {
        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private boolean tryFillFromContainer(ItemStack container) {
        ContainerFluid fill = getContainerFill(container);
        if (fill == null || !canAcceptFluid(fill.fluidId, fill.amount) || !canOutputStack(fill.remainder)) {
            return false;
        }

        if (fluidAmount == 0) {
            fluidId = fill.fluidId;
        }
        fluidAmount += fill.amount;
        container.shrink(1);
        if (container.isEmpty()) {
            inventory.set(4, ItemStack.EMPTY);
        }
        addOutputStack(fill.remainder);
        return true;
    }

    private boolean tryExtractToContainer(ItemStack container) {
        ContainerFluid extract = getContainerExtraction(container);
        if (extract == null || !extract.fluidId.equals(fluidId) || fluidAmount < extract.amount || !canOutputStack(extract.remainder)) {
            return false;
        }

        fluidAmount -= extract.amount;
        normalizeFluid();
        container.shrink(1);
        if (container.isEmpty()) {
            inventory.set(4, ItemStack.EMPTY);
        }
        addOutputStack(extract.remainder);
        return true;
    }

    private boolean canAcceptFluid(String incomingFluidId, int incomingAmount) {
        return incomingAmount > 0
            && (fluidAmount <= 0 || incomingFluidId.equals(fluidId))
            && fluidAmount + incomingAmount <= BNCKegFluid.CAPACITY;
    }

    private boolean canOutputStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        ItemStack output = inventory.get(5);
        if (output.isEmpty()) {
            return true;
        }
        return ItemStack.areItemsEqual(output, stack)
            && ItemStack.areItemStackTagsEqual(output, stack)
            && output.getCount() + stack.getCount() <= Math.min(output.getMaxStackSize(), 64);
    }

    private void addOutputStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        ItemStack output = inventory.get(5);
        if (output.isEmpty()) {
            inventory.set(5, stack.copy());
        } else {
            output.grow(stack.getCount());
        }
    }

    private void normalizeFluid() {
        if (fluidAmount <= 0 || fluidId == null || fluidId.isEmpty()) {
            fluidId = BNCKegFluid.EMPTY;
            fluidAmount = 0;
        } else if (fluidAmount > BNCKegFluid.CAPACITY) {
            fluidAmount = BNCKegFluid.CAPACITY;
        }
    }

    private ContainerFluid getContainerFill(ItemStack stack) {
        if (isItem(stack, "farmersdelight:milk_bottle")) {
            return new ContainerFluid(BNCKegFluid.MILK, 250, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (stack.getItem() == Items.MILK_BUCKET) {
            return new ContainerFluid(BNCKegFluid.MILK, 1000, new ItemStack(Items.BUCKET));
        }
        if (stack.getItem() == Items.WATER_BUCKET) {
            return new ContainerFluid(BNCKegFluid.WATER, 1000, new ItemStack(Items.BUCKET));
        }

        if (isItem(stack, "futuremc:honey_bottle")) {
            return new ContainerFluid(BNCKegFluid.HONEY, 250, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (BNCItems.ADULTERATED_HONEY != null && stack.getItem() == BNCItems.ADULTERATED_HONEY) {
            return new ContainerFluid(BNCKegFluid.HONEY, 250, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (isItem(stack, "teastory:green_tea")) {
            Item cup = ForgeRegistries.ITEMS.getValue(new ResourceLocation("teastory:cup"));
            if (cup != null) {
                return new ContainerFluid(BNCKegFluid.GREEN_TEA, 250, new ItemStack(cup, 1, stack.getMetadata()));
            }
        }
        String drinkFluid = getDrinkFluid(stack);
        if (!drinkFluid.isEmpty()) {
            return new ContainerFluid(drinkFluid, 250, new ItemStack(BNCItems.TANKARD));
        }
        ContainerFluid ceramicsFill = getCeramicsContainerFill(stack);
        if (ceramicsFill != null) {
            return ceramicsFill;
        }
        return getGenericContainerFill(stack);
    }

    private ContainerFluid getCeramicsContainerFill(ItemStack stack) {
        Item ceramicsBucket = ForgeRegistries.ITEMS.getValue(new ResourceLocation("ceramics:clay_bucket"));
        if (ceramicsBucket == null || stack == null || stack.isEmpty() || stack.getItem() != ceramicsBucket
            || !stack.hasTagCompound()) {
            return null;
        }

        NBTTagCompound root = stack.getTagCompound();
        NBTTagCompound fluidTag = root.hasKey("fluids", 10)
            ? root.getCompoundTag("fluids")
            : root.hasKey("Fluid", 10) ? root.getCompoundTag("Fluid") : null;
        if (fluidTag == null) {
            return null;
        }

        String fluidName = fluidTag.getString("FluidName");
        int amount = fluidTag.getInteger("Amount");
        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null || amount < 1000) {
            return null;
        }
        return new ContainerFluid(BNCFluids.idFor(fluid), 1000, new ItemStack(ceramicsBucket));
    }

    private ContainerFluid getContainerExtraction(ItemStack stack) {
        BNCKegFermentingRecipe customPouring = BNCKegFermentingRegistry.findCustomPouringRecipe(
            fluidId, stack, fluidAmount);
        if (customPouring != null) {
            return new ContainerFluid(fluidId, customPouring.getPouringAmount(), customPouring.getPouringResult());
        }
        if (stack.getItem() == Items.GLASS_BOTTLE) {
            if (BNCKegFluid.MILK.equals(fluidId) && hasItem("farmersdelight:milk_bottle")) {
                return new ContainerFluid(BNCKegFluid.MILK, 250, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("farmersdelight:milk_bottle"))));
            }
            if (BNCKegFluid.HONEY.equals(fluidId) && hasItem("futuremc:honey_bottle")) {
                return new ContainerFluid(BNCKegFluid.HONEY, 250, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("futuremc:honey_bottle"))));
            }
            if (BNCKegFluid.HONEY.equals(fluidId) && BNCItems.ADULTERATED_HONEY != null) {
                return new ContainerFluid(BNCKegFluid.HONEY, 250, new ItemStack(BNCItems.ADULTERATED_HONEY));
            }
        }
        if (isItem(stack, "futuremc:honeycomb") || BNCItems.SYNTHETIC_BEESWAX != null && stack.getItem() == BNCItems.SYNTHETIC_BEESWAX) {
            if (BNCKegFluid.FLAXEN_CHEESE.equals(fluidId)) {
                return new ContainerFluid(BNCKegFluid.FLAXEN_CHEESE, 1000, new ItemStack(BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM));
            }
            if (BNCKegFluid.SCARLET_CHEESE.equals(fluidId)) {
                return new ContainerFluid(BNCKegFluid.SCARLET_CHEESE, 1000, new ItemStack(BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM));
            }
        }
        if (stack.getItem() == BNCItems.TANKARD && BNCKegFluid.isDrink(fluidId)) {
            ItemStack drink = getDrinkStack(fluidId);
            if (!drink.isEmpty()) {
                return new ContainerFluid(fluidId, 250, drink);
            }
        }
        return getGenericContainerExtraction(stack);
    }

    private boolean isExtractionContainer(ItemStack stack) {
        if (BNCKegFermentingRegistry.findCustomPouringRecipe(fluidId, stack, BNCKegFluid.CAPACITY) != null) {
            return true;
        }
        return isItem(stack, "futuremc:honeycomb")
            || BNCItems.SYNTHETIC_BEESWAX != null && stack.getItem() == BNCItems.SYNTHETIC_BEESWAX
            || stack.getItem() == BNCItems.TANKARD && BNCKegFluid.isDrink(fluidId)
            || stack.getItem() == Items.GLASS_BOTTLE && (BNCKegFluid.MILK.equals(fluidId) || BNCKegFluid.HONEY.equals(fluidId) && (hasItem("futuremc:honey_bottle") || BNCItems.ADULTERATED_HONEY != null))
            || getGenericContainerExtraction(stack) != null;
    }

    private ContainerFluid getGenericContainerFill(ItemStack stack) {
        ItemStack singleContainer = stack.copy();
        singleContainer.setCount(1);
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(singleContainer);
        if (handler == null) {
            return null;
        }

        int availableSpace = BNCKegFluid.CAPACITY - fluidAmount;
        FluidStack simulated = availableSpace <= 0 ? null : handler.drain(availableSpace, false);
        if (simulated == null || simulated.getFluid() == null || simulated.amount <= 0) {
            return null;
        }
        String incomingFluid = BNCFluids.idFor(simulated.getFluid());
        if (!canAcceptFluid(incomingFluid, simulated.amount)) {
            return null;
        }

        FluidStack drained = handler.drain(simulated.amount, true);
        return drained == null || drained.amount <= 0
            ? null
            : new ContainerFluid(incomingFluid, drained.amount, handler.getContainer());
    }

    private ContainerFluid getGenericContainerExtraction(ItemStack stack) {
        FluidStack available = BNCFluids.stackFor(fluidId, fluidAmount);
        if (available == null) {
            return null;
        }

        ItemStack singleContainer = stack.copy();
        singleContainer.setCount(1);
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(singleContainer);
        if (handler == null) {
            return null;
        }

        int accepted = handler.fill(available, false);
        if (accepted <= 0) {
            return null;
        }
        FluidStack transfer = available.copy();
        transfer.amount = accepted;
        int filled = handler.fill(transfer, true);
        return filled <= 0 ? null : new ContainerFluid(fluidId, filled, handler.getContainer());
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

    private static String getDrinkFluid(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return BNCKegFluid.EMPTY;
        }
        if (BNCItems.BEER != null && stack.getItem() == BNCItems.BEER) {
            return BNCKegFluid.BEER;
        }
        if (BNCItems.VODKA != null && stack.getItem() == BNCItems.VODKA) {
            return BNCKegFluid.VODKA;
        }
        if (BNCItems.MEAD != null && stack.getItem() == BNCItems.MEAD) {
            return BNCKegFluid.MEAD;
        }
        if (BNCItems.RICE_WINE != null && stack.getItem() == BNCItems.RICE_WINE) {
            return BNCKegFluid.RICE_WINE;
        }
        if (BNCItems.EGG_GROG != null && stack.getItem() == BNCItems.EGG_GROG) {
            return BNCKegFluid.EGG_GROG;
        }
        if (BNCItems.STRONGROOT_ALE != null && stack.getItem() == BNCItems.STRONGROOT_ALE) {
            return BNCKegFluid.STRONGROOT_ALE;
        }
        if (BNCItems.SACCHARINE_RUM != null && stack.getItem() == BNCItems.SACCHARINE_RUM) {
            return BNCKegFluid.SACCHARINE_RUM;
        }
        if (BNCItems.BLOODY_MARY != null && stack.getItem() == BNCItems.BLOODY_MARY) {
            return BNCKegFluid.BLOODY_MARY;
        }
        if (BNCItems.STEEL_TOE_STOUT != null && stack.getItem() == BNCItems.STEEL_TOE_STOUT) {
            return BNCKegFluid.STEEL_TOE_STOUT;
        }
        if (BNCItems.GLITTERING_GRENADINE != null && stack.getItem() == BNCItems.GLITTERING_GRENADINE) {
            return BNCKegFluid.GLITTERING_GRENADINE;
        }
        if (BNCItems.PALE_JANE != null && stack.getItem() == BNCItems.PALE_JANE) {
            return BNCKegFluid.PALE_JANE;
        }
        if (BNCItems.SALTY_FOLLY != null && stack.getItem() == BNCItems.SALTY_FOLLY) {
            return BNCKegFluid.SALTY_FOLLY;
        }
        if (BNCItems.DREAD_NOG != null && stack.getItem() == BNCItems.DREAD_NOG) {
            return BNCKegFluid.DREAD_NOG;
        }
        if (BNCItems.RED_RUM != null && stack.getItem() == BNCItems.RED_RUM) {
            return BNCKegFluid.RED_RUM;
        }
        if (BNCItems.WITHERING_DROSS != null && stack.getItem() == BNCItems.WITHERING_DROSS) {
            return BNCKegFluid.WITHERING_DROSS;
        }
        if (BNCItems.KOMBUCHA != null && stack.getItem() == BNCItems.KOMBUCHA) {
            return BNCKegFluid.KOMBUCHA;
        }
        return BNCKegFluid.EMPTY;
    }

    private void consumeHeldItem(EntityPlayer player, EnumHand hand, ItemStack heldStack, ItemStack remainder) {
        if (!player.capabilities.isCreativeMode) {
            heldStack.shrink(1);
            if (heldStack.isEmpty()) {
                player.setHeldItem(hand, remainder.isEmpty() ? ItemStack.EMPTY : remainder.copy());
            } else {
                giveToPlayer(player, remainder);
            }
        }
    }

    private void giveToPlayer(EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        ItemStack result = stack.copy();
        if (!player.inventory.addItemStackToInventory(result)) {
            player.dropItem(result, false);
        }
    }

    private void syncToClient() {
        markDirty();
        if (world != null && pos != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private static boolean isItem(ItemStack stack, String id) {
        return stack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
    }

    private static boolean hasItem(String id) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)) != null;
    }

    private static final class ContainerFluid {
        private final String fluidId;
        private final int amount;
        private final ItemStack remainder;

        private ContainerFluid(String fluidId, int amount, ItemStack remainder) {
            this.fluidId = fluidId;
            this.amount = amount;
            this.remainder = remainder;
        }
    }

    private void coolDownProgress() {
        if (fermentTime > 0) {
            fermentTime = Math.max(0, fermentTime - 20);
            markDirty();
        }
    }

    private static boolean isValidTemperature(int actual, int wanted) {
        switch (wanted) {
            case 1:
                return actual <= 1;
            case 2:
                return actual <= 2;
            case 3:
                return actual > 1 && actual < 5;
            case 4:
                return actual >= 4;
            case 5:
                return actual >= 5;
            default:
                return false;
        }
    }
}
