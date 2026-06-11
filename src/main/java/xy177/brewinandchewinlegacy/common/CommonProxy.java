package xy177.brewinandchewinlegacy.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import xy177.brewinandchewinlegacy.common.command.BNCCoasterTestCommand;
import xy177.brewinandchewinlegacy.common.registry.BNCRecipeRegistry;
import xy177.brewinandchewinlegacy.common.registry.BNCVillagerTrades;
import xy177.brewinandchewinlegacy.common.tile.BNCKegTileEntity;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
        BNCRecipeRegistry.registerRuntimeRecipes();
        BNCVillagerTrades.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new BNCCoasterTestCommand());
    }

    public Object getClientGuiElement(int id, EntityPlayer player, BNCKegTileEntity keg) {
        return null;
    }

    public void spawnFogParticle(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
    }
}
