package xy177.brewinandchewinlegacy.common.registry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFluid;

public final class BNCFluids {
    public static final ResourceLocation WATER_STILL = new ResourceLocation("minecraft", "blocks/water_still");
    public static final ResourceLocation WATER_FLOW = new ResourceLocation("minecraft", "blocks/water_flow");
    public static final ResourceLocation ALCOHOL_WATER_STILL = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/alcohol_water_still");
    public static final ResourceLocation ALCOHOL_WATER_FLOW = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/alcohol_water_flow");
    public static final ResourceLocation MILK_STILL = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/milk_still");
    public static final ResourceLocation MILK_FLOW = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/milk_flowing");
    public static final ResourceLocation HONEY_STILL = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/honey_block_top");
    public static final ResourceLocation HONEY_FLOW = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/honey_block_top");
    public static final ResourceLocation FLAXEN_CHEESE_STILL = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/flaxen_cheese_still");
    public static final ResourceLocation FLAXEN_CHEESE_FLOW = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/flaxen_cheese_flow");
    public static final ResourceLocation SCARLET_CHEESE_STILL = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/scarlet_cheese_still");
    public static final ResourceLocation SCARLET_CHEESE_FLOW = new ResourceLocation(BrewinAndChewinLegacy.MODID, "blocks/scarlet_cheese_flow");

    public static Fluid MILK;
    public static Fluid HONEY;
    public static Fluid BEER;
    public static Fluid VODKA;
    public static Fluid MEAD;
    public static Fluid RICE_WINE;
    public static Fluid EGG_GROG;
    public static Fluid STRONGROOT_ALE;
    public static Fluid SACCHARINE_RUM;
    public static Fluid BLOODY_MARY;
    public static Fluid STEEL_TOE_STOUT;
    public static Fluid GLITTERING_GRENADINE;
    public static Fluid PALE_JANE;
    public static Fluid SALTY_FOLLY;
    public static Fluid DREAD_NOG;
    public static Fluid RED_RUM;
    public static Fluid WITHERING_DROSS;
    public static Fluid KOMBUCHA;
    public static Fluid GREEN_TEA;
    public static Fluid FLAXEN_CHEESE;
    public static Fluid SCARLET_CHEESE;

    private BNCFluids() {
    }

    public static void register() {
        MILK = registerOrGet("milk", "milk", MILK_STILL, MILK_FLOW, 0xFFFFFFFF, 1100, 305, 1300);
        HONEY = registerOrGet(BrewinAndChewinLegacy.MODID + "_honey", "honey", HONEY_STILL, HONEY_FLOW, 0xFFFFFFFF, 1400, 315, 2400);
        BEER = registerOrGet(BrewinAndChewinLegacy.MODID + "_beer", "beer", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFFBB117, 1000, 300, 1200);
        VODKA = registerOrGet(BrewinAndChewinLegacy.MODID + "_vodka", "vodka", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFE7FDF6, 1000, 300, 1000);
        MEAD = registerOrGet(BrewinAndChewinLegacy.MODID + "_mead", "mead", HONEY_STILL, HONEY_FLOW, 0xFFFFD32D, 1100, 300, 1300);
        RICE_WINE = registerOrGet(BrewinAndChewinLegacy.MODID + "_rice_wine", "rice_wine", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFFFFFFF, 1000, 300, 1100);
        EGG_GROG = registerOrGet(BrewinAndChewinLegacy.MODID + "_egg_grog", "egg_grog", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFFFFFFF, 1050, 300, 1400);
        STRONGROOT_ALE = registerOrGet(BrewinAndChewinLegacy.MODID + "_strongroot_ale", "strongroot_ale", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFBC4A4F, 1000, 300, 1200);
        SACCHARINE_RUM = registerOrGet(BrewinAndChewinLegacy.MODID + "_saccharine_rum", "saccharine_rum", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFCD4A7A, 1000, 300, 1200);
        BLOODY_MARY = registerOrGet(BrewinAndChewinLegacy.MODID + "_bloody_mary", "bloody_mary", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF84160D, 1000, 300, 1200);
        STEEL_TOE_STOUT = registerOrGet(BrewinAndChewinLegacy.MODID + "_steel_toe_stout", "steel_toe_stout", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF978B8C, 1000, 300, 1200);
        GLITTERING_GRENADINE = registerOrGet(BrewinAndChewinLegacy.MODID + "_glittering_grenadine", "glittering_grenadine", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFF5A55E, 1000, 300, 1200);
        PALE_JANE = registerOrGet(BrewinAndChewinLegacy.MODID + "_pale_jane", "pale_jane", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFFD8BEAB, 1000, 300, 1200);
        SALTY_FOLLY = registerOrGet(BrewinAndChewinLegacy.MODID + "_salty_folly", "salty_folly", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF38672D, 1000, 300, 1200);
        DREAD_NOG = registerOrGet(BrewinAndChewinLegacy.MODID + "_dread_nog", "dread_nog", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF25DAB7, 1000, 300, 1200);
        RED_RUM = registerOrGet(BrewinAndChewinLegacy.MODID + "_red_rum", "red_rum", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF521810, 1000, 300, 1200);
        WITHERING_DROSS = registerOrGet(BrewinAndChewinLegacy.MODID + "_withering_dross", "withering_dross", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF191411, 1000, 300, 1200);
        if (BNCItems.isTeaStoryLoaded()) {
            GREEN_TEA = registerOrGet(BrewinAndChewinLegacy.MODID + "_green_tea", "green_tea", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF99C75A, 1000, 300, 1200);
            KOMBUCHA = registerOrGet(BrewinAndChewinLegacy.MODID + "_kombucha", "kombucha", ALCOHOL_WATER_STILL, ALCOHOL_WATER_FLOW, 0xFF929238, 1000, 300, 1200);
        }
        FLAXEN_CHEESE = registerOrGet(BrewinAndChewinLegacy.MODID + "_flaxen_cheese", "flaxen_cheese", FLAXEN_CHEESE_STILL, FLAXEN_CHEESE_FLOW, 0xFFFFFFFF, 1300, 315, 2200);
        SCARLET_CHEESE = registerOrGet(BrewinAndChewinLegacy.MODID + "_scarlet_cheese", "scarlet_cheese", SCARLET_CHEESE_STILL, SCARLET_CHEESE_FLOW, 0xFFFFFFFF, 1300, 320, 2200);
    }

