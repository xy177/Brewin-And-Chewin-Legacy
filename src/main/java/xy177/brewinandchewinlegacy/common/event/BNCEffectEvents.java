package xy177.brewinandchewinlegacy.common.event;

import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.item.BNCBoozeItem;
import xy177.brewinandchewinlegacy.common.item.BNCFoodItem;
import xy177.brewinandchewinlegacy.common.network.BNCNetwork;
import xy177.brewinandchewinlegacy.common.registry.BNCEffects;

@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID)
public final class BNCEffectEvents {
    private static final String INTOXICATION_PRE_SATURATION = "BNC_IntoxicationPreSaturation";
    private static final String TIPSY_DAMAGE = "BNC_TipsyDamage";
    private static final String TIPSY_DAMAGE_TICKS = "BNC_TipsyDamageTicks";
    private static final String RAGING_STACKS = "BNC_RagingStacks";
    private static final String RAGING_TICKS = "BNC_RagingTicks";
    private static final String RAGING_PRE_ATTACK_STRENGTH = "BNC_RagingPreAttackStrength";
    private static final String RAGING_PRE_ATTACK_TARGET = "BNC_RagingPreAttackTarget";
    private static final String RAGING_PRE_ATTACK_TICK = "BNC_RagingPreAttackTick";
    private static final UUID RAGING_ATTACK_SPEED_UUID = UUID.fromString("34c32e92-2a13-4a74-bbf1-7c6265486b32");
    private static final DamageSource CARDIAC_ARREST = new DamageSource("brewinandchewin.cardiacArrest")
        .setDamageBypassesArmor()
        .setDamageIsAbsolute();

    private BNCEffectEvents() {
    }

