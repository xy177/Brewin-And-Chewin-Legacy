package xy177.brewinandchewinlegacy.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.registry.BNCEnchantments;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;
import xy177.brewinandchewinlegacy.common.registry.BNCOreDictionary;

@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID)
public final class BNCFishingEvents {
    private static final float PASSIVE_ALGAE_CHANCE = 0.25F;
    private static final float TURTLE_EGG_CHANCE = 0.05F;

    private BNCFishingEvents() {
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        ItemStack rod = getFishingRod(event);
        boolean algaeClearing = !rod.isEmpty() && EnchantmentHelper.getEnchantmentLevel(BNCEnchantments.ALGAE_CLEARING, rod) > 0;
        Random random = event.getEntityPlayer().world.rand;

        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack drop = event.getDrops().get(i);
            if (!isFish(drop)) {
                continue;
            }
            if (algaeClearing || random.nextFloat() < PASSIVE_ALGAE_CHANCE) {
                event.getDrops().set(i, randomAlgae(random, drop.getCount()));
            }
        }

        if (BNCItems.TURTLE_EGG != null && random.nextFloat() < TURTLE_EGG_CHANCE) {
            event.getDrops().add(new ItemStack(BNCItems.TURTLE_EGG));
        }
    }

    private static ItemStack getFishingRod(ItemFishedEvent event) {
        ItemStack main = event.getEntityPlayer().getHeldItemMainhand();
        if (!main.isEmpty() && main.getItem() == Items.FISHING_ROD) {
            return main;
        }
        ItemStack off = event.getEntityPlayer().getHeldItemOffhand();
        return !off.isEmpty() && off.getItem() == Items.FISHING_ROD ? off : ItemStack.EMPTY;
    }

    private static boolean isFish(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == Items.FISH;
    }

    private static ItemStack randomAlgae(Random random, int count) {
        List<Item> options = new ArrayList<>();
        Item kelp = BNCOreDictionary.getKelpItem();
        Item seaPickle = BNCOreDictionary.getSeaPickleItem();
        Item seagrass = BNCOreDictionary.getSeagrassItem();
        if (kelp != null) {
            options.add(kelp);
        }
        if (seaPickle != null) {
            options.add(seaPickle);
        }
        if (seagrass != null) {
            options.add(seagrass);
        }
        return options.isEmpty() ? ItemStack.EMPTY : new ItemStack(options.get(random.nextInt(options.size())), Math.max(1, count));
    }
}
