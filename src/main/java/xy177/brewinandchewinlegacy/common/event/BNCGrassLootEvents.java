package xy177.brewinandchewinlegacy.common.event;

import com.wdcftgg.farmersdelightlegacy.common.item.ItemKnife;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.registry.BNCItems;

@Mod.EventBusSubscriber(modid = BrewinAndChewinLegacy.MODID)
public final class BNCGrassLootEvents {
    private BNCGrassLootEvents() {
    }

    @SubscribeEvent
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.getWorld().isRemote || !isGrass(event) || event.getHarvester() == null) {
            return;
        }
        if (!ItemKnife.isKnife(event.getHarvester().getHeldItemMainhand())) {
            return;
        }
        if (BNCItems.SWEET_BERRIES != null && event.getWorld().rand.nextFloat() < 0.3F) {
            event.getDrops().add(new ItemStack(BNCItems.SWEET_BERRIES));
        }
    }

    private static boolean isGrass(BlockEvent.HarvestDropsEvent event) {
        if (event.getState().getBlock() == Blocks.TALLGRASS) {
            BlockTallGrass.EnumType type = event.getState().getValue(BlockTallGrass.TYPE);
            return type == BlockTallGrass.EnumType.GRASS || type == BlockTallGrass.EnumType.FERN;
        }
        if (event.getState().getBlock() == Blocks.DOUBLE_PLANT) {
            return event.getState().getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS;
        }
        BlockPos pos = event.getPos();
        return pos != null && event.getWorld().getBlockState(pos).getBlock() == Blocks.TALLGRASS;
    }
}
