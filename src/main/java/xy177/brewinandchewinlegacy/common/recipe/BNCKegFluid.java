package xy177.brewinandchewinlegacy.common.recipe;

public final class BNCKegFluid {
    public static final int CAPACITY = 1000;

    public static final String EMPTY = "";
    public static final String WATER = "water";
    public static final String MILK = "milk";
    public static final String HONEY = "honey";
    public static final String BEER = "beer";
    public static final String VODKA = "vodka";
    public static final String MEAD = "mead";
    public static final String RICE_WINE = "rice_wine";
    public static final String EGG_GROG = "egg_grog";
    public static final String STRONGROOT_ALE = "strongroot_ale";
    public static final String SACCHARINE_RUM = "saccharine_rum";
    public static final String BLOODY_MARY = "bloody_mary";
    public static final String STEEL_TOE_STOUT = "steel_toe_stout";
    public static final String GLITTERING_GRENADINE = "glittering_grenadine";
    public static final String PALE_JANE = "pale_jane";
    public static final String SALTY_FOLLY = "salty_folly";
    public static final String DREAD_NOG = "dread_nog";
    public static final String RED_RUM = "red_rum";
    public static final String WITHERING_DROSS = "withering_dross";
    public static final String KOMBUCHA = "kombucha";
    public static final String GREEN_TEA = "green_tea";
    public static final String FLAXEN_CHEESE = "flaxen_cheese";
    public static final String SCARLET_CHEESE = "scarlet_cheese";

    private BNCKegFluid() {
    }

    public static int toCode(String fluidId) {
        if (WATER.equals(fluidId)) {
            return 1;
        }
        if (MILK.equals(fluidId)) {
            return 2;
        }
        if (HONEY.equals(fluidId)) {
            return 3;
        }
        if (BEER.equals(fluidId)) {
            return 4;
        }
        if (VODKA.equals(fluidId)) {
            return 5;
        }
        if (MEAD.equals(fluidId)) {
            return 6;
        }
        if (RICE_WINE.equals(fluidId)) {
            return 7;
        }
        if (EGG_GROG.equals(fluidId)) {
            return 8;
        }
        if (STRONGROOT_ALE.equals(fluidId)) {
            return 9;
        }
        if (SACCHARINE_RUM.equals(fluidId)) {
            return 10;
        }
        if (BLOODY_MARY.equals(fluidId)) {
            return 11;
        }
        if (STEEL_TOE_STOUT.equals(fluidId)) {
            return 12;
        }
        if (FLAXEN_CHEESE.equals(fluidId)) {
            return 13;
        }
        if (SCARLET_CHEESE.equals(fluidId)) {
            return 14;
        }
        if (GLITTERING_GRENADINE.equals(fluidId)) {
            return 15;
        }
        if (PALE_JANE.equals(fluidId)) {
            return 16;
        }
        if (SALTY_FOLLY.equals(fluidId)) {
            return 17;
        }
        if (DREAD_NOG.equals(fluidId)) {
            return 18;
        }
        if (RED_RUM.equals(fluidId)) {
            return 19;
        }
        if (WITHERING_DROSS.equals(fluidId)) {
            return 20;
        }
        if (KOMBUCHA.equals(fluidId)) {
            return 21;
        }
        if (GREEN_TEA.equals(fluidId)) {
            return 22;
        }
        return 0;
    }

    public static String fromCode(int code) {
        switch (code) {
            case 1:
                return WATER;
            case 2:
                return MILK;
            case 3:
                return HONEY;
            case 4:
                return BEER;
            case 5:
                return VODKA;
            case 6:
                return MEAD;
            case 7:
                return RICE_WINE;
            case 8:
                return EGG_GROG;
            case 9:
                return STRONGROOT_ALE;
            case 10:
                return SACCHARINE_RUM;
            case 11:
                return BLOODY_MARY;
            case 12:
                return STEEL_TOE_STOUT;
            case 13:
                return FLAXEN_CHEESE;
            case 14:
                return SCARLET_CHEESE;
            case 15:
                return GLITTERING_GRENADINE;
            case 16:
                return PALE_JANE;
            case 17:
                return SALTY_FOLLY;
            case 18:
                return DREAD_NOG;
            case 19:
                return RED_RUM;
            case 20:
                return WITHERING_DROSS;
            case 21:
                return KOMBUCHA;
            case 22:
                return GREEN_TEA;
            default:
                return EMPTY;
        }
    }

    public static boolean isDrink(String fluidId) {
        return BEER.equals(fluidId)
            || VODKA.equals(fluidId)
            || MEAD.equals(fluidId)
            || RICE_WINE.equals(fluidId)
            || EGG_GROG.equals(fluidId)
            || STRONGROOT_ALE.equals(fluidId)
            || SACCHARINE_RUM.equals(fluidId)
            || BLOODY_MARY.equals(fluidId)
            || STEEL_TOE_STOUT.equals(fluidId)
            || GLITTERING_GRENADINE.equals(fluidId)
            || PALE_JANE.equals(fluidId)
            || SALTY_FOLLY.equals(fluidId)
            || DREAD_NOG.equals(fluidId)
            || RED_RUM.equals(fluidId)
            || WITHERING_DROSS.equals(fluidId)
            || KOMBUCHA.equals(fluidId);
    }

    public static boolean isCheese(String fluidId) {
        return FLAXEN_CHEESE.equals(fluidId) || SCARLET_CHEESE.equals(fluidId);
    }
}
