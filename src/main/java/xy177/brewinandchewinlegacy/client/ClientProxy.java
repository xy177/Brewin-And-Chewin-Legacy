package xy177.brewinandchewinlegacy.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.client.particle.BNCFogParticle;
import xy177.brewinandchewinlegacy.client.gui.BNCKegGui;
import xy177.brewinandchewinlegacy.client.render.BNCCoasterModelRegistry;
import xy177.brewinandchewinlegacy.client.render.BNCCoasterRenderer;
import xy177.brewinandchewinlegacy.common.CommonProxy;
import xy177.brewinandchewinlegacy.common.gui.BNCGuiHandler;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;
import xy177.brewinandchewinlegacy.common.registry.BNCFluids;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;
import xy177.brewinandchewinlegacy.common.tile.BNCCoasterTileEntity;
import xy177.brewinandchewinlegacy.common.tile.BNCKegTileEntity;

@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(BNCCoasterTileEntity.class, new BNCCoasterRenderer());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BNCItems.registerModels();
        BNCBlocks.registerModels();
    }

    @SubscribeEvent
    public static void stitchFluidTextures(TextureStitchEvent.Pre event) {
        BNCFluids.spriteLocations().forEach(event.getMap()::registerSprite);
        BNCCoasterModelRegistry.spriteLocations().forEach(event.getMap()::registerSprite);
        for (int i = 0; i < BNCFogParticle.TEXTURES.length; i++) {
            event.getMap().registerSprite(BNCFogParticle.TEXTURES[i]);
        }
        BNCCoasterModelRegistry.clearCache();
    }

    public static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(
            item,
            0,
            new ModelResourceLocation(item.getRegistryName(), "inventory")
        );
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, BNCKegTileEntity keg) {
        if (id == BNCGuiHandler.KEG) {
            return new BNCKegGui(player.inventory, keg);
        }
        return null;
    }

    @Override
    public void spawnFogParticle(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new BNCFogParticle(world, x, y, z, motionX, motionY, motionZ));
    }
}
