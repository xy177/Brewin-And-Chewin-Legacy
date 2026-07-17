package xy177.brewinandchewinlegacy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xy177.brewinandchewinlegacy.common.tile.BNCKegTileEntity;

public class BNCKegContainer extends Container {
    private static final int KEG_SLOT_COUNT = 6;
    private static final int PLAYER_INV_START = KEG_SLOT_COUNT;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 36;

    private final BNCKegTileEntity keg;
    private int lastFermentTime;
    private int lastFermentTimeTotal;
    private int lastTemperature;
    private int lastFluidAmount;
    private int lastFluidCode;
    private int lastFermenting;

    public BNCKegContainer(InventoryPlayer playerInventory, BNCKegTileEntity keg) {
        this.keg = keg;

        int inputStartX = 39;
        int inputStartY = 17;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                addSlotToContainer(new Slot(keg, (row * 2) + column, inputStartX + (column * 18), inputStartY + (row * 18)));
            }
        }

        addSlotToContainer(new Slot(keg, 4, 91, 55));
        addSlotToContainer(new OutputSlot(keg, 5, 124, 55));

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                addSlotToContainer(new Slot(playerInventory, 9 + (row * 9) + column, 8 + (column * 18), 84 + (row * 18)));
            }
        }

        for (int column = 0; column < 9; ++column) {
            addSlotToContainer(new Slot(playerInventory, column, 8 + (column * 18), 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, keg);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            if (lastFermentTime != keg.getField(0)) {
                listener.sendWindowProperty(this, 0, keg.getField(0));
            }
            if (lastFermentTimeTotal != keg.getField(1)) {
                listener.sendWindowProperty(this, 1, keg.getField(1));
            }
            if (lastTemperature != keg.getField(2)) {
                listener.sendWindowProperty(this, 2, keg.getField(2));
            }
            if (lastFluidAmount != keg.getField(3)) {
                listener.sendWindowProperty(this, 3, keg.getField(3));
            }
            if (lastFluidCode != keg.getField(4)) {
                listener.sendWindowProperty(this, 4, keg.getField(4));
            }
            if (lastFermenting != keg.getField(5)) {
                listener.sendWindowProperty(this, 5, keg.getField(5));
            }
        }

        lastFermentTime = keg.getField(0);
        lastFermentTimeTotal = keg.getField(1);
        lastTemperature = keg.getField(2);
        lastFluidAmount = keg.getField(3);
        lastFluidCode = keg.getField(4);
        lastFermenting = keg.getField(5);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        keg.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return keg.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getStack();
        ItemStack original = stack.copy();

        if (index < KEG_SLOT_COUNT) {
            if (!mergeItemStack(stack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!mergeItemStack(stack, 0, KEG_SLOT_COUNT, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(playerIn, stack);
        return original;
    }

    public int getFermentProgressScaled() {
        int time = keg.getField(0);
        int total = keg.getField(1);
        return total > 0 && time > 0 ? time * 22 / total : 0;
    }

    public int getKegTemperature() {
        return keg.getField(2);
    }

    public int getFluidAmount() {
        return keg.getField(3);
    }

    public int getFluidCode() {
        return keg.getField(4);
    }

    public int getFermentTime() {
        return keg.getField(0);
    }

    public boolean isFermenting() {
        return keg.getField(5) != 0;
    }

    private static class OutputSlot extends Slot {
        private OutputSlot(BNCKegTileEntity inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
