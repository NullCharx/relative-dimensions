package es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet;

import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class S2CPVPTime {

    long remainingTime;

    public S2CPVPTime() {

    }

    public S2CPVPTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public S2CPVPTime(FriendlyByteBuf buf) {
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

        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
