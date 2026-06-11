package xy177.brewinandchewinlegacy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BNCBasicWoodBlock extends Block {
    public BNCBasicWoodBlock() {
        super(Material.WOOD);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
    }
}
