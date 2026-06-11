package xy177.brewinandchewinlegacy.common.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public class BNCPotion extends Potion {
    private final String iconName;

    public BNCPotion(boolean badEffect, int liquidColor, String name, boolean beneficial) {
        super(badEffect, liquidColor);
        this.iconName = name;
        setPotionName("effect.brewinandchewinlegacy." + name);
        if (beneficial) {
            setBeneficial();
        }
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        drawIcon(x + 6, y + 7, 1.0F);
        Minecraft mc = Minecraft.getMinecraft();
        String name = I18n.format(effect.getEffectName());
        if (effect.getAmplifier() > 0) {
            name = name + " " + levelName(effect.getAmplifier() + 1);
        }
        mc.fontRenderer.drawStringWithShadow(name, (float) (x + 28), (float) (y + 6), 0xFFFFFF);
        String duration = Potion.getPotionDurationString(effect, 1.0F);
        mc.fontRenderer.drawStringWithShadow(duration, (float) (x + 28), (float) (y + 16), 0x7F7F7F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
        drawIcon(x + 3, y + 3, alpha);
    }

    @SideOnly(Side.CLIENT)
    private void drawIcon(int x, int y, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(
            new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/mob_effect/" + iconName + ".png")
        );
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 18, 18, 18.0F, 18.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    private static String levelName(int level) {
        String key = "enchantment.level." + level;
        if (I18n.hasKey(key)) {
            return I18n.format(key);
        }
        return toRoman(level);
    }

    private static String toRoman(int number) {
        String[] numerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        if (number >= 0 && number < numerals.length) {
            return numerals[number];
        }
        return Integer.toString(number);
    }

    public static class SweetHeart extends BNCPotion {
        public SweetHeart() {
            super(false, 0xFF07D9, "sweet_heart", true);
        }

        @Override
        public boolean isReady(int duration, int amplifier) {
            int interval = 20 >> amplifier;
            return interval <= 0 || duration % interval == 0;
        }

        @Override
        public void performEffect(EntityLivingBase entity, int amplifier) {
            if (!(entity instanceof EntityPlayer) || entity.world.isRemote || entity.getHealth() >= entity.getMaxHealth()) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            float saturation = player.getFoodStats().getSaturationLevel();
            if (saturation <= 0.0F) {
                return;
            }
            float healing = Math.min(saturation, 1.0F);
            player.heal(healing);
            player.getFoodStats().setFoodSaturationLevel(Math.max(0.0F, saturation - healing));
        }
    }
}
