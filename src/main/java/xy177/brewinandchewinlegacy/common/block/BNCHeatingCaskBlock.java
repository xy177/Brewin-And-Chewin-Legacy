package xy177.brewinandchewinlegacy.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BNCHeatingCaskBlock extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BNCHeatingCaskBlock() {
        super(Material.WOOD);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        ItemStack held = player.getHeldItem(hand);
        boolean waterBucket = held.getItem() == Items.WATER_BUCKET;
        boolean waterPotion = held.getItem() == Items.POTIONITEM && PotionUtils.getPotionFromItem(held) == PotionTypes.WATER;
        if (!waterBucket && !waterPotion) {
            return false;
        }

        if (!worldIn.isRemote) {
            if (!player.capabilities.isCreativeMode) {
                player.setHeldItem(hand, waterBucket ? new ItemStack(Items.BUCKET) : new ItemStack(Items.GLASS_BOTTLE));
            }
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
        } else {
            spawnSmokeBurst(worldIn, pos, worldIn.rand);
        }
        return true;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(5) == 0 && worldIn.isAirBlock(pos.up())) {
            worldIn.spawnParticle(
                EnumParticleTypes.SMOKE_LARGE,
                pos.getX() + 0.5D + randomOffset(rand),
                pos.getY() + 1.1D,
                pos.getZ() + 0.5D + randomOffset(rand),
                0.0D,
                0.005D,
                0.0D
            );
        }
    }

    private void spawnSmokeBurst(World worldIn, BlockPos pos, Random rand) {
        for (int i = 0; i < 20; i++) {
            worldIn.spawnParticle(
                EnumParticleTypes.SMOKE_LARGE,
                pos.getX() + 0.5D + randomOffset(rand),
                pos.getY() + 1.1D,
                pos.getZ() + 0.5D + randomOffset(rand),
                randomOffset(rand),
                0.15D,
                randomOffset(rand)
            );
        }
    }

    private double randomOffset(Random rand) {
        return rand.nextDouble() / 4.0D * (rand.nextBoolean() ? 1.0D : -1.0D);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
}
