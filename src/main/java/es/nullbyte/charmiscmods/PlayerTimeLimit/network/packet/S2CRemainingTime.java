package es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet;

import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class S2CRemainingTime {

    long remainingTime;

    public S2CRemainingTime() {

    }

    public S2CRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public S2CRemainingTime(FriendlyByteBuf buf) {
        remainingTime = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(remainingTime);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //Get the reciever of the packet (player)
            Minecraft client = Minecraft.getInstance();
            assert client.player != null;
            Long timeLimit = PlayerTimeManager.getDailyTimeLimit();
            long remainingTimeSeconds = timeLimit - remainingTime;
            LocalTime remainingTime = LocalTime.ofSecondOfDay(remainingTimeSeconds);
            String formattedRemainingTime = remainingTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            client.player.sendSystemMessage(Component.literal(String.format("Remaining time: " + formattedRemainingTime)));

        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
