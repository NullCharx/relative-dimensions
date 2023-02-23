package es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet;

import es.nullbyte.charmiscmods.PlayerTimeLimit.GUI.LocalState;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeTracker;
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

    //TODO: MAke a new packet to syncornize the timelimit to the clients
    //Synchonize it too when the player logs in
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //Get the reciever of the packet (player)
            Minecraft client = Minecraft.getInstance();
            long timeLimit = PlayerTimeManager.getDailyTimeLimit();
            long remainingTimeSeconds = timeLimit - remainingTime;

            //Get the playerTimeTracker
            PlayerTimeTracker trck = PlayerTimeManager.getTracker(client.player.getUUID());
            LocalState.localtimers.put(client.player.getUUID(), remainingTimeSeconds);


        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
