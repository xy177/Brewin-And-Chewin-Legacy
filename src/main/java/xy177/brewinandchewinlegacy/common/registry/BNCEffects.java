package xy177.brewinandchewinlegacy.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.effect.BNCPotion;

public final class BNCEffects {
    private static final List<Potion> POTIONS = new ArrayList<>();

    public static Potion INTOXICATION;
    public static Potion SWEET_HEART;
    public static Potion RAGING;
    public static Potion TIPSY;

    private BNCEffects() {
    }

    public static void register(IForgeRegistry<Potion> registry) {
        POTIONS.clear();
        INTOXICATION = register("intoxication", new BNCPotion(true, 0xDDC942, "intoxication", false));
        SWEET_HEART = register("sweet_heart", new BNCPotion.SweetHeart());
        RAGING = register("raging", new BNCPotion(false, 0x6A05C1, "raging", true));
        TIPSY = register("tipsy", new BNCPotion(false, 0xC9828E, "tipsy", false));
        registry.registerAll(POTIONS.toArray(new Potion[0]));
    }

    private static Potion register(String name, Potion potion) {
        potion.setRegistryName(BrewinAndChewinLegacy.MODID, name);
        POTIONS.add(potion);
        return potion;
    }
}
