package xy177.brewinandchewinlegacy.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BNCTooltipItem extends Item {
    private final String tooltipKey;

    public BNCTooltipItem(String tooltipKey) {
        this.tooltipKey = tooltipKey;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (tooltipKey != null && !tooltipKey.isEmpty()) {
            tooltip.add(I18n.format(tooltipKey));
        }
    }
}
