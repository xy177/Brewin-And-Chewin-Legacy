package xy177.brewinandchewinlegacy.common.network;

import java.util.HashMap;
import java.util.Map;

public final class BNCClientEffectState {
    private static float tipsyDamage;
    private static int tipsyDamageTicks;
    private static boolean pendingTipsyEffect;
    private static int pendingTipsyDuration;
    private static int pendingTipsyAmplifier;
    private static final Map<Integer, Integer> RAGING_STACKS = new HashMap<>();

    private BNCClientEffectState() {
    }

    public static void setTipsyDamage(float damage, int ticks) {
        tipsyDamage = Math.max(0.0F, damage);
        tipsyDamageTicks = Math.max(0, ticks);
    }

    public static float getTipsyDamage() {
        return tipsyDamage;
    }

    public static int getTipsyDamageTicks() {
        return tipsyDamageTicks;
    }

    public static void setTipsyEffect(int duration, int amplifier) {
        pendingTipsyEffect = true;
        pendingTipsyDuration = duration;
        pendingTipsyAmplifier = Math.max(0, amplifier);
    }

    public static int[] getPendingTipsyEffect() {
        if (!pendingTipsyEffect) {
            return null;
        }
        return new int[] {pendingTipsyDuration, pendingTipsyAmplifier};
    }

    public static void clearPendingTipsyEffect() {
        pendingTipsyEffect = false;
        pendingTipsyDuration = 0;
        pendingTipsyAmplifier = 0;
    }

    public static void setRagingStacks(int entityId, int stacks) {
        if (stacks <= 0) {
            RAGING_STACKS.remove(entityId);
            return;
        }
        RAGING_STACKS.put(entityId, Math.min(4, stacks));
    }

    public static int getRagingStacks(int entityId) {
        Integer stacks = RAGING_STACKS.get(entityId);
        return stacks == null ? 0 : stacks;
    }

    public static void clientTick() {
        if (tipsyDamageTicks > 0) {
            tipsyDamageTicks--;
        }
        if (tipsyDamageTicks <= 0 && tipsyDamage > 0.0F) {
            tipsyDamage = 0.0F;
        }
    }

    public static void clear() {
        tipsyDamage = 0.0F;
        tipsyDamageTicks = 0;
        pendingTipsyEffect = false;
        pendingTipsyDuration = 0;
        pendingTipsyAmplifier = 0;
        RAGING_STACKS.clear();
    }
}
