package xy177.brewinandchewinlegacy.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.client.ClientProxy;
import xy177.brewinandchewinlegacy.common.item.BNCBoozeItem;
import xy177.brewinandchewinlegacy.common.item.BNCFoodItem;
import xy177.brewinandchewinlegacy.common.item.BNCTooltipItem;

public final class BNCItems {
    private static final List<Item> ITEMS = new ArrayList<>();

    public static Item TANKARD;
    public static Item BEER;
    public static Item VODKA;
    public static Item MEAD;
    public static Item RICE_WINE;
    public static Item EGG_GROG;
    public static Item STRONGROOT_ALE;
    public static Item SACCHARINE_RUM;
    public static Item BLOODY_MARY;
    public static Item STEEL_TOE_STOUT;
    public static Item GLITTERING_GRENADINE;
    public static Item PALE_JANE;
    public static Item SALTY_FOLLY;
    public static Item DREAD_NOG;
    public static Item RED_RUM;
    public static Item WITHERING_DROSS;
    public static Item KOMBUCHA;
    public static Item FLAXEN_CHEESE_WEDGE;
    public static Item SCARLET_CHEESE_WEDGE;
    public static Item VEGETABLE_OMELET;
    public static Item CREAMY_ONION_SOUP;
    public static Item CHEESY_PASTA;
    public static Item HORROR_LASAGNA;
    public static Item SCARLET_PIEROGI;
    public static Item FIERY_FONDUE;
    public static Item PIZZA_SLICE;
    public static Item QUICHE_SLICE;
    public static Item HAM_AND_CHEESE_SANDWICH;
    public static Item KIMCHI;
    public static Item JERKY;
    public static Item PICKLED_PICKLES;
    public static Item KIPPERS;
    public static Item COCOA_FUDGE;
    public static Item SWEET_BERRIES;
    public static Item GLOW_BERRIES;
    public static Item SWEET_BERRY_JAM;
    public static Item GLOW_BERRY_MARMALADE;
    public static Item APPLE_JELLY;
    public static Item CRIMSON_FUNGUS;
    public static Item WARPED_FUNGUS;
    public static Item KELP;
    public static Item DRIED_KELP;
    public static Item SEA_PICKLE;
    public static Item SEAGRASS;
    public static Item ADULTERATED_HONEY;
    public static Item SYNTHETIC_BEESWAX;
    public static Item GLOW_INK_SAC;
    public static Item TURTLE_EGG;
    public static Item WITHER_ROSE;

    private BNCItems() {
    }

