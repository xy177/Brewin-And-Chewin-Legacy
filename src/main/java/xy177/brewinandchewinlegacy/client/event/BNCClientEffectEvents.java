package xy177.brewinandchewinlegacy.client.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.network.BNCClientEffectState;
import xy177.brewinandchewinlegacy.common.registry.BNCEffects;

@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID, value = Side.CLIENT)
public final class BNCClientEffectEvents extends Gui {
    private static final int TEXT_SCRAMBLE_LEVEL = 3;
    private static final ResourceLocation FOOD_EMPTY_INTOXICATION = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/hud/food_empty_intoxication.png");
    private static final ResourceLocation FOOD_FULL_INTOXICATION = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/hud/food_full_intoxication.png");
    private static final ResourceLocation FOOD_HALF_INTOXICATION = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/hud/food_half_intoxication.png");
    private static final Random RANDOM = new Random();
    private static final BNCClientEffectEvents HUD = new BNCClientEffectEvents();
    private static final Map<BlockPos, ITextComponent[]> ORIGINAL_SIGN_TEXT = new HashMap<>();

    private BNCClientEffectEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null || mc.world == null) {
            BNCClientEffectState.clear();
            restoreSigns(null);
            return;
        }

        BNCClientEffectState.clientTick();
        applyPendingTipsyEffect(player);
        spawnEffectParticles(player);
        updateNearbySigns(mc.world, player);
    }

    private static void applyPendingTipsyEffect(EntityPlayer player) {
        int[] effect = BNCClientEffectState.getPendingTipsyEffect();
        if (effect == null || BNCEffects.TIPSY == null || !player.isPotionActive(BNCEffects.TIPSY)) {
            return;
        }
        player.removePotionEffect(BNCEffects.TIPSY);
        player.addPotionEffect(new PotionEffect(BNCEffects.TIPSY, effect[0], effect[1], false, false));
        BNCClientEffectState.clearPendingTipsyEffect();
    }

    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        PotionEffect effect = player.getActivePotionEffect(BNCEffects.TIPSY);
        if (effect == null) {
            return;
        }

        float ticks = player.ticksExisted + (float) event.getRenderPartialTicks();
        float strength = Math.min(11, effect.getAmplifier() + 1);
        event.setRoll(event.getRoll() + (float) Math.sin(ticks * 0.11F) * strength * 0.45F);
        event.setPitch(event.getPitch() + (float) Math.sin(ticks * 0.07F + 2.0F) * strength * 0.12F);
        event.setYaw(event.getYaw() + (float) Math.cos(ticks * 0.05F + 3.0F) * strength * 0.12F);
    }

    @SubscribeEvent
    public static void onClientChat(ClientChatEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || event.getMessage().startsWith("/")) {
            return;
        }
        PotionEffect effect = mc.player.getActivePotionEffect(BNCEffects.TIPSY);
        if (effect != null && effect.getAmplifier() >= TEXT_SCRAMBLE_LEVEL) {
            event.setMessage(scramble(event.getMessage(), mc.player.getRNG(), effect.getAmplifier()));
        }
    }

    @SubscribeEvent
    public static void onRenderHealth(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        float tipsyDamage = BNCClientEffectState.getTipsyDamage();
        if (player == null || tipsyDamage <= 0.0F || !player.isPotionActive(BNCEffects.TIPSY)) {
            return;
        }

        renderTipsyHearts(mc, event.getResolution(), tipsyDamage);
    }

    @SubscribeEvent
    public static void onRenderFood(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null || !player.isPotionActive(BNCEffects.INTOXICATION)) {
            return;
        }
        renderIntoxicationFoodOverlay(mc, event.getResolution());
    }

    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer viewer = mc.player;
        if (viewer == null || event.getEntityPlayer() == viewer) {
            return;
        }
        PotionEffect effect = viewer.getActivePotionEffect(BNCEffects.TIPSY);
        if (effect != null && effect.getAmplifier() >= TEXT_SCRAMBLE_LEVEL) {
            event.setDisplayname(scramble(event.getDisplayname(), new Random(event.getEntityPlayer().getUniqueID().getLeastSignificantBits()), effect.getAmplifier()));
        }
    }

    private static void spawnEffectParticles(EntityPlayer player) {
        if (player.world.getTotalWorldTime() % 4L == 0L && player.isPotionActive(BNCEffects.TIPSY)) {
            spawnAround(player, EnumParticleTypes.SPELL_MOB_AMBIENT, 0.95D, 0.52D, 0.25D);
        }
        if (player.world.getTotalWorldTime() % 3L == 0L && player.isPotionActive(BNCEffects.RAGING)) {
            spawnRagingParticle(player);
        }
        if (player.world.getTotalWorldTime() % 8L == 0L && player.isPotionActive(BNCEffects.SWEET_HEART)) {
            spawnAround(player, EnumParticleTypes.HEART, 0.0D, 0.0D, 0.0D);
        }
    }

    private static void spawnRagingParticle(EntityPlayer player) {
        int stacks = BNCClientEffectState.getRagingStacks(player.getEntityId());
        if (stacks <= 0 && player.getRNG().nextInt(12) != 0) {
            return;
        }
        double red = stacks >= 4 ? 0.96D : stacks >= 3 ? 0.92D : stacks >= 2 ? 0.85D : 0.70D;
        double green = stacks >= 4 ? 0.01D : stacks >= 3 ? 0.06D : stacks >= 2 ? 0.15D : 0.25D;
        double blue = stacks >= 4 ? 0.01D : stacks >= 3 ? 0.03D : stacks >= 2 ? 0.04D : 0.08D;
        spawnAround(player, EnumParticleTypes.REDSTONE, red, green, blue);
    }

    private static void spawnAround(EntityLivingBase entity, EnumParticleTypes particle, double xSpeed, double ySpeed, double zSpeed) {
        double x = entity.posX + (RANDOM.nextDouble() - 0.5D) * entity.width;
        double y = entity.posY + 0.4D + RANDOM.nextDouble() * entity.height * 0.8D;
        double z = entity.posZ + (RANDOM.nextDouble() - 0.5D) * entity.width;
        entity.world.spawnParticle(particle, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    private static void updateNearbySigns(World world, EntityPlayer player) {
        PotionEffect effect = player.getActivePotionEffect(BNCEffects.TIPSY);
        if (effect == null || effect.getAmplifier() < TEXT_SCRAMBLE_LEVEL) {
            restoreSigns(world);
            return;
        }

        int radius = 8;
        for (BlockPos pos : BlockPos.getAllInBoxMutable(player.getPosition().add(-radius, -radius, -radius), player.getPosition().add(radius, radius, radius))) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntitySign) {
                scrambleSign((TileEntitySign) tile, effect.getAmplifier());
            }
        }
    }

    private static void scrambleSign(TileEntitySign sign, int amplifier) {
        BlockPos pos = sign.getPos().toImmutable();
        if (!ORIGINAL_SIGN_TEXT.containsKey(pos)) {
            ITextComponent[] copy = new ITextComponent[sign.signText.length];
            for (int i = 0; i < sign.signText.length; i++) {
                copy[i] = sign.signText[i] == null ? new TextComponentString("") : sign.signText[i].createCopy();
            }
            ORIGINAL_SIGN_TEXT.put(pos, copy);
        }

        ITextComponent[] original = ORIGINAL_SIGN_TEXT.get(pos);
        Random seeded = new Random(pos.toLong());
        for (int i = 0; i < sign.signText.length; i++) {
            String text = original[i] == null ? "" : original[i].getUnformattedText();
            sign.signText[i] = new TextComponentString(scramble(text, seeded, amplifier));
        }
    }

    private static void restoreSigns(World world) {
        if (ORIGINAL_SIGN_TEXT.isEmpty()) {
            return;
        }
        if (world != null) {
            for (Map.Entry<BlockPos, ITextComponent[]> entry : ORIGINAL_SIGN_TEXT.entrySet()) {
                TileEntity tile = world.getTileEntity(entry.getKey());
                if (tile instanceof TileEntitySign) {
                    TileEntitySign sign = (TileEntitySign) tile;
                    ITextComponent[] original = entry.getValue();
                    for (int i = 0; i < sign.signText.length && i < original.length; i++) {
                        sign.signText[i] = original[i] == null ? new TextComponentString("") : original[i].createCopy();
                    }
                }
            }
        }
        ORIGINAL_SIGN_TEXT.clear();
    }

    private static String scramble(String text, Random random, int amplifier) {
        if (text.length() < 4) {
            return text;
        }
        StringBuilder builder = new StringBuilder(text);
        int level = Math.max(0, amplifier - TEXT_SCRAMBLE_LEVEL);
        int swaps = Math.max(1, (level + 1) * Math.max(1, builder.length() / 6));
        for (int i = 0; i < swaps; i++) {
            int index = 1 + random.nextInt(Math.max(1, builder.length() - 2));
            int other = Math.max(1, Math.min(builder.length() - 2, index + (random.nextBoolean() ? 1 : -1)));
            char first = builder.charAt(index);
            builder.setCharAt(index, builder.charAt(other));
            builder.setCharAt(other, first);
        }
        return builder.toString();
    }

    private static void renderIntoxicationFoodOverlay(Minecraft mc, ScaledResolution resolution) {
        int food = mc.player.getFoodStats().getFoodLevel();
        int left = resolution.getScaledWidth() / 2 + 91;
        int top = resolution.getScaledHeight() - 39;
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i = 0; i < 10; i++) {
            int x = left - i * 8 - 9;
            int y = top;
            ResourceLocation texture = FOOD_EMPTY_INTOXICATION;
            int value = i * 2 + 1;
            if (value < food) {
                texture = FOOD_FULL_INTOXICATION;
            } else if (value == food) {
                texture = FOOD_HALF_INTOXICATION;
            }
            mc.getTextureManager().bindTexture(texture);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 9, 9, 9.0F, 9.0F);
        }
        GlStateManager.disableBlend();
    }

    private static void renderTipsyHearts(Minecraft mc, ScaledResolution resolution, float tipsyDamage) {
        int health = (int) Math.ceil(mc.player.getHealth());
        float maxHealth = (float) mc.player.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
        float absorb = (float) Math.ceil(mc.player.getAbsorptionAmount());
        int healthRows = (int) Math.ceil((maxHealth + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        int left = resolution.getScaledWidth() / 2 - 91;
        int top = resolution.getScaledHeight() - 39;
        int remaining = (int) Math.ceil(tipsyDamage);
        int start = Math.max(0, (health - remaining + 1) / 2);
        int end = Math.max(start, (health + 1) / 2);
        int textureY = 9 * (mc.world.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);

        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 0.62F, 0.22F, BNCClientEffectState.getTipsyDamageTicks() < 80 ? 0.65F : 0.9F);
        mc.getTextureManager().bindTexture(Gui.ICONS);
        for (int i = start; i < end; i++) {
            int row = (int) Math.ceil((i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;
            if (mc.player.isPotionActive(MobEffects.REGENERATION) && i == mc.ingameGUI.getUpdateCounter() % 25) {
                y -= 2;
            }
            int heartsBefore = Math.max(0, i * 2 - (health - remaining));
            boolean half = remaining - heartsBefore == 1;
            HUD.drawTexturedModalRect(x, y, half ? 61 : 52, textureY, 9, 9);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
    }
}
