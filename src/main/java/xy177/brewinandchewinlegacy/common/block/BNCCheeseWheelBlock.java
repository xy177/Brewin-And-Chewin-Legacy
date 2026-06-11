package xy177.brewinandchewinlegacy.common.block;

import com.wdcftgg.farmersdelightlegacy.common.item.ItemKnife;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import javax.annotation.Nullable;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public class BNCCheeseWheelBlock extends Block {
    public static final PropertyInteger SERVINGS = PropertyInteger.create("servings", 0, 3);

    private static final AxisAlignedBB QUARTER = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.5D, 0.375D, 0.5D);
    private static final AxisAlignedBB HALF = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.375D, 0.5D);
    private static final AxisAlignedBB L_SHAPE_LEG = new AxisAlignedBB(0.125D, 0.0D, 0.5D, 0.5D, 0.375D, 0.875D);
    private static final AxisAlignedBB FULL = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.375D, 0.875D);

    private static final AxisAlignedBB[] OUTLINE_SHAPES = new AxisAlignedBB[] {
        QUARTER,
        HALF,
        FULL,
        FULL
    };

    private static final AxisAlignedBB[][] COLLISION_SHAPES = new AxisAlignedBB[][] {
        {QUARTER},
        {HALF},
        {HALF, L_SHAPE_LEG},
        {FULL}
    };

    private final ResourceLocation wedgeItemId;

    public BNCCheeseWheelBlock(String wedgeItemName) {
        super(Material.CAKE);
        this.wedgeItemId = new ResourceLocation(BrewinAndChewinLegacy.MODID, wedgeItemName);
        setHardness(0.5F);
        setResistance(0.5F);
        setSoundType(SoundType.CLOTH);
        setDefaultState(blockState.getBaseState().withProperty(SERVINGS, 3));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        ItemStack held = player.getHeldItem(hand);
        if (!ItemKnife.isKnife(held)) {
            if (!worldIn.isRemote) {
                player.sendStatusMessage(new TextComponentTranslation("brewinandchewinlegacy.block.feast.use_knife"), true);
            }
            return true;
        }

        if (!worldIn.isRemote) {
            cutWedge(worldIn, pos, state, player);
        }
        return true;
    }

    private void cutWedge(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        int servings = state.getValue(SERVINGS);
        Item wedgeItem = ForgeRegistries.ITEMS.getValue(wedgeItemId);
        if (wedgeItem == null || wedgeItem == Items.AIR) {
            return;
        }
        giveOrDrop(player, new ItemStack(wedgeItem));
        worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (servings > 0) {
            worldIn.setBlockState(pos, state.withProperty(SERVINGS, servings - 1), 3);
        } else {
            worldIn.setBlockToAir(pos);
        }
    }

    private void giveOrDrop(EntityPlayer player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack.copy())) {
            player.dropItem(stack.copy(), false);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos)
            && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (fromPos.equals(pos.down()) && !canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return;
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return OUTLINE_SHAPES[state.getValue(SERVINGS)];
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        for (AxisAlignedBB shape : COLLISION_SHAPES[state.getValue(SERVINGS)]) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, shape);
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(SERVINGS);
    }

    @Override
    public Item getItemDropped(IBlockState state, java.util.Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (state.getValue(SERVINGS) == 3) {
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

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SERVINGS);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(SERVINGS, Math.max(0, Math.min(3, meta)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SERVINGS);
    }
}
