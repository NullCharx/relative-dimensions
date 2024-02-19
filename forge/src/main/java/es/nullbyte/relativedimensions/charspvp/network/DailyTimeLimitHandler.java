package es.nullbyte.relativedimensions.charspvp.network;

import es.nullbyte.relativedimensions.charspvp.network.packet.S2CDailyTimeLimit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.ChannelBuilder;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class DailyTimeLimitHandler {
    private static SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MOD_ID, "resettimehandler"))
            .clientAcceptedVersions((status,version)->true)
            .serverAcceptedVersions((status,version) -> true)
            .networkProtocolVersion(1)
                .simpleChannel();
    private static int ID = 0;

    private static int id() {
        return ID++;
    }

    public static void register() {

        INSTANCE.messageBuilder(S2CDailyTimeLimit.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CDailyTimeLimit::toBytes)
                .decoder(S2CDailyTimeLimit::new)
                .consumerMainThread(S2CDailyTimeLimit::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.PLAYER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with((player)));
    }

}
