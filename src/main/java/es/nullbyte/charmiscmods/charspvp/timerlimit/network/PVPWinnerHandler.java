package es.nullbyte.charmiscmods.charspvp.timerlimit.network;

import es.nullbyte.charmiscmods.charspvp.network.packet.S2CEventWinner;
import es.nullbyte.charmiscmods.charspvp.network.packet.S2CPVPState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class PVPWinnerHandler {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int id() {
        return ID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "pvpwinnerhandler"))
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .networkProtocolVersion(() -> "1.0")
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(S2CEventWinner.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CEventWinner::toBytes)
                .decoder(S2CEventWinner::new)
                .consumerMainThread(S2CEventWinner::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
