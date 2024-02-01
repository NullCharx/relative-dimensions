package es.nullbyte.charmiscmods.charspvp.timenpvpstate.network;

import es.nullbyte.charmiscmods.charspvp.timenpvpstate.network.packet.S2CRemainingTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.ChannelBuilder;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class RemainingTimeHandler {

    private static int ID = 0;

    private static int id() {
        return ID++;
    }
    private static SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MOD_ID, "remainingtimehandler"))
            .clientAcceptedVersions((status,version)->true)
            .serverAcceptedVersions((status,version) -> true)
            .networkProtocolVersion(1)
            .simpleChannel();
    public static void register() {


        INSTANCE.messageBuilder(S2CRemainingTime.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CRemainingTime::toBytes)
                .decoder(S2CRemainingTime::new)
                .consumerMainThread(S2CRemainingTime::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with((player)));
    }

}
