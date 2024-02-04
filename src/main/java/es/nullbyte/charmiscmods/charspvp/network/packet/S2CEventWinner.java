package es.nullbyte.charmiscmods.charspvp.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class S2CEventWinner {

    private final String winnername;
    private final UUID winneruuid;
    public S2CEventWinner(String name, UUID uuid) {
        this.winnername = name;
        this.winneruuid = uuid;
    }

    public S2CEventWinner(FriendlyByteBuf buf) {
        winnername = buf.readUtf();
        winneruuid = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(winnername);
        buf.writeUUID(winneruuid);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        //Get the reciever of the packet (player)
        es.nullbyte.charmiscmods.charspvp.timenpvpstate.GUI.LocalTimeState.winner = winnername;
        es.nullbyte.charmiscmods.charspvp.timenpvpstate.GUI.LocalTimeState.winnerid = winneruuid;


        ctx.setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
