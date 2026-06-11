package xy177.brewinandchewinlegacy.common.registry;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public final class BNCVillagerTrades {
    private static boolean registered;

    private BNCVillagerTrades() {
    }

    public static void register() {
        if (registered || VillagerRegistry.FARMER == null) {
            return;
        }
        registered = true;

        VillagerRegistry.VillagerCareer fisherman = VillagerRegistry.FARMER.getCareer(1);
        Item kelp = BNCOreDictionary.getKelpItem();
        Item seaPickle = BNCOreDictionary.getSeaPickleItem();
        Item seagrass = BNCOreDictionary.getSeagrassItem();
        if (kelp != null) {
            fisherman.addTrade(1, new EmeraldForItemTrade(kelp, 6));
        }
        if (seaPickle != null) {
            fisherman.addTrade(2, new EmeraldForItemTrade(seaPickle, 4));
        }
        if (seagrass != null) {
            fisherman.addTrade(1, new EmeraldForItemTrade(seagrass, 8));
        }
    }

    private static final class EmeraldForItemTrade implements EntityVillager.ITradeList {
        private final Item item;
        private final int count;

        private EmeraldForItemTrade(Item item, int count) {
            this.item = item;
            this.count = count;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD), new ItemStack(item, count)));
        }
    }
}
