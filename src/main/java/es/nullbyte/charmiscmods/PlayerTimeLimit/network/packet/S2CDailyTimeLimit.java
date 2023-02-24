package es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet;

import es.nullbyte.charmiscmods.PlayerTimeLimit.GUI.LocalState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class S2CDailyTimeLimit {

    private final long localTimeLimit;

     public S2CDailyTimeLimit(long localTimeLimit) {
        this.localTimeLimit = localTimeLimit;
    }

    public S2CDailyTimeLimit(FriendlyByteBuf buf) {
        localTimeLimit = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {buf.writeLong(localTimeLimit);

    }

    //TODO: MAke a new packet to syncornize the timelimit to the clients (done, mpw syncronize on logon)
    //Synchonize it too when the player logs in
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            LocalState.dailyTL = localTimeLimit;

        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
