package es.nullbyte.charmiscmods.PlayerTimeLimit.network;

import es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet.S2CPVPState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class PVPStateHandler {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int id() {
        return ID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "pvpstatehandler"))
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .networkProtocolVersion(() -> "1.0")
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(S2CPVPState.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CPVPState::toBytes)
                .decoder(S2CPVPState::new)
                .consumerMainThread(S2CPVPState::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
