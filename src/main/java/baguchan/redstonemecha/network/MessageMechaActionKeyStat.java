package baguchan.redstonemecha.network;

import baguchan.redstonemecha.entity.MechaBaseEntity;
import baguchan.redstonemecha.init.GearTypeRegister;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMechaActionKeyStat {
    private int entityID;
    private int inventoryID;

    public MessageMechaActionKeyStat() {
    }

    public MessageMechaActionKeyStat(int entityID, int inventoryID) {
        this.entityID = entityID;
        this.inventoryID = inventoryID;
    }

    public MessageMechaActionKeyStat(MechaBaseEntity entityIn, int inventoryID) {
        this.entityID = entityIn.getEntityId();
        this.inventoryID = inventoryID;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static MessageMechaActionKeyStat readPacketData(PacketBuffer buf) {
        int entityId = buf.readVarInt();
        int inventoryID = buf.readInt();
        return new MessageMechaActionKeyStat(entityId, inventoryID);
    }

    public void writePacketData(PacketBuffer buffer) {
        buffer.writeVarInt(this.entityID);
        buffer.writeInt(this.inventoryID);
    }


    public static class Handler {
        public static void handle(MessageMechaActionKeyStat message, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            ctx.get().enqueueWork(() -> {

                Entity entity = ctx.get().getSender().getServerWorld().getEntityByID(message.entityID);
                if (entity instanceof MechaBaseEntity) {
                    MechaBaseEntity mechaBaseEntity = (MechaBaseEntity) entity;

                    if (message.inventoryID < mechaBaseEntity.getInventory().getSizeInventory()) {
                        if (!mechaBaseEntity.getInventory().getStackInSlot(message.inventoryID).isEmpty()) {
                            if (GearTypeRegister.getGear(mechaBaseEntity.getInventory().getStackInSlot(message.inventoryID)) != null) {
                                GearTypeRegister.getGear(mechaBaseEntity.getInventory().getStackInSlot(message.inventoryID))
                                        .onPushActionKey(mechaBaseEntity);
                            }
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}