package es.nullbyte.charmiscmods.items.network;

import es.nullbyte.charmiscmods.items.network.packet.coordPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class TransmatBeamHandler {
    private static SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MOD_ID, "transmatbeamhandler"))
            .clientAcceptedVersions((status,version)->true)
            .serverAcceptedVersions((status,version) -> true)
            .networkProtocolVersion(1)
                .simpleChannel();
    private static int ID = 0;

    private static int id() {
        return ID++;
    }

    public static void register() {

        INSTANCE.messageBuilder(coordPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(coordPacket::toBytes)
                .decoder(coordPacket::new)
                .consumerMainThread(coordPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with((player)));
    }

}
