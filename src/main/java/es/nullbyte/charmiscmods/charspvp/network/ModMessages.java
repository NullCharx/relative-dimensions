package es.nullbyte.charmiscmods.charspvp.network;

import es.nullbyte.charmiscmods.charspvp.network.packet.Client2Server;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.ChannelBuilder;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class ModMessages {
    private static SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MOD_ID, "main"))
            .clientAcceptedVersions((status,version)->true)
            .serverAcceptedVersions((status,version) -> true)
            .networkProtocolVersion(1)
            .simpleChannel();
    private static int ID = 0;

    private static int id() {
        return ID++;
    }

    public static void register() {

        INSTANCE.messageBuilder(Client2Server.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(Client2Server::toBytes)
                .decoder(Client2Server::new)
                .consumerMainThread(Client2Server::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.PLAYER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with((player)));
    }

}
