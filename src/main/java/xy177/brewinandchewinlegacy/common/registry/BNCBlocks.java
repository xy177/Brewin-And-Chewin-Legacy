package xy177.brewinandchewinlegacy.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.client.ClientProxy;
import xy177.brewinandchewinlegacy.common.block.BNCBasicWoodBlock;
import xy177.brewinandchewinlegacy.common.block.BNCCheeseWheelBlock;
import xy177.brewinandchewinlegacy.common.block.BNCCoasterBlock;
import xy177.brewinandchewinlegacy.common.block.BNCFieryFonduePotBlock;
import xy177.brewinandchewinlegacy.common.block.BNCHeatingCaskBlock;
import xy177.brewinandchewinlegacy.common.block.BNCIceCrateBlock;
import xy177.brewinandchewinlegacy.common.block.BNCKegBlock;
import xy177.brewinandchewinlegacy.common.block.BNCSliceableFoodBlock;
import xy177.brewinandchewinlegacy.common.block.BNCUnripeCheeseWheelBlock;

public final class BNCBlocks {
    private static final List<Block> BLOCKS = new ArrayList<>();
    private static final List<Item> ITEM_BLOCKS = new ArrayList<>();

    public static Block PIZZA;
    public static Item PIZZA_ITEM;
    public static Block QUICHE;
    public static Item QUICHE_ITEM;
    public static Block FLAXEN_CHEESE_WHEEL;
    public static Item FLAXEN_CHEESE_WHEEL_ITEM;
    public static Block SCARLET_CHEESE_WHEEL;
    public static Item SCARLET_CHEESE_WHEEL_ITEM;
    public static Block FIERY_FONDUE_POT;
    public static Item FIERY_FONDUE_POT_ITEM;
    public static Block HEATING_CASK;
    public static Item HEATING_CASK_ITEM;
    public static Block ICE_CRATE;
    public static Item ICE_CRATE_ITEM;
    public static Block UNRIPE_FLAXEN_CHEESE_WHEEL;
    public static Item UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM;
    public static Block UNRIPE_SCARLET_CHEESE_WHEEL;
    public static Item UNRIPE_SCARLET_CHEESE_WHEEL_ITEM;
    public static Block KEG;
    public static Item KEG_ITEM;
    public static Block COASTER;
    public static Item COASTER_ITEM;

    private BNCBlocks() {
    }

    public static void register(IForgeRegistry<Block> registry) {
        BLOCKS.clear();

        PIZZA = register(registry, "pizza", new BNCSliceableFoodBlock("pizza_slice"));
        QUICHE = register(registry, "quiche", new BNCSliceableFoodBlock("quiche_slice"));
        FLAXEN_CHEESE_WHEEL = register(registry, "flaxen_cheese_wheel", new BNCCheeseWheelBlock("flaxen_cheese_wedge"));
        SCARLET_CHEESE_WHEEL = register(registry, "scarlet_cheese_wheel", new BNCCheeseWheelBlock("scarlet_cheese_wedge"));
        FIERY_FONDUE_POT = register(registry, "fiery_fondue_pot", new BNCFieryFonduePotBlock());
        HEATING_CASK = register(registry, "heating_cask", new BNCHeatingCaskBlock());
        ICE_CRATE = register(registry, "ice_crate", new BNCIceCrateBlock());
        UNRIPE_FLAXEN_CHEESE_WHEEL = register(registry, "unripe_flaxen_cheese_wheel", new BNCUnripeCheeseWheelBlock(() -> FLAXEN_CHEESE_WHEEL));
        UNRIPE_SCARLET_CHEESE_WHEEL = register(registry, "unripe_scarlet_cheese_wheel", new BNCUnripeCheeseWheelBlock(() -> SCARLET_CHEESE_WHEEL));
        KEG = register(registry, "keg", new BNCKegBlock());
        COASTER = register(registry, "coaster", new BNCCoasterBlock());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        ITEM_BLOCKS.clear();

        PIZZA_ITEM = registerItemBlock(registry, PIZZA, true);
        QUICHE_ITEM = registerItemBlock(registry, QUICHE, false);
        FLAXEN_CHEESE_WHEEL_ITEM = registerItemBlock(registry, FLAXEN_CHEESE_WHEEL, true);
        SCARLET_CHEESE_WHEEL_ITEM = registerItemBlock(registry, SCARLET_CHEESE_WHEEL, true);
        FIERY_FONDUE_POT_ITEM = registerItemBlock(registry, FIERY_FONDUE_POT, true);
        HEATING_CASK_ITEM = registerItemBlock(registry, HEATING_CASK, false);
        ICE_CRATE_ITEM = registerItemBlock(registry, ICE_CRATE, false);
        UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM = registerItemBlock(registry, UNRIPE_FLAXEN_CHEESE_WHEEL, true);
        UNRIPE_SCARLET_CHEESE_WHEEL_ITEM = registerItemBlock(registry, UNRIPE_SCARLET_CHEESE_WHEEL, true);
        KEG_ITEM = registerItemBlock(registry, KEG, false);
        COASTER_ITEM = registerItemBlock(registry, COASTER, false);
    }

    public static void registerModels() {
        for (Item item : ITEM_BLOCKS) {
            ClientProxy.registerItemModel(item);
        }
    }

    private static Block register(IForgeRegistry<Block> registry, String name, Block block) {
        block.setRegistryName(BrewinAndChewinLegacy.MODID, name);
        block.setUnlocalizedName(BrewinAndChewinLegacy.MODID + "." + name);
        block.setCreativeTab(BrewinAndChewinLegacy.CREATIVE_TAB);
        registry.register(block);
        BLOCKS.add(block);
        return block;
    }

    private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block, boolean stackOne) {
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        item.setUnlocalizedName(block.getUnlocalizedName());
        item.setCreativeTab(BrewinAndChewinLegacy.CREATIVE_TAB);
        if (stackOne) {
            item.setMaxStackSize(1);
        }
        registry.register(item);
        ITEM_BLOCKS.add(item);
        return item;
    }

}
