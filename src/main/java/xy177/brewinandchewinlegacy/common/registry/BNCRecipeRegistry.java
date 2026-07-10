package xy177.brewinandchewinlegacy.common.registry;

import com.wdcftgg.farmersdelightlegacy.api.recipe.CookingPotRecipeApi;
import com.wdcftgg.farmersdelightlegacy.api.recipe.CuttingBoardRecipeApi;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFermentingRegistry;

public final class BNCRecipeRegistry {
    private static boolean registered;

    private BNCRecipeRegistry() {
    }

    public static void registerRuntimeRecipes() {
        if (registered) {
            return;
        }
        registered = true;

        BNCOreDictionary.registerBerryCompatibility();
        logBerryCompatibility();
        registerBerryCookingPotRecipes();
        registerGlowInkSacCookingPotRecipe();
        registerHoneyFallbackCookingPotRecipes();
        registerDriedKelpSmelting();
        registerFeastCuttingBoardRecipes();
        registerWitherRoseCuttingBoardRecipe();
        BNCKegFermentingRegistry.registerDefaults();
    }

    public static void registerCraftingRecipes(IForgeRegistry<IRecipe> registry) {
        registerFallbackFungusRecipes(registry);
        registerHoneyFallbackCraftingRecipes(registry);
    }

    private static void registerHoneyFallbackCookingPotRecipes() {
        if (BNCItems.ADULTERATED_HONEY == null) {
            return;
        }
        registerCookingPot(
            id("adulterated_honey"),
            new String[] {
                "ore:bncSugar",
                "ore:bncSugar",
                "minecraft:dye@11"
            },
            new ItemStack(BNCItems.ADULTERATED_HONEY),
            new ItemStack(Items.GLASS_BOTTLE),
            200,
            1.0F
        );
    }

    private static void registerGlowInkSacCookingPotRecipe() {
        if (BNCItems.GLOW_INK_SAC == null) {
            return;
        }
        registerCookingPot(
            id("glow_ink_sac"),
            new String[] {
                "minecraft:dye@0"
            },
            new ItemStack(BNCItems.GLOW_INK_SAC),
            new ItemStack(Items.GLOWSTONE_DUST),
            200,
            1.0F
        );
    }

    private static void registerBerryCookingPotRecipes() {
        registerCookingPot(
            id("sweet_berry_jam"),
            new String[] {
                "ore:bncSweetBerries",
                "ore:bncSweetBerries",
                "ore:bncSweetBerries",
                "ore:bncSugar"
            },
            new ItemStack(BNCItems.SWEET_BERRY_JAM),
            new ItemStack(Items.GLASS_BOTTLE),
            200,
            1.0F
        );

        registerCookingPot(
            id("glow_berry_marmalade"),
            new String[] {
                "ore:bncGlowBerries",
                "ore:bncGlowBerries",
                "ore:bncGlowBerries",
                "ore:bncSugar"
            },
            new ItemStack(BNCItems.GLOW_BERRY_MARMALADE),
            new ItemStack(Items.GLASS_BOTTLE),
            200,
            1.0F
        );

        if (!BNCOreDictionary.hasExternalGlowBerries() && BNCItems.GLOW_BERRIES != null) {
            registerCookingPot(
                id("glow_berries"),
                new String[] {
                    "ore:bncSweetBerries"
                },
                new ItemStack(BNCItems.GLOW_BERRIES),
                new ItemStack(Items.GLOWSTONE_DUST),
                200,
                1.0F
            );
        }
    }

    private static void registerCookingPot(String recipeId, String[] inputs, ItemStack result, ItemStack container, int cookingTime, float experience) {
        if (!CookingPotRecipeApi.registerRecipe(recipeId, inputs, result, container, cookingTime, experience)) {
            BrewinAndChewinLegacy.getLogger().warn("Failed to register cooking pot recipe {}.", recipeId);
        }
    }