    @SubscribeEvent
    public static void onUseItemStart(LivingEntityUseItemEvent.Start event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer) || !entity.isPotionActive(BNCEffects.INTOXICATION) || !(event.getItem().getItem() instanceof ItemFood)) {
            return;
        }
        entity.getEntityData().setFloat(INTOXICATION_PRE_SATURATION, ((EntityPlayer) entity).getFoodStats().getSaturationLevel());
    }

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!entity.world.isRemote && entity instanceof EntityPlayer) {
            applyBoozeSpecialEffects((EntityPlayer) entity, event);
        }
        if (!(entity instanceof EntityPlayer) || !entity.isPotionActive(BNCEffects.INTOXICATION) || !(event.getItem().getItem() instanceof ItemFood)) {
            return;
        }
        NBTTagCompound data = entity.getEntityData();
        if (!data.hasKey(INTOXICATION_PRE_SATURATION)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;
        float before = data.getFloat(INTOXICATION_PRE_SATURATION);
        float after = player.getFoodStats().getSaturationLevel();
        if (after > before) {
            player.getFoodStats().setFoodSaturationLevel(before);
        }
        data.removeTag(INTOXICATION_PRE_SATURATION);
    }

    private static void applyBoozeSpecialEffects(EntityPlayer player, LivingEntityUseItemEvent.Finish event) {
        if (!(event.getItem().getItem() instanceof BNCBoozeItem)) {
            return;
        }
        BNCBoozeItem booze = (BNCBoozeItem) event.getItem().getItem();
        applyTipsyFromBooze(player, booze.getTipsyEffect());
        applyBadOmenFromBooze(player, booze.getPreferredBadOmenEffect());
    }

    private static void applyTipsyFromBooze(EntityPlayer player, BNCFoodItem.EffectEntry entry) {
        if (entry == null || player.world.rand.nextFloat() > entry.chance || BNCEffects.TIPSY == null) {
            return;
        }
        PotionEffect current = player.getActivePotionEffect(BNCEffects.TIPSY);
        int duration = current == null || current.getDuration() != -1 ? entry.duration : -1;
        int amplifier = entry.amplifier;
        if (current != null) {
            duration = current.getDuration() == -1 ? -1 : current.getDuration() + entry.duration;
            amplifier = Math.min(current.getAmplifier() + entry.amplifier + 1, 9);
            player.removePotionEffect(BNCEffects.TIPSY);
        }
        player.addPotionEffect(new PotionEffect(BNCEffects.TIPSY, duration, amplifier, false, false));
        if (player instanceof EntityPlayerMP) {
            BNCNetwork.sendTipsyEffect((EntityPlayerMP) player, duration, amplifier);
        }
    }

    private static void applyBadOmenFromBooze(EntityPlayer player, BNCFoodItem.EffectEntry entry) {
        if (entry == null || player.world.rand.nextFloat() > entry.chance) {
            return;
        }
        Potion badOmen = ForgeRegistries.POTIONS.getValue(entry.effectId);
        if (badOmen == null) {
            return;
        }
        PotionEffect current = player.getActivePotionEffect(badOmen);
        int duration = entry.duration;
        int amplifier = entry.amplifier;
        if (current != null) {
            duration = current.getDuration() == -1 ? -1 : Math.max(current.getDuration(), entry.duration);
            amplifier = Math.min(current.getAmplifier() + entry.amplifier + 1, 4);
            player.removePotionEffect(badOmen);
        }
        player.addPotionEffect(new PotionEffect(badOmen, duration, amplifier, false, false));
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isPotionActive(BNCEffects.RAGING)) {
            return;
        }
        NBTTagCompound data = player.getEntityData();
        data.setFloat(RAGING_PRE_ATTACK_STRENGTH, player.getCooledAttackStrength(0.0F));
        data.setInteger(RAGING_PRE_ATTACK_TARGET, event.getTarget().getEntityId());
        data.setInteger(RAGING_PRE_ATTACK_TICK, player.ticksExisted);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        if (target.isPotionActive(BNCEffects.TIPSY) && event.getSource() != CARDIAC_ARREST) {
            PotionEffect effect = target.getActivePotionEffect(BNCEffects.TIPSY);
            int amplifier = effect == null ? 0 : effect.getAmplifier();
            float maximum = Math.min(Math.max(1.0F, target.getMaxHealth() - 2.0F), Math.max(1.0F, (float) Math.floor((2.0F + amplifier * 1.6F) / 2.0F) * 2.0F));
            NBTTagCompound data = target.getEntityData();
            float stored = data.getFloat(TIPSY_DAMAGE);
            float delayed = Math.min(maximum - stored, event.getAmount());
            if (delayed > 0.0F && delayed <= target.getHealth()) {
                event.setAmount(event.getAmount() - delayed);
                data.setFloat(TIPSY_DAMAGE, stored + delayed);
                data.setInteger(TIPSY_DAMAGE_TICKS, 200 + 20 * amplifier);
                syncTipsyDamage(target, stored + delayed, 200 + 20 * amplifier);
            }
        }

        if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
            if (attacker.isPotionActive(BNCEffects.RAGING)
                && event.getAmount() > 0.0F
                && triggersRaging(event.getSource(), attacker)
                && isRagingAttackCharged(attacker, target)) {
                NBTTagCompound data = attacker.getEntityData();
                int stacks = Math.min(4, data.getInteger(RAGING_STACKS) + 1);
                data.setInteger(RAGING_STACKS, stacks);
                data.setInteger(RAGING_TICKS, ragingResetTicks(attacker));
                updateRagingModifier(attacker, stacks);
                syncRagingStacks(attacker, stacks);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) {
            return;
        }
        tickTipsyDamage(entity);
        tickRaging(entity);
    }

    private static void tickTipsyDamage(EntityLivingBase entity) {
        NBTTagCompound data = entity.getEntityData();
        float stored = data.getFloat(TIPSY_DAMAGE);
        if (stored <= 0.0F) {
            return;
        }
        int ticks = data.getInteger(TIPSY_DAMAGE_TICKS);
        if (ticks > 0 && entity.isPotionActive(BNCEffects.TIPSY)) {
            data.setInteger(TIPSY_DAMAGE_TICKS, ticks - 1);
            return;
        }
        data.removeTag(TIPSY_DAMAGE);
        data.removeTag(TIPSY_DAMAGE_TICKS);
        syncTipsyDamage(entity, 0.0F, 0);
        entity.attackEntityFrom(CARDIAC_ARREST, stored);
    }

    private static void syncTipsyDamage(EntityLivingBase entity, float damage, int ticks) {
        if (entity instanceof EntityPlayerMP) {
            BNCNetwork.sendTipsyDamage((EntityPlayerMP) entity, damage, ticks);
        }
    }

    private static void tickRaging(EntityLivingBase entity) {
        NBTTagCompound data = entity.getEntityData();
        int stacks = data.getInteger(RAGING_STACKS);
        if (stacks <= 0) {
            clearRaging(entity, data);
            return;
        }
        if (!entity.isPotionActive(BNCEffects.RAGING)) {
            clearRaging(entity, data);
            return;
        }
        int ticks = data.getInteger(RAGING_TICKS);
        if (ticks > 0) {
            data.setInteger(RAGING_TICKS, ticks - 1);
            updateRagingModifier(entity, stacks);
            return;
        }
        stacks--;
        if (stacks <= 0) {
            clearRaging(entity, data);
            return;
        }
        data.setInteger(RAGING_STACKS, stacks);
        data.setInteger(RAGING_TICKS, ragingResetTicks(entity));
        updateRagingModifier(entity, stacks);
        syncRagingStacks(entity, stacks);
    }

    private static void updateRagingModifier(EntityLivingBase entity, int stacks) {
        IAttributeInstance attackSpeed = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        if (attackSpeed == null) {
            return;
        }
        AttributeModifier oldModifier = attackSpeed.getModifier(RAGING_ATTACK_SPEED_UUID);
        if (oldModifier != null) {
            attackSpeed.removeModifier(oldModifier);
        }
        PotionEffect effect = entity.getActivePotionEffect(BNCEffects.RAGING);
        int amplifier = effect == null ? 0 : effect.getAmplifier();
        double amount = Math.min(0.8D, 0.05D * stacks + 0.025D * amplifier * stacks);
        attackSpeed.applyModifier(new AttributeModifier(RAGING_ATTACK_SPEED_UUID, "BrewinAndChewinLegacy raging", amount, 2).setSaved(false));
    }

    private static void removeRagingModifier(EntityLivingBase entity) {
        IAttributeInstance attackSpeed = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        if (attackSpeed == null) {
            return;
        }
        AttributeModifier modifier = attackSpeed.getModifier(RAGING_ATTACK_SPEED_UUID);
        if (modifier != null) {
            attackSpeed.removeModifier(modifier);
        }
    }

    private static void clearRaging(EntityLivingBase entity, NBTTagCompound data) {
        boolean hadStacks = data.hasKey(RAGING_STACKS);
        data.removeTag(RAGING_STACKS);
        data.removeTag(RAGING_TICKS);
        removeRagingModifier(entity);
        if (hadStacks) {
            syncRagingStacks(entity, 0);
        }
    }

    private static int ragingResetTicks(EntityLivingBase entity) {
        float delay = entity instanceof EntityPlayer ? ((EntityPlayer) entity).getCooldownPeriod() : 30.0F;
        return Math.max(1, (int) Math.ceil(2.5F * delay));
    }

    private static boolean triggersRaging(DamageSource source, EntityLivingBase attacker) {
        if (source.getTrueSource() != attacker || source.getImmediateSource() != attacker) {
            return false;
        }
        String type = source.getDamageType();
        return "player".equals(type) || "mob".equals(type);
    }

    private static boolean isRagingAttackCharged(EntityLivingBase attacker, EntityLivingBase target) {
        if (!(attacker instanceof EntityPlayer)) {
            return true;
        }
        NBTTagCompound data = attacker.getEntityData();
        if (data.hasKey(RAGING_PRE_ATTACK_STRENGTH)
            && data.getInteger(RAGING_PRE_ATTACK_TARGET) == target.getEntityId()
            && attacker.ticksExisted - data.getInteger(RAGING_PRE_ATTACK_TICK) <= 5) {
            float strength = data.getFloat(RAGING_PRE_ATTACK_STRENGTH);
            data.removeTag(RAGING_PRE_ATTACK_STRENGTH);
            data.removeTag(RAGING_PRE_ATTACK_TARGET);
            data.removeTag(RAGING_PRE_ATTACK_TICK);
            return strength > 0.8F;
        }
        return ((EntityPlayer) attacker).getCooledAttackStrength(0.0F) > 0.8F;
    }

    private static void syncRagingStacks(EntityLivingBase entity, int stacks) {
        BNCNetwork.sendRagingStacks(entity, stacks);
    }
}
