package xy177.brewinandchewinlegacy.common.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public class BNCIceCrateBlock extends BNCBasicWoodBlock {
    private static final AxisAlignedBB FOG_AREA = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D);

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(2) != 0 || !isSpaceAbove(worldIn, pos)) {
            return;
        }

        for (EnumFacing facing : EnumFacing.values()) {
            spawnFogOnFace(worldIn, rand, pos, facing, facing == EnumFacing.UP ? 0.25D : 0.55D);
        }
    }

    private static boolean isSpaceAbove(World world, BlockPos pos) {
        IBlockState above = world.getBlockState(pos.up());
        AxisAlignedBB collision = above.getCollisionBoundingBox(world, pos.up());
        return collision == null || !FOG_AREA.offset(pos.up()).intersects(collision);
    }

    private static void spawnFogOnFace(World world, Random rand, BlockPos pos, EnumFacing facing, double offset) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;

        if (facing.getAxis() == EnumFacing.Axis.X) {
            x += facing.getFrontOffsetX() * offset;
            y += rand.nextDouble() - 0.5D;
            z += rand.nextDouble() - 0.5D;
        } else if (facing.getAxis() == EnumFacing.Axis.Y) {
            x += rand.nextDouble() - 0.5D;
            y += facing.getFrontOffsetY() * offset;
            z += rand.nextDouble() - 0.5D;
        } else {
            x += rand.nextDouble() - 0.5D;
            y += rand.nextDouble() - 0.5D;
            z += facing.getFrontOffsetZ() * offset;
        }

        double motionX = (rand.nextDouble() - 0.5D) * 0.01D;
        double motionY = -0.01D - rand.nextDouble() * 0.015D;
        double motionZ = (rand.nextDouble() - 0.5D) * 0.01D;
        BrewinAndChewinLegacy.proxy.spawnFogParticle(world, x, y, z, motionX, motionY, motionZ);
    }
}
