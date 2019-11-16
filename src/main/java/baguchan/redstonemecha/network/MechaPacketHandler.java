package baguchan.redstonemecha.network;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.entity.MechaBaseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MechaPacketHandler {
    public static final String NETWORK_PROTOCOL = "2";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(RedstoneMechaCore.MODID, "net"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(MessageMechaSpaceKeyStat.class, 0)
                .encoder(MessageMechaSpaceKeyStat::writePacketData).decoder(MessageMechaSpaceKeyStat::readPacketData)
                .consumer(MessageMechaSpaceKeyStat.Handler::handle)
                .add();
    }

    public static void pushSpaceStart(MechaBaseEntity entity) {
        MechaPacketHandler.CHANNEL.sendToServer(new MessageMechaSpaceKeyStat(entity));
    }
}