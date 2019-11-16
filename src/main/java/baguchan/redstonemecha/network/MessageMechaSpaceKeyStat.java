package baguchan.redstonemecha.network;

import baguchan.redstonemecha.entity.MechaBaseEntity;
import baguchan.redstonemecha.init.GearTypeRegister;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMechaSpaceKeyStat {
    private int entityID;

    public MessageMechaSpaceKeyStat() {
    }

    public MessageMechaSpaceKeyStat(int entityID) {
        this.entityID = entityID;
    }

    public MessageMechaSpaceKeyStat(MechaBaseEntity ravagerEntityIn) {
        this.entityID = ravagerEntityIn.getEntityId();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static MessageMechaSpaceKeyStat readPacketData(PacketBuffer buf) {
        return new MessageMechaSpaceKeyStat(buf.readVarInt());
    }

    public void writePacketData(PacketBuffer buffer) {
        buffer.writeVarInt(this.entityID);
    }


    public static class Handler {
        public static void handle(MessageMechaSpaceKeyStat message, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            ctx.get().enqueueWork(() -> {

                Entity entity = ctx.get().getSender().getServerWorld().getEntityByID(message.entityID);
                if (entity instanceof MechaBaseEntity) {
                    MechaBaseEntity mechaBaseEntity = (MechaBaseEntity) entity;

                    if (!mechaBaseEntity.getInventory().getStackInSlot(1).isEmpty()) {
                        if (GearTypeRegister.getGear(mechaBaseEntity.getInventory().getStackInSlot(1)) != null) {
                            GearTypeRegister.getGear(mechaBaseEntity.getInventory().getStackInSlot(1))
                                    .onPushSpaceKey(mechaBaseEntity);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}