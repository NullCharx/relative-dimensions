package es.nullbyte.relativedimensions.charspvp.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

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


    public void handle(CustomPayloadEvent.Context ctx) {

        //Get the reciever of the packet (player)
        Minecraft client = Minecraft.getInstance();
        long remainingTimeSeconds = es.nullbyte.relativedimensions.charspvp.GUI.LocalTimeState.dailyTL - remainingTime; //Calculate player remining time with local dauly limit
        if (remainingTime == 45296) { //Set untoggled time.
            remainingTimeSeconds = 45296;
        }
        //Get the playerTimeTracker
        es.nullbyte.relativedimensions.charspvp.GUI.LocalTimeState.localtimers.put(client.player.getUUID(), remainingTimeSeconds); //Syncronize

        ctx.setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
