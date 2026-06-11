package xy177.brewinandchewinlegacy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xy177.brewinandchewinlegacy.BrewinAndChewinLegacy;

public final class BNCNetwork {
    private static SimpleNetworkWrapper channel;

    private BNCNetwork() {
    }

    public static void init() {
        if (channel != null) {
            return;
        }
        channel = NetworkRegistry.INSTANCE.newSimpleChannel("bnclegacy");
        channel.registerMessage(SyncTipsyDamageMessage.Handler.class, SyncTipsyDamageMessage.class, 0, Side.CLIENT);
        channel.registerMessage(SyncRagingStacksMessage.Handler.class, SyncRagingStacksMessage.class, 1, Side.CLIENT);
        channel.registerMessage(SyncTipsyEffectMessage.Handler.class, SyncTipsyEffectMessage.class, 2, Side.CLIENT);
    }

    public static void sendTipsyDamage(EntityPlayerMP player, float damage, int ticks) {
        if (channel != null) {
            channel.sendTo(new SyncTipsyDamageMessage(damage, ticks), player);
        }
    }

    public static void sendTipsyEffect(EntityPlayerMP player, int duration, int amplifier) {
        if (channel != null) {
            channel.sendTo(new SyncTipsyEffectMessage(duration, amplifier), player);
        }
    }

    public static void sendRagingStacks(EntityLivingBase entity, int stacks) {
        if (channel == null) {
            return;
        }
        SyncRagingStacksMessage message = new SyncRagingStacksMessage(entity.getEntityId(), stacks);
        channel.sendToAllTracking(message, entity);
        if (entity instanceof EntityPlayerMP) {
            channel.sendTo(message, (EntityPlayerMP) entity);
        }
    }

    public static class SyncTipsyDamageMessage implements IMessage {
        private float damage;
        private int ticks;

        public SyncTipsyDamageMessage() {
        }

        public SyncTipsyDamageMessage(float damage, int ticks) {
            this.damage = damage;
            this.ticks = ticks;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            damage = buf.readFloat();
            ticks = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeFloat(damage);
            buf.writeInt(ticks);
        }

        public static class Handler implements IMessageHandler<SyncTipsyDamageMessage, IMessage> {
            @Override
            public IMessage onMessage(final SyncTipsyDamageMessage message, MessageContext ctx) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        BNCClientEffectState.setTipsyDamage(message.damage, message.ticks);
                    }
                });
                return null;
            }
        }
    }

    public static class SyncRagingStacksMessage implements IMessage {
        private int entityId;
        private int stacks;

        public SyncRagingStacksMessage() {
        }

        public SyncRagingStacksMessage(int entityId, int stacks) {
            this.entityId = entityId;
            this.stacks = stacks;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            stacks = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeInt(stacks);
        }

        public static class Handler implements IMessageHandler<SyncRagingStacksMessage, IMessage> {
            @Override
            public IMessage onMessage(final SyncRagingStacksMessage message, MessageContext ctx) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        BNCClientEffectState.setRagingStacks(message.entityId, message.stacks);
                    }
                });
                return null;
            }
        }
    }

    public static class SyncTipsyEffectMessage implements IMessage {
        private int duration;
        private int amplifier;

        public SyncTipsyEffectMessage() {
        }

        public SyncTipsyEffectMessage(int duration, int amplifier) {
            this.duration = duration;
            this.amplifier = amplifier;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            duration = buf.readInt();
            amplifier = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(duration);
            buf.writeInt(amplifier);
        }

        public static class Handler implements IMessageHandler<SyncTipsyEffectMessage, IMessage> {
            @Override
            public IMessage onMessage(final SyncTipsyEffectMessage message, MessageContext ctx) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        BNCClientEffectState.setTipsyEffect(message.duration, message.amplifier);
                    }
                });
                return null;
            }
        }
    }
}
