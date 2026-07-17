package xy177.brewinandchewinlegacy.common.config;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID)
public final class BNCConfig {
    private static final String CATEGORY = "keg_temperature";
    private static final String[] DEFAULT_HEAT_SOURCES = {
        "brewinandchewinlegacy:heating_cask",
        "minecraft:fire",
        "minecraft:lava",
        "minecraft:flowing_lava"
    };
    private static final String[] DEFAULT_COLD_SOURCES = {
        "brewinandchewinlegacy:ice_crate",
        "minecraft:ice",
        "minecraft:packed_ice",
        "minecraft:frosted_ice"
    };

    private static Configuration configuration;
    private static Set<ResourceLocation> heatSources = Collections.emptySet();
    private static Set<ResourceLocation> coldSources = Collections.emptySet();

    private BNCConfig() {
    }

    public static void init(File configFile) {
        configuration = new Configuration(configFile);
        sync();
    }

    public static boolean isHeatSource(IBlockState state) {
        return contains(heatSources, state);
    }

    public static boolean isColdSource(IBlockState state) {
        return contains(coldSources, state);
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (BrewinAndChewinLegacy.MODID.equals(event.getModID())) {
            sync();
        }
    }

    private static void sync() {
        if (configuration == null) {
            return;
        }

        configuration.load();
        configuration.setCategoryComment(CATEGORY,
            "Configure which blocks affect nearby keg temperature. Entries use modid:block_name registry names.");

        String[] configuredHeatSources = configuration.getStringList(
            "heatSources",
            CATEGORY,
            DEFAULT_HEAT_SOURCES,
            "Blocks counted as heat sources by kegs. One nearby source makes the keg warm; two or more make it hot."
        );
        String[] configuredColdSources = configuration.getStringList(
            "coldSources",
            CATEGORY,
            DEFAULT_COLD_SOURCES,
            "Blocks counted as cold sources by kegs. One nearby source makes the keg chilly; two or more make it cold."
        );

        heatSources = parseBlockIds(configuredHeatSources, "heatSources");
        coldSources = parseBlockIds(configuredColdSources, "coldSources");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static Set<ResourceLocation> parseBlockIds(String[] values, String property) {
        LinkedHashSet<ResourceLocation> parsed = new LinkedHashSet<>();
        if (values != null) {
            for (String value : values) {
                String trimmed = value == null ? "" : value.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                try {
                    parsed.add(new ResourceLocation(trimmed));
                } catch (RuntimeException exception) {
                    BrewinAndChewinLegacy.getLogger().warn(
                        "Ignoring invalid block id '{}' in config property '{}'.", trimmed, property);
                }
            }
        }
        return Collections.unmodifiableSet(parsed);
    }

    private static boolean contains(Set<ResourceLocation> sources, IBlockState state) {
        if (state == null || state.getBlock() == null || state.getBlock().getRegistryName() == null) {
            return false;
        }
        return sources.contains(state.getBlock().getRegistryName());
    }
}
