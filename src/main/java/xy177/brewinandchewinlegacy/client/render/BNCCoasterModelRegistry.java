package xy177.brewinandchewinlegacy.client.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public final class BNCCoasterModelRegistry {
    private static final Map<String, List<IBakedModel>> MODEL_CACHE = new HashMap<>();
    private static final Map<String, IBakedModel> BLOCK_MODEL_CACHE = new HashMap<>();
    private static final Set<String> MISSING_MODELS = new HashSet<>();

    private static final String[] STITCHED_TEXTURES = {
        "apple", "apple_cider", "apple_jelly", "baked_cod_stew", "beef_stew", "beer",
        "beetroot", "beetroot_soup", "bloody_mary", "bone_broth", "bowl", "bread",
        "cabbage", "carrot", "chicken_soup", "chorus_fruit", "coaster_bottom",
        "coaster_side", "coaster_top", "cod", "cod_roll", "cooked_beef", "cooked_chicken",
        "cooked_cod", "cooked_mutton", "cooked_porkchop", "cooked_rabbit", "cooked_rice",
        "cooked_salmon", "cookie", "creamy_onion_soup", "dog_food", "dread_nog", "egg",
        "egg_grog", "fiery_fondue", "fish_stew", "flaxen_cheese_wedge", "fried_egg",
        "fried_rice", "fruit_salad", "glistering_melon_slice", "glittering_grenadine",
        "glow_berries", "glow_berry_custard", "glow_berry_marmalade", "golden_apple",
        "golden_carrot", "honey_bottle", "honey_cookie", "hot_cocoa", "kelp_roll",
        "kelp_roll_slice", "kombucha", "mead", "melon_juice", "melon_popsicle",
        "melon_slice", "milk_bottle", "mixed_salad", "mushroom_stew", "nether_salad",
        "noodle_soup", "onion", "pale_jane", "poisonous_potato", "potato", "potion_bottle",
        "potion_contents", "pufferfish", "pumpkin_pie", "pumpkin_slice", "pumpkin_soup",
        "rabbit_stew", "raw_beef", "raw_chicken", "raw_mutton", "raw_porkchop", "raw_rabbit",
        "red_rum", "rice_wine", "rotten_tomato", "saccharine_rum", "salmon", "salmon_roll",
        "salty_folly", "scarlet_cheese_wedge", "steel_toe_stout", "strongroot_ale",
        "stuffed_pumpkin", "suspicious_stew", "sweet_berries", "sweet_berry_cookie",
        "sweet_berry_jam", "tankard", "tomato", "tomato_sauce", "tropical_fish",
        "vegetable_soup", "vodka", "withering_dross"
    };

    private BNCCoasterModelRegistry() {
    }

    public static List<ResourceLocation> spriteLocations() {
        List<ResourceLocation> sprites = new ArrayList<>();
        for (String texture : STITCHED_TEXTURES) {
            sprites.add(new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/" + texture));
        }
        return sprites;
    }

    public static List<IBakedModel> getModels(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem().getRegistryName() == null) {
            return Collections.emptyList();
        }

        String key = stackKey(stack);
        if (MODEL_CACHE.containsKey(key)) {
            return MODEL_CACHE.get(key);
        }

        List<IBakedModel> models = new ArrayList<>();
        for (String modelName : modelNames(stack)) {
            IBakedModel model = bakeModel(modelName);
            if (model != null) {
                models.add(model);
            }
        }

        List<IBakedModel> immutable = models.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(models);
        MODEL_CACHE.put(key, immutable);
        return immutable;
    }

    public static IBakedModel getBlockModel(String modelName) {
        if (BLOCK_MODEL_CACHE.containsKey(modelName)) {
            return BLOCK_MODEL_CACHE.get(modelName);
        }

        IBakedModel model = bakeModel(modelName);
        if (model != null) {
            BLOCK_MODEL_CACHE.put(modelName, model);
        }
        return model;
    }

    public static void clearCache() {
        MODEL_CACHE.clear();
        BLOCK_MODEL_CACHE.clear();
        MISSING_MODELS.clear();
    }

    private static List<String> modelNames(ItemStack stack) {
        if (stack.getItem() == Items.POTIONITEM || stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION) {
            List<String> models = new ArrayList<>();
            models.add("coaster_potion_bottle");
            models.add("coaster_potion_contents");
            return models;
        }
        if (stack.getItem() == Items.GLASS_BOTTLE) {
            return Collections.singletonList("coaster_potion_bottle");
        }
        if (stack.getItem() == Items.FISH) {
            switch (stack.getMetadata()) {
                case 1:
                    return Collections.singletonList("coaster_salmon");
                case 2:
                    return Collections.singletonList("coaster_tropical_fish");
                case 3:
                    return Collections.singletonList("coaster_pufferfish");
                default:
                    return Collections.singletonList("coaster_cod");
            }
        }
        if (stack.getItem() == Items.COOKED_FISH) {
            return Collections.singletonList(stack.getMetadata() == 1 ? "coaster_cooked_salmon" : "coaster_cooked_cod");
        }
        if (stack.getItem() == Items.GOLDEN_APPLE) {
            return Collections.singletonList("coaster_golden_apple");
        }

        ResourceLocation itemId = stack.getItem().getRegistryName();
        if (!isSupportedDomain(itemId.getResourceDomain())) {
            return Collections.emptyList();
        }
        return Collections.singletonList("coaster_" + itemId.getResourcePath());
    }

    private static boolean isSupportedDomain(String namespace) {
        return "minecraft".equals(namespace)
            || "farmersdelight".equals(namespace)
            || BrewinAndChewinLegacy.MODID.equals(namespace)
            || "futuremc".equals(namespace);
    }

    private static String stackKey(ItemStack stack) {
        ResourceLocation id = stack.getItem().getRegistryName();
        String path = id == null ? "unknown" : id.toString();
        if (stack.getItem() == Items.FISH || stack.getItem() == Items.COOKED_FISH || stack.getItem() == Items.GOLDEN_APPLE) {
            return path + ":" + stack.getMetadata();
        }
        return path;
    }

    private static IBakedModel bakeModel(String modelName) {
        if (MISSING_MODELS.contains(modelName)) {
            return null;
        }

        ResourceLocation location = new ResourceLocation(BrewinAndChewinLegacy.MODID, "block/" + modelName);
        try {
            return ModelLoaderRegistry.getModel(location).bake(
                TRSRTransformation.identity(),
                DefaultVertexFormats.ITEM,
                sprite -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(sprite.toString())
            );
        } catch (Exception ex) {
            MISSING_MODELS.add(modelName);
            Logger logger = BrewinAndChewinLegacy.getLogger();
            if (logger != null) {
                logger.warn("Failed to bake coaster display model {}.", location, ex);
            }
            return null;
        }
    }
}
