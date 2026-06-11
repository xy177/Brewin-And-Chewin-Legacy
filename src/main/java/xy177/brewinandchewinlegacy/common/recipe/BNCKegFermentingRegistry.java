package xy177.brewinandchewinlegacy.common.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

public final class BNCKegFermentingRegistry {
    private static final int FAST_FERMENTING = 4800;
    private static final int NORMAL_FERMENTING = 9600;
    private static final float MEDIUM_EXP = 1.0F;
    private static final List<BNCKegFermentingRecipe> RECIPES = new ArrayList<>();

    private BNCKegFermentingRegistry() {
    }

    public static void registerDefaults() {
        if (!RECIPES.isEmpty()) {
            return;
        }

        register("jerky", new ItemStack(BNCItems.JERKY, 3), NORMAL_FERMENTING, 4,
            "ore:bncJerkyMeat",
            "ore:bncJerkyMeat",
            "ore:bncJerkyMeat");

        register("kimchi", new ItemStack(BNCItems.KIMCHI, 2), NORMAL_FERMENTING, 4,
            "ore:cropCabbage",
            "ore:listAllveggie",
            "ore:bncKelp");

        register("kippers", new ItemStack(BNCItems.KIPPERS, 2), NORMAL_FERMENTING, 4,
            "ore:listAllfishraw",
            "ore:listAllfishraw",
            "ore:bncDriedKelp");

        registerFluidResult("beer", BNCKegFluid.BEER, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 3,
            "minecraft:wheat",
            "minecraft:wheat_seeds",
            "minecraft:brown_mushroom");

        registerFluidResult("vodka", BNCKegFluid.VODKA, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 3,
            "ore:cropPotato",
            "minecraft:wheat",
            "minecraft:wheat_seeds");

        registerFluidResult("mead", BNCKegFluid.MEAD, 1000, BNCKegFluid.HONEY, 1000, NORMAL_FERMENTING, 3,
            "minecraft:wheat",
            "minecraft:wheat_seeds",
            "ore:bncSweetBerries");

        registerFluidResult("rice_wine", BNCKegFluid.RICE_WINE, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 3,
            "ore:cropRice",
            "minecraft:brown_mushroom");

        registerFluidResult("egg_grog", BNCKegFluid.EGG_GROG, 1000, BNCKegFluid.MILK, 1000, NORMAL_FERMENTING, 3,
            "ore:listAllEgg",
            "ore:cropCabbage",
            "ore:bncSugar");

        registerFluidResult("strongroot_ale", BNCKegFluid.STRONGROOT_ALE, 1000, BNCKegFluid.BEER, 1000, FAST_FERMENTING, 3,
            "ore:cropBeetroot",
            "ore:cropPotato",
            "minecraft:brown_mushroom",
            "brewinandchewinlegacy:jerky");

        registerFluidResult("saccharine_rum", BNCKegFluid.SACCHARINE_RUM, 1000, BNCKegFluid.MEAD, 1000, FAST_FERMENTING, 4,
            "ore:bncSweetBerries",
            "minecraft:reeds",
            "minecraft:melon");

        registerFluidResult("bloody_mary", BNCKegFluid.BLOODY_MARY, 1000, BNCKegFluid.VODKA, 1000, FAST_FERMENTING, 4,
            "ore:cropTomato",
            "ore:cropCabbage",
            "ore:bncSweetBerries");

        registerFluidResult("steel_toe_stout", BNCKegFluid.STEEL_TOE_STOUT, 1000, BNCKegFluid.STRONGROOT_ALE, 1000, FAST_FERMENTING, 1,
            "minecraft:iron_ingot",
            "ore:bncCrimsonFungus",
            "minecraft:nether_wart",
            "minecraft:wheat");

        registerFluidResult("glittering_grenadine", BNCKegFluid.GLITTERING_GRENADINE, 1000, BNCKegFluid.WATER, 1000, NORMAL_FERMENTING, 2,
            "ore:bncGlowBerries",
            "minecraft:glowstone_dust",
            "ore:bncGlowInkSac");

        registerFluidResult("pale_jane", BNCKegFluid.PALE_JANE, 1000, BNCKegFluid.RICE_WINE, 1000, FAST_FERMENTING, 4,
            "ore:bncHoneyItem",
            "farmersdelight:tree_bark",
            "ore:bncPaleFlower",
            "ore:bncSugar");

        registerFluidResult("salty_folly", BNCKegFluid.SALTY_FOLLY, 1000, BNCKegFluid.VODKA, 1000, FAST_FERMENTING, 2,
            "ore:bncSeaPickle",
            "ore:bncDriedKelp",
            "ore:bncSeagrass");

        registerFluidResult("dread_nog", BNCKegFluid.DREAD_NOG, 1000, BNCKegFluid.EGG_GROG, 1000, FAST_FERMENTING, 1,
            ominousBottleIngredient(),
            "ore:bncTurtleEgg",
            "minecraft:fermented_spider_eye");

        registerFluidResult("red_rum", BNCKegFluid.RED_RUM, 1000, BNCKegFluid.BLOODY_MARY, 1000, FAST_FERMENTING, 5,
            "ore:bncCrimsonFungus",
            "minecraft:nether_wart",
            "minecraft:fermented_spider_eye",
            "ore:bncShroomlight");

        registerFluidResult("withering_dross", BNCKegFluid.WITHERING_DROSS, 1000, BNCKegFluid.SALTY_FOLLY, 1000, NORMAL_FERMENTING, 5,
            "ore:bncWitherRose",
            "minecraft:dye:0",
            "minecraft:nether_wart",
            "minecraft:bone");

        if (BNCItems.KOMBUCHA != null) {
            registerFluidResult("kombucha", BNCKegFluid.KOMBUCHA, 1000, BNCKegFluid.GREEN_TEA, 1000, NORMAL_FERMENTING, 3,
                "ore:cropCarrot",
                "ore:bncSweetBerries",
                "teastory:black_tea_leaf");
        }

        registerFluidResult("flaxen_cheese", BNCKegFluid.FLAXEN_CHEESE, 1000, BNCKegFluid.MILK, 1000, NORMAL_FERMENTING, 4,
            "minecraft:brown_mushroom",
            "minecraft:pumpkin_seeds",
            "ore:bncSugar");

        registerFluidResult("scarlet_cheese", BNCKegFluid.SCARLET_CHEESE, 1000, BNCKegFluid.MILK, 1000, NORMAL_FERMENTING, 5,
            "ore:bncCrimsonFungus",
            "minecraft:nether_wart",
            "ore:bncSugar");

        registerWithFluid("pickled_pickles", new ItemStack(BNCItems.PICKLED_PICKLES, 2), BNCKegFluid.HONEY, 250, NORMAL_FERMENTING, 2,
            "ore:bncSeaPickle",
            "ore:bncSeaPickle",
            "ore:bncGlowBerries");

        registerWithFluid("cocoa_fudge", new ItemStack(BNCItems.COCOA_FUDGE), BNCKegFluid.MILK, 500, NORMAL_FERMENTING, 2,
            "ore:bncSugar",
            "minecraft:dye:3",
            "minecraft:dye:3");
    }