    public static void register(IForgeRegistry<Item> registry) {
        ITEMS.clear();

        TANKARD = register("tankard", plain());
        BEER = register("beer", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F));
        VODKA = register("vodka", tankardDrink()
            .addEffect(bncEffect("tipsy"), 3600, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 3000, 0, 1.0F));
        MEAD = register("mead", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(bncEffect("sweet_heart"), 2400, 0, 1.0F));
        RICE_WINE = register("rice_wine", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(effect("farmersdelight", "comfort"), 1200, 0, 1.0F));
        EGG_GROG = register("egg_grog", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(effect("minecraft", "absorption"), 600, 0, 1.0F));
        STRONGROOT_ALE = register("strongroot_ale", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 1, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(effect("minecraft", "resistance"), 600, 0, 1.0F));
        SACCHARINE_RUM = register("saccharine_rum", tankardDrink()
            .addEffect(bncEffect("tipsy"), 3600, 1, 1.0F)
            .addEffect(bncEffect("intoxication"), 2400, 0, 1.0F)
            .addEffect(bncEffect("sweet_heart"), 3600, 0, 1.0F));
        BLOODY_MARY = register("bloody_mary", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 1, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(bncEffect("raging"), 1200, 0, 1.0F));
        STEEL_TOE_STOUT = register("steel_toe_stout", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 2, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(effect("minecraft", "resistance"), 1200, 0, 1.0F));
        GLITTERING_GRENADINE = register("glittering_grenadine", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(effect("minecraft", "glowing"), 600, 0, 1.0F)
            .addEffect(effect("minecraft", "night_vision"), 600, 0, 1.0F));
        PALE_JANE = register("pale_jane", tankardDrink()
            .addEffect(bncEffect("tipsy"), 3600, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 3000, 0, 1.0F)
            .addEffect(effect("farmersdelight", "comfort"), 2400, 0, 1.0F));
        SALTY_FOLLY = register("salty_folly", tankardDrink()
            .addEffect(bncEffect("tipsy"), 3600, 1, 1.0F)
            .addEffect(bncEffect("intoxication"), 3000, 0, 1.0F)
            .addEffect(effect("minecraft", "water_breathing"), 1800, 0, 1.0F));
        DREAD_NOG = register("dread_nog", tankardDrink()
            .addEffect(bncEffect("tipsy"), 4800, 2, 1.0F)
            .addEffect(bncEffect("intoxication"), 4200, 0, 1.0F)
            .addEffect(effect("minecraft", "bad_omen"), 72000, 0, 1.0F)
            .addEffect(effect("deeperdepths", "bad_omen"), 72000, 0, 1.0F)
            .addEffect(effect("raids", "bad_omen"), 72000, 0, 1.0F));
        RED_RUM = register("red_rum", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 2, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(bncEffect("raging"), 2400, 0, 1.0F));
        WITHERING_DROSS = register("withering_dross", tankardDrink()
            .addEffect(bncEffect("tipsy"), 3600, 2, 1.0F)
            .addEffect(bncEffect("intoxication"), 3000, 0, 1.0F)
            .addEffect(effect("minecraft", "blindness"), 200, 0, 1.0F)
            .addEffect(effect("minecraft", "weakness"), 3000, 0, 1.0F)
            .addEffect(effect("minecraft", "slowness"), 3000, 0, 1.0F)
            .addEffect(effect("minecraft", "wither"), 1200, 0, 1.0F));
        KOMBUCHA = isTeaStoryLoaded() ? register("kombucha", tankardDrink()
            .addEffect(bncEffect("tipsy"), 2400, 0, 1.0F)
            .addEffect(bncEffect("intoxication"), 1800, 0, 1.0F)
            .addEffect(effect("minecraft", "haste"), 1200, 1, 1.0F)) : null;
        FLAXEN_CHEESE_WEDGE = register("flaxen_cheese_wedge", food(4, 1.0F));
        SCARLET_CHEESE_WEDGE = register("scarlet_cheese_wedge", food(4, 1.0F));
        VEGETABLE_OMELET = register("vegetable_omelet", bowlFood(12, 0.8F).addEffect(effect("farmersdelight", "nourishment"), 3600, 0, 1.0F));
        CREAMY_ONION_SOUP = register("creamy_onion_soup", bowlFood(12, 0.8F).addEffect(effect("farmersdelight", "comfort"), 3600, 0, 1.0F));
        CHEESY_PASTA = register("cheesy_pasta", bowlFood(14, 0.75F).addEffect(effect("farmersdelight", "nourishment"), 6000, 0, 1.0F));
        HORROR_LASAGNA = register("horror_lasagna", bowlFood(16, 0.55F).addEffect(effect("farmersdelight", "nourishment"), 6000, 0, 1.0F));
        SCARLET_PIEROGI = register("scarlet_pierogi", bowlFood(12, 1.0F).addEffect(effect("farmersdelight", "nourishment"), 8400, 0, 1.0F));
        FIERY_FONDUE = register("fiery_fondue", bowlFood(14, 0.75F).addEffect(effect("farmersdelight", "comfort"), 8400, 0, 1.0F));
        PIZZA_SLICE = register("pizza_slice", food(5, 1.0F));
        QUICHE_SLICE = register("quiche_slice", food(4, 0.8F));
        HAM_AND_CHEESE_SANDWICH = register("ham_and_cheese_sandwich", food(9, 1.0F));
        KIMCHI = register("kimchi", food(2, 0.6F));
        JERKY = register("jerky", food(3, 0.7F));
        PICKLED_PICKLES = register("pickled_pickles", food(4, 0.3F));
        KIPPERS = register("kippers", food(6, 0.5F));
        COCOA_FUDGE = register("cocoa_fudge", food(4, 0.8F).addEffect(effect("minecraft", "speed"), 800, 0, 1.0F));
        SWEET_BERRIES = Loader.isModLoaded("futuremc") ? null : register("sweet_berries", food(2, 0.1F));
        GLOW_BERRIES = hasExternalGlowBerries() ? null : register("glow_berries", food(2, 0.1F).addEffect(effect("minecraft", "night_vision"), 200, 0, 1.0F));
        SWEET_BERRY_JAM = register("sweet_berry_jam", bottleFood(6, 0.4F));
        GLOW_BERRY_MARMALADE = register("glow_berry_marmalade", bottleFood(6, 0.4F));
        APPLE_JELLY = register("apple_jelly", bottleFood(10, 0.6F));
        CRIMSON_FUNGUS = hasExternalFungus("crimson_fungus") ? null : register("crimson_fungus", plain());
        WARPED_FUNGUS = hasExternalFungus("warped_fungus") ? null : register("warped_fungus", plain());
        KELP = hasExternalOceanItem("kelp") ? null : register("kelp", plain());
        DRIED_KELP = hasExternalOceanItem("dried_kelp") ? null : register("dried_kelp", food(1, 0.3F));
        SEA_PICKLE = hasExternalOceanItem("sea_pickle") ? null : register("sea_pickle", plain());
        SEAGRASS = hasExternalSeagrass() ? null : register("seagrass", plain());
        ADULTERATED_HONEY = Loader.isModLoaded("futuremc") ? null : register("adulterated_honey", bottleFood(6, 0.1F));
        SYNTHETIC_BEESWAX = Loader.isModLoaded("futuremc") ? null : register("synthetic_beeswax", plain());
        GLOW_INK_SAC = hasRegisteredItem("oe", "glow_ink_sac") ? null : register("glow_ink_sac", plain());
        TURTLE_EGG = hasRegisteredItem("oe", "turtle_egg") ? null : register("turtle_egg", plain());
        WITHER_ROSE = hasExternalWitherRose() ? null : register("wither_rose", new BNCTooltipItem("brewinandchewinlegacy.tooltip.not_placeable"));

