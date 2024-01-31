package es.nullbyte.charmiscmods.charspvp.network.packet;

import es.nullbyte.charmiscmods.charspvp.GUI.LocalState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CPVPState {

    int PVPstate;
    public S2CPVPState(int PVPstate) {
        this.PVPstate = PVPstate;
    }

    public S2CPVPState(FriendlyByteBuf buf) {
        PVPstate = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(PVPstate);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //Get the reciever of the packet (player)
            LocalState.PVPstate = this.PVPstate;
        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