    public static FluidStack stackFor(String fluidId, int amount) {
        Fluid fluid = fluidFor(fluidId);
        return fluid == null || amount <= 0 ? null : new FluidStack(fluid, amount);
    }

    public static String idFor(Fluid fluid) {
        if (fluid == null) {
            return BNCKegFluid.EMPTY;
        }
        if (fluid == FluidRegistry.WATER) {
            return BNCKegFluid.WATER;
        }
        if (fluid == MILK) {
            return BNCKegFluid.MILK;
        }
        if (fluid == HONEY) {
            return BNCKegFluid.HONEY;
        }
        if (fluid == BEER) {
            return BNCKegFluid.BEER;
        }
        if (fluid == VODKA) {
            return BNCKegFluid.VODKA;
        }
        if (fluid == MEAD) {
            return BNCKegFluid.MEAD;
        }
        if (fluid == RICE_WINE) {
            return BNCKegFluid.RICE_WINE;
        }
        if (fluid == EGG_GROG) {
            return BNCKegFluid.EGG_GROG;
        }
        if (fluid == STRONGROOT_ALE) {
            return BNCKegFluid.STRONGROOT_ALE;
        }
        if (fluid == SACCHARINE_RUM) {
            return BNCKegFluid.SACCHARINE_RUM;
        }
        if (fluid == BLOODY_MARY) {
            return BNCKegFluid.BLOODY_MARY;
        }
        if (fluid == STEEL_TOE_STOUT) {
            return BNCKegFluid.STEEL_TOE_STOUT;
        }
        if (fluid == GLITTERING_GRENADINE) {
            return BNCKegFluid.GLITTERING_GRENADINE;
        }
        if (fluid == PALE_JANE) {
            return BNCKegFluid.PALE_JANE;
        }
        if (fluid == SALTY_FOLLY) {
            return BNCKegFluid.SALTY_FOLLY;
        }
        if (fluid == DREAD_NOG) {
            return BNCKegFluid.DREAD_NOG;
        }
        if (fluid == RED_RUM) {
            return BNCKegFluid.RED_RUM;
        }
        if (fluid == WITHERING_DROSS) {
            return BNCKegFluid.WITHERING_DROSS;
        }
        if (fluid == KOMBUCHA) {
            return BNCKegFluid.KOMBUCHA;
        }
        if (fluid == GREEN_TEA) {
            return BNCKegFluid.GREEN_TEA;
        }
        if (fluid == FLAXEN_CHEESE) {
            return BNCKegFluid.FLAXEN_CHEESE;
        }
        if (fluid == SCARLET_CHEESE) {
            return BNCKegFluid.SCARLET_CHEESE;
        }
        String registeredName = FluidRegistry.getFluidName(fluid);
        return registeredName == null ? BNCKegFluid.EMPTY : registeredName;
    }

