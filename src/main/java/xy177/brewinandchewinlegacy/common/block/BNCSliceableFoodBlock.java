package xy177.brewinandchewinlegacy.common.block;

import com.wdcftgg.farmersdelightlegacy.common.block.BlockPie;
import com.wdcftgg.farmersdelightlegacy.common.item.ItemKnife;
import com.wdcftgg.farmersdelightlegacy.common.registry.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public class BNCSliceableFoodBlock extends BlockPie {
    private final ResourceLocation sliceItemId;

    public BNCSliceableFoodBlock(String sliceItemName) {
        super("air");
        this.sliceItemId = new ResourceLocation(BrewinAndChewinLegacy.MODID, sliceItemName);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        ItemStack held = player.getHeldItem(hand);
        if (ItemKnife.isKnife(held)) {
            if (!worldIn.isRemote) {
                cutSlice(worldIn, pos, state, player, held);
            }
            return true;
        }
        return eatSlice(worldIn, pos, state, player);
    }

    private boolean eatSlice(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.canEat(false)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        Item sliceItem = getSliceItem();
        if (sliceItem == Items.AIR) {
            return false;
        }
        ItemStack slice = new ItemStack(sliceItem);
        if (sliceItem instanceof ItemFood) {
            ((ItemFood) sliceItem).onItemUseFinish(slice, worldIn, player);
        }
        removeSlice(worldIn, pos, state);
        worldIn.playSound(null, pos, net.minecraft.init.SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);
        spawnCrumbs(worldIn, pos);
        return true;
    }

    private void cutSlice(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack knife) {
        Item sliceItem = getSliceItem();
        if (sliceItem == Items.AIR) {
            return;
        }
        removeSlice(worldIn, pos, state);
        giveOrDrop(player, new ItemStack(sliceItem));
        worldIn.playSound(null, pos, ModSounds.foodSlice, SoundCategory.PLAYERS, 0.8F, 0.8F);
        spawnCrumbs(worldIn, pos);
        knife.damageItem(1, player);
    }

    private void giveOrDrop(EntityPlayer player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack.copy())) {
            player.dropItem(stack.copy(), false);
        }
    }

    private Item getSliceItem() {
        Item item = ForgeRegistries.ITEMS.getValue(sliceItemId);
        return item == null ? Items.AIR : item;
    }

    private void removeSlice(World worldIn, BlockPos pos, IBlockState state) {
        int bites = state.getValue(BlockPie.BITES);
        if (bites >= 3) {
            worldIn.setBlockToAir(pos);
        } else {
            worldIn.setBlockState(pos, state.withProperty(BlockPie.BITES, bites + 1), 3);
        }
    }

    private void spawnCrumbs(World worldIn, BlockPos pos) {
        for (int i = 0; i < 3; i++) {
            worldIn.spawnParticle(
                EnumParticleTypes.BLOCK_CRACK,
                pos.getX() + 0.5D + (worldIn.rand.nextDouble() - 0.5D) * 0.2D,
                pos.getY() + 0.3D,
                pos.getZ() + 0.5D + (worldIn.rand.nextDouble() - 0.5D) * 0.2D,
                0.0D,
                0.0D,
                0.0D,
                Block.getStateId(getDefaultState())
            );
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, java.util.Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (state.getValue(BlockPie.BITES) == 0) {
            Item item = Item.getItemFromBlock(this);
            if (item != Items.AIR) {
                drops.add(new ItemStack(item));
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        Item item = Item.getItemFromBlock(this);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
