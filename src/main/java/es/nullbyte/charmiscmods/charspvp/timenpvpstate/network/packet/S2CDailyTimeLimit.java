package es.nullbyte.charmiscmods.charspvp.timenpvpstate.network.packet;

import es.nullbyte.charmiscmods.charspvp.GUI.LocalState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

    public void handle(CustomPayloadEvent.Context ctx) {

        LocalState.dailyTL = localTimeLimit;

        ctx.setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
