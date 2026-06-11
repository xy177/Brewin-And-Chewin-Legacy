package xy177.brewinandchewinlegacy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

import java.util.List;

public class BNCFieryFonduePotBlock extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 1, 3);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private static final AxisAlignedBB OUTLINE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    private static final AxisAlignedBB LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 0.1875D, 0.75D);
    private static final AxisAlignedBB WALL_NORTH = new AxisAlignedBB(0.0D, 0.1875D, 0.0D, 1.0D, 1.0D, 0.125D);
    private static final AxisAlignedBB WALL_SOUTH = new AxisAlignedBB(0.0D, 0.1875D, 0.875D, 1.0D, 1.0D, 1.0D);
    private static final AxisAlignedBB WALL_EAST = new AxisAlignedBB(0.875D, 0.1875D, 0.0D, 1.0D, 1.0D, 1.0D);
    private static final AxisAlignedBB WALL_WEST = new AxisAlignedBB(0.0D, 0.1875D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BNCFieryFonduePotBlock() {
        super(Material.IRON);
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);
        setDefaultState(blockState.getBaseState()
            .withProperty(LEVEL, 3)
            .withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        ItemStack held = player.getHeldItem(hand);
        if (held.getItem() != Items.BOWL) {
            if (!worldIn.isRemote) {
                player.sendStatusMessage(new TextComponentTranslation(
                    "farmersdelight.block.feast.use_container",
                    new ItemStack(Items.BOWL).getDisplayName()), true);
            }
            return true;
        }

        if (!worldIn.isRemote) {
            takeServing(worldIn, pos, state, player, held);
        }
        return true;
    }

    private void takeServing(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack held) {
        int servings = state.getValue(LEVEL);
        if (!player.capabilities.isCreativeMode) {
            held.shrink(1);
        }

        ItemStack fondue = new ItemStack(BNCItems.FIERY_FONDUE);
        if (!player.inventory.addItemStackToInventory(fondue.copy())) {
            player.dropItem(fondue.copy(), false);
        }

        if (servings > 1) {
            worldIn.setBlockState(pos, state.withProperty(LEVEL, servings - 1), 3);
        } else {
            worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 3);
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, new ItemStack(Items.BONE)));
        }
        worldIn.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && isEntityInsideContent(state, pos, entityIn)) {
            entityIn.attackEntityFrom(DamageSource.LAVA, 4.0F);
        }
    }

    private boolean isEntityInsideContent(IBlockState state, BlockPos pos, Entity entity) {
        double contentHeight = pos.getY() + (6.0D + state.getValue(LEVEL) * 3.0D) / 16.0D;
        return entity.posY < contentHeight && entity.getEntityBoundingBox().maxY > pos.getY() + 0.25D;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return OUTLINE;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, LEGS);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, WALL_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, WALL_SOUTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, WALL_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, WALL_WEST);
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(LEVEL);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | ((state.getValue(LEVEL) - 1) << 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int level = Math.max(1, Math.min(3, ((meta >> 2) & 3) + 1));
        return getDefaultState()
            .withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
            .withProperty(LEVEL, level);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LEVEL);
    }
}
