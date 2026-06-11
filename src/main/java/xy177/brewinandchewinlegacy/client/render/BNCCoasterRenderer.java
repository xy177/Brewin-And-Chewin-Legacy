package xy177.brewinandchewinlegacy.client.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import xy177.brewinandchewinlegacy.common.block.BNCCoasterBlock;
import xy177.brewinandchewinlegacy.common.tile.BNCCoasterTileEntity;

public class BNCCoasterRenderer extends TileEntitySpecialRenderer<BNCCoasterTileEntity> {
    private static final double[][] OFFSET_2 = {
        {-0.20D, -0.15D},
        {0.20D, 0.15D}
    };
    private static final double[][] OFFSET_3 = {
        {0.05D, 0.25D},
        {-0.25D, -0.15D},
        {0.25D, -0.25D}
    };
    private static final double[][] OFFSET_4 = {
        {0.20D, 0.25D},
        {-0.25D, 0.20D},
        {0.25D, -0.20D},
        {-0.20D, -0.25D}
    };
    private static final float[] ROTATION_2 = {190.0F, 10.0F};
    private static final float[] ROTATION_3 = {-20.0F, 220.0F, 100.0F};
    private static final float[] ROTATION_4 = {-5.0F, 265.0F, 85.0F, 175.0F};

    @Override
    public void render(BNCCoasterTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        List<ItemStack> stacks = getDisplayedStacks(tile);
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        boolean isCoaster = state.getBlock() instanceof BNCCoasterBlock;
        boolean invisible = isCoaster && state.getValue(BNCCoasterBlock.INVISIBLE);
        int size = isCoaster ? state.getValue(BNCCoasterBlock.SIZE) : stacks.size();
        Random random = new Random(tile.getPos().toLong());

        RenderHelper.enableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y, z + 0.5D);
        GlStateManager.rotate(-(360.0F / 16.0F) * tile.getRotationSegment(), 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-x - 0.5D, -y, -z - 0.5D);
        if (!invisible || stacks.isEmpty()) {
            renderCoasterBase(size, x, y, z);
        }
        for (int i = 0; i < stacks.size(); i++) {
            renderStack(stacks.get(i), stacks.size(), i, random, invisible, x, y, z);
        }
        GlStateManager.popMatrix();
    }

    private static void renderCoasterBase(int size, double x, double y, double z) {
        IBakedModel model = BNCCoasterModelRegistry.getBlockModel(size > 1 ? "coaster_tray" : "coaster");
        if (model == null) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
            .renderModelBrightnessColor(model, 1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private static List<ItemStack> getDisplayedStacks(BNCCoasterTileEntity tile) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ItemStack stack = tile.getStack(i);
            if (!stack.isEmpty()) {
                ItemStack copy = stack.copy();
                copy.setCount(1);
                stacks.add(copy);
            }
        }
        return stacks;
    }

    private static void renderStack(ItemStack stack, int count, int index, Random random, boolean invisible, double x, double y, double z) {
        double[] offset = offset(count, index);
        float rotation = rotation(count, index) + random.nextFloat() * 20.0F - 10.0F;
        List<IBakedModel> coasterModels = BNCCoasterModelRegistry.getModels(stack);

        GlStateManager.pushMatrix();
        double surfaceY = invisible ? 0.0D : 0.0625D;
        GlStateManager.translate(x + 0.5D + offset[0], y + surfaceY, z + 0.5D + offset[1]);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        if (coasterModels.isEmpty()) {
            GlStateManager.translate(0.0D, 0.0375D, 0.0D);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(0.42F, 0.42F, 0.42F);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        } else {
            GlStateManager.translate(-0.5D, 0.0D, -0.5D);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            for (int i = 0; i < coasterModels.size(); i++) {
                renderCoasterModel(stack, coasterModels.get(i), i);
            }
        }
        GlStateManager.popMatrix();
    }

    private static void renderCoasterModel(ItemStack stack, IBakedModel model, int modelIndex) {
        float red = 1.0F;
        float green = 1.0F;
        float blue = 1.0F;
        if (modelIndex == 1 && isPotionStack(stack)) {
            int color = PotionUtils.getColor(stack);
            red = (float)(color >> 16 & 255) / 255.0F;
            green = (float)(color >> 8 & 255) / 255.0F;
            blue = (float)(color & 255) / 255.0F;
        }

        // Avoid RenderItem here because its enchantment glint pass corrupts coaster block-space models.
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
            .renderModelBrightnessColor(model, 1.0F, red, green, blue);
    }

    private static boolean isPotionStack(ItemStack stack) {
        return stack.getItem() == Items.POTIONITEM
            || stack.getItem() == Items.SPLASH_POTION
            || stack.getItem() == Items.LINGERING_POTION;
    }

    private static double[] offset(int count, int index) {
        switch (count) {
            case 2:
                return OFFSET_2[index];
            case 3:
                return OFFSET_3[index];
            case 4:
                return OFFSET_4[index];
            default:
                return new double[] {0.0D, 0.0D};
        }
    }

    private static float rotation(int count, int index) {
        switch (count) {
            case 2:
                return ROTATION_2[index];
            case 3:
                return ROTATION_3[index];
            case 4:
                return ROTATION_4[index];
            default:
                return 0.0F;
        }
    }
}
