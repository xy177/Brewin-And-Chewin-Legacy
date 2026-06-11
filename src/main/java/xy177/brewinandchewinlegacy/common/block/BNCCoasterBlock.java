package xy177.brewinandchewinlegacy.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;
import xy177.brewinandchewinlegacy.common.tile.BNCCoasterTileEntity;

public class BNCCoasterBlock extends Block implements ITileEntityProvider {
    public static final PropertyInteger SIZE = PropertyInteger.create("size", 0, 4);
    public static final PropertyBool INVISIBLE = PropertyBool.create("invisible");

    private static final AxisAlignedBB COASTER_SHAPE = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D);
    private static final AxisAlignedBB TRAY_SHAPE = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);

    public BNCCoasterBlock() {
        super(Material.CARPET);
        setHardness(0.2F);
        setResistance(0.2F);
        setSoundType(SoundType.WOOD);
        setDefaultState(blockState.getBaseState()
            .withProperty(SIZE, 0)
            .withProperty(INVISIBLE, Boolean.FALSE));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof BNCCoasterTileEntity)) {
            return false;
        }

        BNCCoasterTileEntity coaster = (BNCCoasterTileEntity) tile;
        ItemStack held = player.getHeldItem(hand);
        if (!held.isEmpty()) {
            return handleHeldItem(worldIn, pos, state, player, hand, held, coaster);
        }
        return handleEmptyHand(worldIn, pos, state, player, coaster);
    }

    private boolean handleHeldItem(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                   ItemStack held, BNCCoasterTileEntity coaster) {
        if (state.getValue(INVISIBLE) && held.getItem() == BNCBlocks.COASTER_ITEM) {
            if (!world.isRemote) {
                if (!player.capabilities.isCreativeMode) {
                    held.shrink(1);
                    if (held.isEmpty()) {
                        player.setHeldItem(hand, ItemStack.EMPTY);
                    }
                }
                world.setBlockState(pos, state.withProperty(INVISIBLE, Boolean.FALSE), 3);
                world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
            }
            return true;
        }

        int size = state.getValue(SIZE);
        if (size >= 4) {
            return false;
        }
        if (!world.isRemote) {
            ItemStack stored = held.copy();
            stored.setCount(1);
            coaster.setStack(size, stored);
            if (!player.capabilities.isCreativeMode) {
                held.shrink(1);
                if (held.isEmpty()) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
            }
            world.setBlockState(pos, state.withProperty(SIZE, size + 1), 3);
            coaster.sync();
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.4F, 1.4F);
        }
        return true;
    }

    private boolean handleEmptyHand(World world, BlockPos pos, IBlockState state, EntityPlayer player, BNCCoasterTileEntity coaster) {
        int size = state.getValue(SIZE);
        if (size <= 0) {
            return false;
        }

        if (!world.isRemote) {
            if (player.isSneaking() && !state.getValue(INVISIBLE)) {
                giveToPlayer(player, new ItemStack(BNCBlocks.COASTER_ITEM));
                world.setBlockState(pos, state.withProperty(INVISIBLE, Boolean.TRUE), 3);
                coaster.sync();
                world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_BREAK, SoundCategory.BLOCKS, 0.7F, 1.0F);
                return true;
            }

            ItemStack stored = coaster.getStack(size - 1);
            coaster.setStack(size - 1, ItemStack.EMPTY);
            giveToPlayer(player, stored);
            if (state.getValue(INVISIBLE) && size == 1) {
                world.setBlockToAir(pos);
            } else {
                world.setBlockState(pos, state.withProperty(SIZE, size - 1), 3);
                coaster.sync();
            }
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.4F, 0.8F);
        }
        return true;
    }

    private static void giveToPlayer(EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty() || player.capabilities.isCreativeMode) {
            return;
        }
        ItemStack result = stack.copy();
        if (!player.inventory.addItemStackToInventory(result)) {
            player.dropItem(result, false);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new BNCCoasterTileEntity();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof BNCCoasterTileEntity) {
            ((BNCCoasterTileEntity) tile).setRotationSegment(rotationSegment(placer.rotationYaw + 180.0F));
        }
    }

    private static int rotationSegment(float yaw) {
        return net.minecraft.util.math.MathHelper.floor((yaw * 16.0F / 360.0F) + 0.5D) & 15;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canBlockStay(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        IBlockState down = world.getBlockState(pos.down());
        return down.isSideSolid(world, pos.down(), EnumFacing.UP);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof BNCCoasterTileEntity) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (BNCCoasterTileEntity) tile);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (!state.getValue(INVISIBLE)) {
            drops.add(new ItemStack(Item.getItemFromBlock(this)));
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(SIZE) > 1 ? TRAY_SHAPE : COASTER_SHAPE;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
        if (!state.getValue(INVISIBLE)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, getBoundingBox(state, worldIn, pos));
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Math.min(blockState.getValue(SIZE) * 4, 15);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, net.minecraft.util.math.RayTraceResult target, World world,
                                  BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof BNCCoasterTileEntity) {
            int size = state.getValue(SIZE);
            if (size > 0) {
                ItemStack stack = ((BNCCoasterTileEntity) tile).getStack(size - 1);
                if (!stack.isEmpty()) {
                    return stack.copy();
                }
            }
        }
        return new ItemStack(BNCBlocks.COASTER_ITEM);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SIZE) | (state.getValue(INVISIBLE) ? 8 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
            .withProperty(SIZE, Math.min(4, meta & 7))
            .withProperty(INVISIBLE, (meta & 8) != 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SIZE, INVISIBLE);
    }
}
