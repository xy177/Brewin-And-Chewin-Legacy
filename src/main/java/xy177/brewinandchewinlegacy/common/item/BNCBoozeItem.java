package xy177.brewinandchewinlegacy.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BNCBoozeItem extends BNCFoodItem {
    public BNCBoozeItem(Item containerItem) {
        super(0, 0.0F, false, containerItem, EnumAction.DRINK);
        setMaxStackSize(16);
        setAlwaysEdible();
    }

    @Override
    protected boolean shouldApplyEffect(EffectEntry entry) {
        return !TIPSY_ID.equals(entry.effectId) && !"bad_omen".equals(entry.effectId.getResourcePath());
    }

    public EffectEntry getTipsyEffect() {
        return findEffect(TIPSY_ID);
    }

    public List<EffectEntry> getBadOmenEffects() {
        return findBadOmenEffects();
    }

    public EffectEntry getPreferredBadOmenEffect() {
        return preferredBadOmen(findBadOmenEffects());
    }

    @Override
    protected List<EffectEntry> getTooltipEffects() {
        EffectEntry preferredBadOmen = getPreferredBadOmenEffect();
        List<EffectEntry> result = new ArrayList<>();
        boolean addedBadOmen = false;
        for (EffectEntry entry : effects) {
            if (isBadOmen(entry)) {
                if (!addedBadOmen && entry == preferredBadOmen) {
                    result.add(entry);
                    addedBadOmen = true;
                }
                continue;
            }
            result.add(entry);
        }
        if (!addedBadOmen && preferredBadOmen != null) {
            result.add(preferredBadOmen);
        }
        return result;
    }

    private EffectEntry findEffect(ResourceLocation effectId) {
        for (EffectEntry entry : effects) {
            if (effectId.equals(entry.effectId)) {
                return entry;
            }
        }
        return null;
    }

    private List<EffectEntry> findBadOmenEffects() {
        List<EffectEntry> result = new ArrayList<>();
        for (EffectEntry entry : effects) {
            if (isBadOmen(entry)) {
                result.add(entry);
            }
        }
        return result;
    }

    private static boolean isBadOmen(EffectEntry entry) {
        return entry != null && entry.effectId != null && "bad_omen".equals(entry.effectId.getResourcePath());
    }

    private static EffectEntry preferredBadOmen(List<EffectEntry> entries) {
        EffectEntry fallback = null;
        EffectEntry raids = null;
        for (EffectEntry entry : entries) {
            if (!isBadOmen(entry) || ForgeRegistries.POTIONS.getValue(entry.effectId) == null) {
                continue;
            }
            String domain = entry.effectId.getResourceDomain();
            if ("deeperdepths".equals(domain)) {
                return entry;
            }
            if ("raids".equals(domain)) {
                raids = entry;
            } else if (fallback == null) {
                fallback = entry;
            }
        }
        return raids == null ? fallback : raids;
    }
}