    public static int networkCodeFor(String fluidId) {
        Fluid fluid = fluidFor(fluidId);
        Integer code = fluid == null ? null : FluidRegistry.getRegisteredFluidIDs().get(fluid);
        return code == null ? 0 : code + 1;
    }

    public static String idFromNetworkCode(int code) {
        if (code <= 0) {
            return BNCKegFluid.EMPTY;
        }
        int fluidCode = code - 1;
        for (Map.Entry<Fluid, Integer> entry : FluidRegistry.getRegisteredFluidIDs().entrySet()) {
            if (entry.getValue() != null && entry.getValue() == fluidCode) {
                return idFor(entry.getKey());
            }
        }
        return BNCKegFluid.EMPTY;
    }

    public static Fluid fluidFor(String fluidId) {
        if (BNCKegFluid.WATER.equals(fluidId)) {
            return FluidRegistry.WATER;
        }
        if (BNCKegFluid.MILK.equals(fluidId)) {
            return MILK;
        }
        if (BNCKegFluid.HONEY.equals(fluidId)) {
            return HONEY;
        }
        if (BNCKegFluid.BEER.equals(fluidId)) {
            return BEER;
        }
        if (BNCKegFluid.VODKA.equals(fluidId)) {
            return VODKA;
        }
        if (BNCKegFluid.MEAD.equals(fluidId)) {
            return MEAD;
        }
        if (BNCKegFluid.RICE_WINE.equals(fluidId)) {
            return RICE_WINE;
        }
        if (BNCKegFluid.EGG_GROG.equals(fluidId)) {
            return EGG_GROG;
        }
        if (BNCKegFluid.STRONGROOT_ALE.equals(fluidId)) {
            return STRONGROOT_ALE;
        }
        if (BNCKegFluid.SACCHARINE_RUM.equals(fluidId)) {
            return SACCHARINE_RUM;
        }
        if (BNCKegFluid.BLOODY_MARY.equals(fluidId)) {
            return BLOODY_MARY;
        }
        if (BNCKegFluid.STEEL_TOE_STOUT.equals(fluidId)) {
            return STEEL_TOE_STOUT;
        }
        if (BNCKegFluid.GLITTERING_GRENADINE.equals(fluidId)) {
            return GLITTERING_GRENADINE;
        }
        if (BNCKegFluid.PALE_JANE.equals(fluidId)) {
            return PALE_JANE;
        }
        if (BNCKegFluid.SALTY_FOLLY.equals(fluidId)) {
            return SALTY_FOLLY;
        }
        if (BNCKegFluid.DREAD_NOG.equals(fluidId)) {
            return DREAD_NOG;
        }
        if (BNCKegFluid.RED_RUM.equals(fluidId)) {
            return RED_RUM;
        }
        if (BNCKegFluid.WITHERING_DROSS.equals(fluidId)) {
            return WITHERING_DROSS;
        }
        if (BNCKegFluid.KOMBUCHA.equals(fluidId)) {
            return KOMBUCHA;
        }
        if (BNCKegFluid.GREEN_TEA.equals(fluidId)) {
            return GREEN_TEA;
        }
        if (BNCKegFluid.FLAXEN_CHEESE.equals(fluidId)) {
            return FLAXEN_CHEESE;
        }
        if (BNCKegFluid.SCARLET_CHEESE.equals(fluidId)) {
            return SCARLET_CHEESE;
        }
        return FluidRegistry.getFluid(fluidId);
    }

    public static List<ResourceLocation> spriteLocations() {
        return Collections.unmodifiableList(Arrays.asList(
            MILK_STILL,
            MILK_FLOW,
            ALCOHOL_WATER_STILL,
            ALCOHOL_WATER_FLOW,
            HONEY_STILL,
            FLAXEN_CHEESE_STILL,
            FLAXEN_CHEESE_FLOW,
            SCARLET_CHEESE_STILL,
            SCARLET_CHEESE_FLOW
        ));
    }

    private static Fluid registerOrGet(String name, String translationName, ResourceLocation still, ResourceLocation flowing, int color, int density, int temperature, int viscosity) {
        Fluid existing = FluidRegistry.getFluid(name);
        if (existing != null) {
            return existing;
        }

        Fluid fluid = new Fluid(name, still, flowing, color)
            .setUnlocalizedName(BrewinAndChewinLegacy.MODID + "." + translationName)
            .setDensity(density)
            .setTemperature(temperature)
            .setViscosity(viscosity);
        FluidRegistry.registerFluid(fluid);
        return FluidRegistry.getFluid(name);
    }
}
