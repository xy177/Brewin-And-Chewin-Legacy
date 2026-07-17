package xy177.brewinandchewinlegacy;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.event.RegistryEvent;
import xy177.brewinandchewinlegacy.common.CommonProxy;
import xy177.brewinandchewinlegacy.common.config.BNCConfig;
import xy177.brewinandchewinlegacy.common.gui.BNCGuiHandler;
import xy177.brewinandchewinlegacy.common.network.BNCNetwork;
import xy177.brewinandchewinlegacy.common.registry.BNCCreativeTab;
import xy177.brewinandchewinlegacy.common.registry.BNCBlocks;
import xy177.brewinandchewinlegacy.common.registry.BNCEnchantments;
import xy177.brewinandchewinlegacy.common.registry.BNCEffects;
import xy177.brewinandchewinlegacy.common.registry.BNCFluids;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;
import xy177.brewinandchewinlegacy.common.registry.BNCRecipeRegistry;
import xy177.brewinandchewinlegacy.common.tile.BNCCoasterTileEntity;
import xy177.brewinandchewinlegacy.common.tile.BNCKegTileEntity;

@Mod(
    modid = BrewinAndChewinLegacy.MODID,
    name = BrewinAndChewinLegacy.NAME,
    version = BrewinAndChewinLegacy.VERSION,
    dependencies = "required-after:farmersdelight;after:crafttweaker;after:futuremc;after:da;after:depthsupdate;after:oe;after:netherized;after:nb;after:deeperdepths;after:raids;after:teastory"
)
@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID)
public class BrewinAndChewinLegacy {
    public static final String MODID = "brewinandchewinlegacy";
    public static final String NAME = "Brewin' And Chewin' Legacy";
    public static final String VERSION = "1.1.1";
    public static final CreativeTabs CREATIVE_TAB = BNCCreativeTab.INSTANCE;

    @Mod.Instance(MODID)
    public static BrewinAndChewinLegacy instance;

    private static Logger logger;

    @SidedProxy(
        clientSide = "xy177.brewinandchewinlegacy.client.ClientProxy",
        serverSide = "xy177.brewinandchewinlegacy.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        BNCConfig.init(event.getSuggestedConfigurationFile());
        BNCNetwork.init();
        BNCFluids.register();
        GameRegistry.registerTileEntity(BNCKegTileEntity.class, MODID + ":keg");
        GameRegistry.registerTileEntity(BNCCoasterTileEntity.class, MODID + ":coaster");
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BNCGuiHandler());
        proxy.init(event);
        logger.info("{} initialized.", NAME);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BNCBlocks.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BNCItems.register(event.getRegistry());
        BNCBlocks.registerItemBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        BNCEnchantments.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        BNCEffects.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        BNCRecipeRegistry.registerCraftingRecipes(event.getRegistry());
    }

    public static Logger getLogger() {
        return logger;
    }
}
