package xy177.brewinandchewinlegacy.common.registry;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BNCCreativeTab extends CreativeTabs {
    public static final BNCCreativeTab INSTANCE = new BNCCreativeTab();

    private BNCCreativeTab() {
        super("brewinandchewinlegacy");
    }

    @Override
    public ItemStack getTabIconItem() {
        return BNCBlocks.KEG_ITEM == null ? ItemStack.EMPTY : new ItemStack(BNCBlocks.KEG_ITEM);
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> items) {
        add(items, BNCBlocks.KEG_ITEM);
        add(items, BNCBlocks.HEATING_CASK_ITEM);
        add(items, BNCBlocks.ICE_CRATE_ITEM);
        add(items, BNCBlocks.COASTER_ITEM);

        add(items, BNCItems.TANKARD);

        add(items, BNCItems.BEER);
        add(items, BNCItems.VODKA);
        add(items, BNCItems.MEAD);
        add(items, BNCItems.RICE_WINE);
        add(items, BNCItems.PALE_JANE);
        add(items, BNCItems.EGG_GROG);
        add(items, BNCItems.GLITTERING_GRENADINE);
        add(items, BNCItems.SACCHARINE_RUM);
        add(items, BNCItems.SALTY_FOLLY);
        add(items, BNCItems.BLOODY_MARY);
        add(items, BNCItems.RED_RUM);
        add(items, BNCItems.STRONGROOT_ALE);
        add(items, BNCItems.STEEL_TOE_STOUT);
        add(items, BNCItems.DREAD_NOG);
        add(items, BNCItems.WITHERING_DROSS);
        add(items, BNCItems.KOMBUCHA);

        add(items, BNCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL_ITEM);
        add(items, BNCBlocks.FLAXEN_CHEESE_WHEEL_ITEM);
        add(items, BNCItems.FLAXEN_CHEESE_WEDGE);
        add(items, BNCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL_ITEM);
        add(items, BNCBlocks.SCARLET_CHEESE_WHEEL_ITEM);
        add(items, BNCItems.SCARLET_CHEESE_WEDGE);

        add(items, BNCItems.VEGETABLE_OMELET);
        add(items, BNCItems.CREAMY_ONION_SOUP);
        add(items, BNCItems.CHEESY_PASTA);
        add(items, BNCItems.HORROR_LASAGNA);
        add(items, BNCItems.SCARLET_PIEROGI);

        add(items, BNCBlocks.FIERY_FONDUE_POT_ITEM);
        add(items, BNCItems.FIERY_FONDUE);

        add(items, BNCBlocks.PIZZA_ITEM);
        add(items, BNCBlocks.QUICHE_ITEM);
        add(items, BNCItems.PIZZA_SLICE);
        add(items, BNCItems.QUICHE_SLICE);

        add(items, BNCItems.HAM_AND_CHEESE_SANDWICH);
        add(items, BNCItems.KIMCHI);
        add(items, BNCItems.JERKY);
        add(items, BNCItems.PICKLED_PICKLES);
        add(items, BNCItems.KIPPERS);
        add(items, BNCItems.COCOA_FUDGE);

        add(items, BNCItems.SWEET_BERRY_JAM);
        add(items, BNCItems.GLOW_BERRY_MARMALADE);
        add(items, BNCItems.APPLE_JELLY);

        addExtraIngredients(items);
    }

    private static void addExtraIngredients(NonNullList<ItemStack> items) {
        add(items, BNCItems.SWEET_BERRIES);
        add(items, BNCItems.GLOW_BERRIES);
        add(items, BNCItems.CRIMSON_FUNGUS);
        add(items, BNCItems.WARPED_FUNGUS);
        add(items, BNCItems.KELP);
        add(items, BNCItems.DRIED_KELP);
        add(items, BNCItems.SEA_PICKLE);
        add(items, BNCItems.SEAGRASS);
        add(items, BNCItems.ADULTERATED_HONEY);
        add(items, BNCItems.SYNTHETIC_BEESWAX);
        add(items, BNCItems.GLOW_INK_SAC);
        add(items, BNCItems.TURTLE_EGG);
        add(items, BNCItems.WITHER_ROSE);
    }

    private static void add(NonNullList<ItemStack> items, Item item) {
        if (item != null) {
            items.add(new ItemStack(item));
        }
    }
}