        registry.registerAll(ITEMS.toArray(new Item[0]));
        BNCOreDictionary.register();
    }

    public static void registerModels() {
        for (Item item : ITEMS) {
            ClientProxy.registerItemModel(item);
        }
    }

    private static Item register(String name, Item item) {
        item.setRegistryName(BrewinAndChewinLegacy.MODID, name);
        item.setUnlocalizedName(BrewinAndChewinLegacy.MODID + "." + name);
        item.setCreativeTab(BrewinAndChewinLegacy.CREATIVE_TAB);
        ITEMS.add(item);
        return item;
    }

    private static BNCFoodItem food(int amount, float saturation) {
        return new BNCFoodItem(amount, saturation, true);
    }

    private static BNCFoodItem bowlFood(int amount, float saturation) {
        BNCFoodItem item = new BNCFoodItem(amount, saturation, false, Items.BOWL, EnumAction.EAT);
        item.setMaxStackSize(16);
        return item;
    }

    private static BNCFoodItem bottleFood(int amount, float saturation) {
        BNCFoodItem item = new BNCFoodItem(amount, saturation, false, Items.GLASS_BOTTLE, EnumAction.EAT);
        item.setMaxStackSize(16);
        return item;
    }

    private static BNCFoodItem tankardDrink() {
        return new BNCBoozeItem(TANKARD);
    }

    private static Item plain() {
        return new Item();
    }

    private static ResourceLocation effect(String modid, String path) {
        return new ResourceLocation(modid, path);
    }

    private static ResourceLocation bncEffect(String path) {
        return effect(BrewinAndChewinLegacy.MODID, path);
    }

    private static boolean hasExternalFungus(String path) {
        return hasRegisteredItem("futuremc", path) || hasRegisteredItem("netherized", path) || hasRegisteredItem("nb", path);
    }

    private static boolean hasExternalGlowBerries() {
        return hasRegisteredItem("da", "glow_berry") || hasRegisteredItem("depthsupdate", "glow_berries");
    }

    private static boolean hasExternalOceanItem(String path) {
        return hasRegisteredItem("oe", path);
    }

    private static boolean hasExternalSeagrass() {
        return hasRegisteredItem("oe", "seagrass") || hasRegisteredItem("futuremc", "seagrass");
    }

    private static boolean hasExternalWitherRose() {
        for (Item item : ForgeRegistries.ITEMS) {
            ResourceLocation registryName = item.getRegistryName();
            if (registryName != null
                && "wither_rose".equals(registryName.getResourcePath())
                && !BrewinAndChewinLegacy.MODID.equals(registryName.getResourceDomain())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasRegisteredItem(String modid, String path) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, path)) != null;
    }

    public static boolean isTeaStoryLoaded() {
        return Loader.isModLoaded("teastory");
    }
}
