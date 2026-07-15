package xy177.brewinandchewinlegacy.client.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;
import xy177.brewinandchewinlegacy.common.gui.BNCKegContainer;
import xy177.brewinandchewinlegacy.common.recipe.BNCKegFluid;
import xy177.brewinandchewinlegacy.common.registry.BNCFluids;
import xy177.brewinandchewinlegacy.common.tile.BNCKegTileEntity;

public class BNCKegGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BrewinAndChewinLegacy.MODID, "textures/gui/keg.png");

    private final InventoryPlayer playerInventory;
    private final BNCKegTileEntity keg;

    public BNCKegGui(InventoryPlayer playerInventory, BNCKegTileEntity keg) {
        super(new BNCKegContainer(playerInventory, keg));
        this.playerInventory = playerInventory;
        this.keg = keg;
        xSize = 176;
        ySize = 166;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

        int relX = mouseX - guiLeft;
        int relY = mouseY - guiTop;
        if (relX >= 35 && relX < 77 && relY >= 54 && relY < 59) {
            drawHoveringText(Collections.singletonList(getTemperatureText()), mouseX, mouseY);
        } else if (relX >= 119 && relX < 146 && relY >= 15 && relY < 48) {
            drawHoveringText(Arrays.asList(getFluidName(), getFluidAmountText()), mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(keg.getDisplayName().getUnformattedText(), 38, 6, 4210752);
        fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int progress = ((BNCKegContainer) inventorySlots).getFermentProgressScaled();
        drawTexturedModalRect(guiLeft + 80, guiTop + 25, 176, 4, progress + 1, 18);

        int temperature = ((BNCKegContainer) inventorySlots).getKegTemperature();
        if (temperature == 1) {
            drawTexturedModalRect(guiLeft + 35, guiTop + 55, 176, 0, 8, 4);
        }
        if (temperature < 3) {
            drawTexturedModalRect(guiLeft + 43, guiTop + 55, 184, 0, 9, 4);
        }
        if (temperature > 3) {
            drawTexturedModalRect(guiLeft + 60, guiTop + 55, 201, 0, 9, 4);
        }
        if (temperature == 5) {
            drawTexturedModalRect(guiLeft + 69, guiTop + 55, 210, 0, 8, 4);
        }

        drawFluid();

        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft + 119, guiTop + 15, 176, 22, 27, 33);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    private String getTemperatureText() {
        switch (((BNCKegContainer) inventorySlots).getKegTemperature()) {
            case 1:
                return I18n.format("brewinandchewinlegacy.container.keg.cold");
            case 2:
                return I18n.format("brewinandchewinlegacy.container.keg.chilly");
            case 4:
                return I18n.format("brewinandchewinlegacy.container.keg.warm");
            case 5:
                return I18n.format("brewinandchewinlegacy.container.keg.hot");
            default:
                return I18n.format("brewinandchewinlegacy.container.keg.normal");
        }
    }

    private String getFluidName() {
        String fluidId = BNCFluids.idFromNetworkCode(((BNCKegContainer) inventorySlots).getFluidCode());
        switch (fluidId) {
            case BNCKegFluid.WATER:
                return I18n.format("brewinandchewinlegacy.fluid.water");
            case BNCKegFluid.MILK:
                return I18n.format("brewinandchewinlegacy.fluid.milk");
            case BNCKegFluid.HONEY:
                return I18n.format("brewinandchewinlegacy.fluid.honey");
            case BNCKegFluid.BEER:
                return I18n.format("brewinandchewinlegacy.fluid.beer");
            case BNCKegFluid.VODKA:
                return I18n.format("brewinandchewinlegacy.fluid.vodka");
            case BNCKegFluid.MEAD:
                return I18n.format("brewinandchewinlegacy.fluid.mead");
            case BNCKegFluid.RICE_WINE:
                return I18n.format("brewinandchewinlegacy.fluid.rice_wine");
            case BNCKegFluid.EGG_GROG:
                return I18n.format("brewinandchewinlegacy.fluid.egg_grog");
            case BNCKegFluid.STRONGROOT_ALE:
                return I18n.format("brewinandchewinlegacy.fluid.strongroot_ale");
            case BNCKegFluid.SACCHARINE_RUM:
                return I18n.format("brewinandchewinlegacy.fluid.saccharine_rum");
            case BNCKegFluid.BLOODY_MARY:
                return I18n.format("brewinandchewinlegacy.fluid.bloody_mary");
            case BNCKegFluid.STEEL_TOE_STOUT:
                return I18n.format("brewinandchewinlegacy.fluid.steel_toe_stout");
            case BNCKegFluid.GLITTERING_GRENADINE:
                return I18n.format("brewinandchewinlegacy.fluid.glittering_grenadine");
            case BNCKegFluid.PALE_JANE:
                return I18n.format("brewinandchewinlegacy.fluid.pale_jane");
            case BNCKegFluid.SALTY_FOLLY:
                return I18n.format("brewinandchewinlegacy.fluid.salty_folly");
            case BNCKegFluid.DREAD_NOG:
                return I18n.format("brewinandchewinlegacy.fluid.dread_nog");
            case BNCKegFluid.RED_RUM:
                return I18n.format("brewinandchewinlegacy.fluid.red_rum");
            case BNCKegFluid.WITHERING_DROSS:
                return I18n.format("brewinandchewinlegacy.fluid.withering_dross");
            case BNCKegFluid.GREEN_TEA:
                return I18n.format("brewinandchewinlegacy.fluid.green_tea");
            case BNCKegFluid.FLAXEN_CHEESE:
                return I18n.format("brewinandchewinlegacy.fluid.flaxen_cheese");
            case BNCKegFluid.SCARLET_CHEESE:
                return I18n.format("brewinandchewinlegacy.fluid.scarlet_cheese");
            default:
                FluidStack stack = BNCFluids.stackFor(fluidId, 1);
                return stack == null
                    ? I18n.format("brewinandchewinlegacy.fluid.empty")
                    : stack.getLocalizedName();
        }
    }

    private String getFluidAmountText() {
        return I18n.format("brewinandchewinlegacy.container.keg.fluid_amount", ((BNCKegContainer) inventorySlots).getFluidAmount());
    }

    private void drawFluid() {
        int amount = ((BNCKegContainer) inventorySlots).getFluidAmount();
        String fluidId = BNCFluids.idFromNetworkCode(((BNCKegContainer) inventorySlots).getFluidCode());
        FluidStack stack = BNCFluids.stackFor(fluidId, amount);
        if (stack == null) {
            return;
        }

        int width = 25;
        int height = Math.max(1, Math.min(31, amount * 31 / BNCKegFluid.CAPACITY));
        int x = guiLeft + 120;
        int y = guiTop + 16 + (31 - height);
        drawFluidStack(stack, x, y, width, height);
    }

    private void drawFluidStack(FluidStack stack, int x, int y, int width, int height) {
        TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(stack.getFluid().getStill(stack).toString());
        int color = stack.getFluid().getColor(stack);
        float alpha = ((color >> 24) & 255) / 255.0F;
        if (alpha <= 0.0F) {
            alpha = 1.0F;
        }
        GlStateManager.color(
            ((color >> 16) & 255) / 255.0F,
            ((color >> 8) & 255) / 255.0F,
            (color & 255) / 255.0F,
            alpha
        );
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        for (int drawX = 0; drawX < width; drawX += 16) {
            for (int drawY = 0; drawY < height; drawY += 16) {
                drawTexturedModalRect(x + drawX, y + drawY, sprite, Math.min(16, width - drawX), Math.min(16, height - drawY));
            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