    private static void registerFeastCuttingBoardRecipes() {
        registerCuttingBoardRecipe("flaxen_cheese_wheel", BNCBlocks.FLAXEN_CHEESE_WHEEL_ITEM, BNCItems.FLAXEN_CHEESE_WEDGE);
        registerCuttingBoardRecipe("scarlet_cheese_wheel", BNCBlocks.SCARLET_CHEESE_WHEEL_ITEM, BNCItems.SCARLET_CHEESE_WEDGE);
        registerCuttingBoardRecipe("quiche", BNCBlocks.QUICHE_ITEM, BNCItems.QUICHE_SLICE);
        registerCuttingBoardRecipe("pizza", BNCBlocks.PIZZA_ITEM, BNCItems.PIZZA_SLICE);
    }

    private static void registerCuttingBoardRecipe(String path, Item input, Item result) {
        if (!CuttingBoardRecipeApi.registerRecipe(
            id(path),
            new ItemStack(input),
            null,
            new ItemStack(result, 4),
            1.0F
        )) {
            BrewinAndChewinLegacy.getLogger().warn("Failed to register cutting board recipe {}.", id(path));
        }
    }

    private static void registerWitherRoseCuttingBoardRecipe() {
        if (BNCItems.WITHER_ROSE == null) {
            return;
        }
        if (!CuttingBoardRecipeApi.registerRecipe(
            id("wither_rose"),
            new ItemStack(Blocks.RED_FLOWER, 1, 0),
            new ItemStack(Items.NETHER_STAR),
            new ItemStack(BNCItems.WITHER_ROSE),
            1.0F
        )) {
            BrewinAndChewinLegacy.getLogger().warn("Failed to register cutting board recipe {}.", id("wither_rose"));
        }
    }

    private static void logBerryCompatibility() {
        BrewinAndChewinLegacy.getLogger().info("Using {} for sweet berry recipes.", itemName(BNCOreDictionary.getSweetBerriesItem()));
        BrewinAndChewinLegacy.getLogger().info("Using {} for glow berry recipes.", itemName(BNCOreDictionary.getGlowBerriesItem()));
        if (!BNCOreDictionary.hasExternalGlowBerries()) {
            BrewinAndChewinLegacy.getLogger().info("No compatible external glow berries are available; local glow berries cooking-pot fallback is enabled.");
        }
    }

    private static String itemName(Item item) {
        return item == null || item.getRegistryName() == null ? "none" : item.getRegistryName().toString();
    }

    private static String id(String path) {
        return BrewinAndChewinLegacy.MODID + ":" + path;
    }

    private static void registerFallbackFungusRecipes(IForgeRegistry<IRecipe> registry) {
        if (BNCItems.CRIMSON_FUNGUS != null) {
            registerShapeless(registry, "crimson_fungus", new ItemStack(BNCItems.CRIMSON_FUNGUS), new ItemStack(Blocks.RED_MUSHROOM), Items.NETHER_WART);
        }
        if (BNCItems.WARPED_FUNGUS != null) {
            registerShapeless(registry, "warped_fungus", new ItemStack(BNCItems.WARPED_FUNGUS), new ItemStack(Blocks.BROWN_MUSHROOM), Items.NETHER_WART);
        }
    }

    private static void registerHoneyFallbackCraftingRecipes(IForgeRegistry<IRecipe> registry) {
        if (BNCItems.SYNTHETIC_BEESWAX != null) {
            registerShapeless(registry, "synthetic_beeswax", new ItemStack(BNCItems.SYNTHETIC_BEESWAX), new ItemStack(Items.DYE, 1, 11), Items.SLIME_BALL);
        }
    }

    private static void registerDriedKelpSmelting() {
        Item kelp = BNCOreDictionary.getKelpItem();
        if (BNCItems.DRIED_KELP != null && kelp != null) {
            GameRegistry.addSmelting(new ItemStack(kelp), new ItemStack(BNCItems.DRIED_KELP), 0.1F);
        }
    }

    private static void registerShapeless(IForgeRegistry<IRecipe> registry, String path, ItemStack output, Object... inputs) {
        ResourceLocation recipeId = new ResourceLocation(BrewinAndChewinLegacy.MODID, path);
        ShapelessOreRecipe recipe = new ShapelessOreRecipe(null, output, inputs);
        recipe.setRegistryName(recipeId);
        registry.register(recipe);
    }
}
