package es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet;

import es.nullbyte.charmiscmods.PlayerTimeLimit.GUI.LocalState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CPVPTime {

    int PVPstate;

    public S2CPVPTime() {

    }

    public S2CPVPTime(int remainingTime) {
        this.PVPstate = remainingTime;
    }

    public S2CPVPTime(FriendlyByteBuf buf) {
        PVPstate = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(PVPstate);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //Get the reciever of the packet (player)
            LocalState.PVPstate = PVPstate;
        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
