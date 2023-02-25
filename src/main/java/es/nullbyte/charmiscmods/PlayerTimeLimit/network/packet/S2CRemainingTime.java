package es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet;

import es.nullbyte.charmiscmods.PlayerTimeLimit.GUI.LocalState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CRemainingTime {

    private final long remainingTime;

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
            long remainingTimeSeconds = LocalState.dailyTL - remainingTime; //Calculate player remining time with local dauly limit
            if (remainingTime == 45296) { //Set untoggled time.
                remainingTimeSeconds = 45296;
            }
            //Get the playerTimeTracker
            LocalState.localtimers.put(client.player.getUUID(), remainingTimeSeconds); //Syncronize


        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