    public static List<BNCKegFermentingRecipe> getRecipes() {
        return Collections.unmodifiableList(RECIPES);
    }

    public static BNCKegFermentingRecipe findMatch(ItemStack[] inputs) {
        for (BNCKegFermentingRecipe recipe : RECIPES) {
            if (recipe.matches(inputs)) {
                return recipe;
            }
        }
        return null;
    }

    private static void register(String path, ItemStack result, int fermentTime, int temperature, String... ingredients) {
        RECIPES.add(new BNCKegFermentingRecipe(new ResourceLocation(BrewinAndChewinLegacy.MODID, path), result, fermentTime, temperature, ingredients));
    }

    private static void registerWithFluid(String path, ItemStack result, String baseFluid, int baseFluidAmount, int fermentTime, int temperature, String... ingredients) {
        RECIPES.add(new BNCKegFermentingRecipe(new ResourceLocation(BrewinAndChewinLegacy.MODID, path), result, baseFluid, baseFluidAmount, MEDIUM_EXP, fermentTime, temperature, ingredients));
    }

    private static void registerFluidResult(String path, String resultFluid, int resultFluidAmount, String baseFluid, int baseFluidAmount, int fermentTime, int temperature, String... ingredients) {
        RECIPES.add(new BNCKegFermentingRecipe(new ResourceLocation(BrewinAndChewinLegacy.MODID, path), resultFluid, resultFluidAmount, baseFluid, baseFluidAmount, MEDIUM_EXP, fermentTime, temperature, ingredients));
    }

    private static String ominousBottleIngredient() {
        return OreDictionary.getOres("bncOminousBottle").isEmpty() ? "potion:poison" : "ore:bncOminousBottle";
    }
}
