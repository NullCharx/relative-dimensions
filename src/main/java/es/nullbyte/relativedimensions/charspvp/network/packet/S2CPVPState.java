package es.nullbyte.relativedimensions.charspvp.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

    public void handle(CustomPayloadEvent.Context ctx) {
        //Get the reciever of the packet (player)
        es.nullbyte.relativedimensions.charspvp.GUI.LocalTimeState.PVPstate = this.PVPstate;
        ctx.setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
