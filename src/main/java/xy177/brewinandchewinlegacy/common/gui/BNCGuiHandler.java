package xy177.brewinandchewinlegacy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.tile.BNCKegTileEntity;

public class BNCGuiHandler implements IGuiHandler {
    public static final int KEG = 0;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == KEG) {
            BNCKegTileEntity keg = getKeg(world, x, y, z);
            return keg == null ? null : new BNCKegContainer(player.inventory, keg);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == KEG) {
            BNCKegTileEntity keg = getKeg(world, x, y, z);
            return keg == null ? null : BrewinAndChewinLegacy.proxy.getClientGuiElement(id, player, keg);
        }
        return null;
    }

    private static BNCKegTileEntity getKeg(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        return tile instanceof BNCKegTileEntity ? (BNCKegTileEntity) tile : null;
    }
}
