package xy177.brewinandchewinlegacy.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public class BNCFogParticle extends Particle {
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_0"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_1"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_2"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_3"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_4"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_5"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_6"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_7"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_8"),
        new ResourceLocation(BrewinAndChewinLegacy.MODID, "particle/fog_9")
    };

    public BNCFogParticle(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.particleGravity = 0.0F;
        this.particleAlpha = 0.55F;
        this.particleScale = 0.6F + this.rand.nextFloat() * 0.25F;
        this.particleMaxAge = 28 + this.rand.nextInt(12);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURES[0].toString());
        this.setParticleTexture(sprite);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.particleAge < this.particleMaxAge) {
            float life = (float) this.particleAge / (float) this.particleMaxAge;
            this.particleAlpha = 0.55F * (1.0F - life);
            int frame = Math.min(TEXTURES.length - 1, (int) (life * TEXTURES.length));
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURES[frame].toString());
            this.setParticleTexture(sprite);
        }
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
