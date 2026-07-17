package xy177.brewinandchewinlegacy.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;

public final class BNCFluidRenderer {
    private BNCFluidRenderer() {
    }

    public static void drawTank(Minecraft minecraft, FluidStack stack, int x, int y,
                                int width, int height, int capacity) {
        if (stack == null || stack.getFluid() == null || stack.amount <= 0 || capacity <= 0) {
            return;
        }
        int filledHeight = Math.max(1, Math.min(height, stack.amount * height / capacity));
        draw(minecraft, stack, x, y + height - filledHeight, width, filledHeight);
    }

    public static void draw(Minecraft minecraft, FluidStack stack, int x, int y, int width, int height) {
        if (stack == null || stack.getFluid() == null || width <= 0 || height <= 0) {
            return;
        }

        TextureAtlasSprite sprite = minecraft.getTextureMapBlocks()
            .getAtlasSprite(stack.getFluid().getStill(stack).toString());
        int color = stack.getFluid().getColor(stack);
        float alpha = ((color >> 24) & 255) / 255.0F;
        if (alpha <= 0.0F) {
            alpha = 1.0F;
        }

        minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.color(
            ((color >> 16) & 255) / 255.0F,
            ((color >> 8) & 255) / 255.0F,
            (color & 255) / 255.0F,
            alpha
        );
        for (int drawX = 0; drawX < width; drawX += 16) {
            for (int drawY = 0; drawY < height; drawY += 16) {
                drawSprite(sprite, x + drawX, y + drawY,
                    Math.min(16, width - drawX), Math.min(16, height - drawY));
            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void drawSprite(TextureAtlasSprite sprite, int x, int y, int width, int height) {
        double maxU = sprite.getMinU() + (sprite.getMaxU() - sprite.getMinU()) * width / 16.0D;
        double maxV = sprite.getMinV() + (sprite.getMaxV() - sprite.getMinV()) * height / 16.0D;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0D).tex(sprite.getMinU(), maxV).endVertex();
        buffer.pos(x + width, y + height, 0.0D).tex(maxU, maxV).endVertex();
        buffer.pos(x + width, y, 0.0D).tex(maxU, sprite.getMinV()).endVertex();
        buffer.pos(x, y, 0.0D).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
        tessellator.draw();
    }
}
