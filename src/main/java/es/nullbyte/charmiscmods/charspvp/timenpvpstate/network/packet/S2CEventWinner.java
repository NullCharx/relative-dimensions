package es.nullbyte.charmiscmods.charspvp.network.packet;

import es.nullbyte.charmiscmods.charspvp.GUI.LocalState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

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

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //Get the reciever of the packet (player)
            LocalState.winner = winnername;
            LocalState.winnerid = winneruuid;

        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
