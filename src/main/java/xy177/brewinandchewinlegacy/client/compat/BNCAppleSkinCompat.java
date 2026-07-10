package xy177.brewinandchewinlegacy.client.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public final class BNCAppleSkinCompat {
    private static final ResourceLocation APPLESKIN_ICONS = new ResourceLocation("appleskin", "textures/icons.png");

    private static boolean initialized;
    private static boolean available;
    private static Field showSaturationOverlay;
    private static Field showFoodValuesOverlay;
    private static Field showExhaustionUnderlay;
    private static Method isFood;
    private static Method getModifiedFoodValues;
    private static Method getSaturationIncrement;
    private static Method getExhaustion;
    private static Method getMaxExhaustion;
    private static Field hunger;
    private static float flashAlpha;
    private static int alphaDirection = 1;

    private BNCAppleSkinCompat() {
    }

    public static void clientTick() {
        if (!isAvailable()) {
            return;
        }
        flashAlpha += alphaDirection * 0.125F;
        if (flashAlpha >= 1.5F) {
            flashAlpha = 1.0F;
            alphaDirection = -1;
        } else if (flashAlpha <= -0.5F) {
            flashAlpha = 0.0F;
            alphaDirection = 1;
        }
    }

    public static void renderExhaustionUnderlay(Minecraft mc, int right, int top) {
        if (!isAvailable() || !getBoolean(showExhaustionUnderlay)) {
            return;
        }
        try {
            float exhaustion = ((Number) getExhaustion.invoke(null, mc.player)).floatValue();
            float maxExhaustion = ((Number) getMaxExhaustion.invoke(null, mc.player)).floatValue();
            int width = (int) (Math.min(1.0F, Math.max(0.0F, exhaustion / maxExhaustion)) * 81.0F);
            if (width <= 0) {
                return;
            }
            mc.getTextureManager().bindTexture(APPLESKIN_ICONS);
            enableAlpha(0.75F);
            Gui.drawModalRectWithCustomSizedTexture(right - width, top, 81.0F - width, 18.0F, width, 9, 256.0F, 256.0F);
            disableAlpha();
        } catch (ReflectiveOperationException exception) {
            disable(exception);
        }
    }

    public static void renderFoodOverlays(Minecraft mc, int[] iconX, int[] iconY) {
        if (!isAvailable()) {
            return;
        }
        EntityPlayer player = mc.player;
        int food = player.getFoodStats().getFoodLevel();
        float saturation = player.getFoodStats().getSaturationLevel();

        if (getBoolean(showSaturationOverlay)) {
            renderSaturation(mc, iconX, iconY, 0.0F, saturation, 1.0F);
        }

        ItemStack held = player.getHeldItemMainhand();
        if (!isFood(held)) {
            held = player.getHeldItemOffhand();
        }
        if (!getBoolean(showFoodValuesOverlay) || !isFood(held)) {
            flashAlpha = 0.0F;
            alphaDirection = 1;
            return;
        }

        try {
            Object values = getModifiedFoodValues.invoke(null, held, player);
            int gainedFood = hunger.getInt(values);
            float gainedSaturation = ((Number) getSaturationIncrement.invoke(values)).floatValue();
            renderHunger(mc, iconX, iconY, gainedFood, food, flashAlpha);
            if (getBoolean(showSaturationOverlay)) {
                int newFood = food + gainedFood;
                float newSaturation = saturation + gainedSaturation;
                float visibleGain = newSaturation > newFood ? newFood - saturation : gainedSaturation;
                renderSaturation(mc, iconX, iconY, visibleGain, saturation, flashAlpha);
            }
        } catch (ReflectiveOperationException exception) {
            disable(exception);
        }
    }

    private static void renderSaturation(Minecraft mc, int[] iconX, int[] iconY, float gained, float saturation, float alpha) {
        int start = gained == 0.0F ? 0 : Math.max(0, (int) saturation / 2);
        int end = (int) Math.ceil(Math.min(20.0F, saturation + gained) / 2.0F);
        mc.getTextureManager().bindTexture(APPLESKIN_ICONS);
        enableAlpha(alpha);
        for (int i = start; i < end && i < iconX.length; i++) {
            float value = (saturation + gained) / 2.0F - i;
            int textureX = value >= 1.0F ? 27 : value > 0.5F ? 18 : value > 0.25F ? 9 : value > 0.0F ? 0 : -1;
            if (textureX >= 0) {
                Gui.drawModalRectWithCustomSizedTexture(iconX[i], iconY[i], textureX, 0.0F, 9, 9, 256.0F, 256.0F);
            }
        }
        disableAlpha();
    }

    private static void renderHunger(Minecraft mc, int[] iconX, int[] iconY, int gained, int food, float alpha) {
        if (gained == 0) {
            return;
        }
        int start = food / 2;
        int end = (int) Math.ceil(Math.min(20, food + gained) / 2.0F);
        int icon = mc.player.isPotionActive(MobEffects.HUNGER) ? 52 : 16;
        int background = 16 + 13 * 9;
        mc.getTextureManager().bindTexture(Gui.ICONS);
        enableAlpha(alpha);
        for (int i = start; i < end && i < iconX.length; i++) {
            int value = i * 2 + 1;
            Gui.drawModalRectWithCustomSizedTexture(iconX[i], iconY[i], background, 27.0F, 9, 9, 256.0F, 256.0F);
            if (value < food + gained) {
                Gui.drawModalRectWithCustomSizedTexture(iconX[i], iconY[i], icon + 36, 27.0F, 9, 9, 256.0F, 256.0F);
            } else if (value == food + gained) {
                Gui.drawModalRectWithCustomSizedTexture(iconX[i], iconY[i], icon + 45, 27.0F, 9, 9, 256.0F, 256.0F);
            }
        }
        disableAlpha();
    }

    private static boolean isFood(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        try {
            return (Boolean) isFood.invoke(null, stack);
        } catch (ReflectiveOperationException exception) {
            disable(exception);
            return false;
        }
    }

    private static boolean getBoolean(Field field) {
        try {
            return field != null && field.getBoolean(null);
        } catch (ReflectiveOperationException exception) {
            disable(exception);
            return false;
        }
    }

    private static boolean isAvailable() {
        if (!initialized) {
            initialize();
        }
        return available;
    }

    private static void initialize() {
        initialized = true;
        if (!Loader.isModLoaded("appleskin")) {
            return;
        }
        try {
            Class<?> config = Class.forName("squeek.appleskin.ModConfig");
            Class<?> foodHelper = Class.forName("squeek.appleskin.helpers.FoodHelper");
            Class<?> values = Class.forName("squeek.appleskin.helpers.FoodHelper$BasicFoodValues");
            Class<?> hungerHelper = Class.forName("squeek.appleskin.helpers.HungerHelper");

            showSaturationOverlay = config.getField("SHOW_SATURATION_OVERLAY");
            showFoodValuesOverlay = config.getField("SHOW_FOOD_VALUES_OVERLAY");
            showExhaustionUnderlay = config.getField("SHOW_FOOD_EXHAUSTION_UNDERLAY");
            isFood = foodHelper.getMethod("isFood", ItemStack.class);
            getModifiedFoodValues = foodHelper.getMethod("getModifiedFoodValues", ItemStack.class, EntityPlayer.class);
            hunger = values.getField("hunger");
            getSaturationIncrement = values.getMethod("getSaturationIncrement");
            getExhaustion = hungerHelper.getMethod("getExhaustion", EntityPlayer.class);
            getMaxExhaustion = hungerHelper.getMethod("getMaxExhaustion", EntityPlayer.class);
            available = true;
        } catch (ReflectiveOperationException exception) {
            disable(exception);
        }
    }

    private static void enableAlpha(float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
    }

    private static void disableAlpha() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
    }

    private static void disable(Exception exception) {
        if (available || !initialized) {
            BrewinAndChewinLegacy.getLogger().warn("AppleSkin HUD compatibility was disabled.", exception);
        }
        available = false;
        disableAlpha();
    }
}
