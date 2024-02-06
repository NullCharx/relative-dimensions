package es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network;

import es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.packet.C2SProximityStateReq;
import es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.packet.S2CProximityStateRes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class AberrantOreProxHandler {

    private static SimpleChannel PROXIMITY_REQUEST = ChannelBuilder.named(new ResourceLocation(MOD_ID, "aberrantoreproximityrequest"))
            .clientAcceptedVersions((status,version)->true)
            .serverAcceptedVersions((status,version) -> true)
            .networkProtocolVersion(1)
            .simpleChannel();
    private static SimpleChannel PROXIMITY_RESOLVER = ChannelBuilder.named(new ResourceLocation(MOD_ID, "aberrantoreproximityresolver"))
            .clientAcceptedVersions((status,version)->true)
            .serverAcceptedVersions((status,version) -> true)
            .networkProtocolVersion(1)
                .simpleChannel();
    private static int ID = 0;

    private static int id() {
        return ID++;
    }

    public static void register() {
        PROXIMITY_REQUEST.messageBuilder(C2SProximityStateReq.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SProximityStateReq::toBytes)
                .decoder(C2SProximityStateReq::new)
                .consumerMainThread(C2SProximityStateReq::handle)
                .add();

        PROXIMITY_RESOLVER.messageBuilder(S2CProximityStateRes.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CProximityStateRes::toBytes)
                .decoder(S2CProximityStateRes::new)
                .consumerMainThread(S2CProximityStateRes::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        PROXIMITY_REQUEST.send(message, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        PROXIMITY_RESOLVER.send(message, PacketDistributor.PLAYER.with((player)));
    }

}
