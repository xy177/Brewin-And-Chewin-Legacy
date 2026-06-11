package xy177.brewinandchewinlegacy.common.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.IForgeRegistry;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.enchantment.BNCAlgaeClearingEnchantment;

public final class BNCEnchantments {
    public static Enchantment ALGAE_CLEARING;

    private BNCEnchantments() {
    }

    public static void register(IForgeRegistry<Enchantment> registry) {
        ALGAE_CLEARING = new BNCAlgaeClearingEnchantment();
        ALGAE_CLEARING.setRegistryName(BrewinAndChewinLegacy.MODID, "algae_clearing");
        registry.register(ALGAE_CLEARING);
    }
}
