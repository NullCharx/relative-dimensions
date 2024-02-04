package es.nullbyte.charmiscmods.charspvp.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.function.Supplier;

public class Client2Server {

    public Client2Server() {

    }

    public Client2Server(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(CustomPayloadEvent.Context ctx) {
            //After recieving packet from client, do something
        Player player = ctx.getSender();
        assert player != null;
        player.sendSystemMessage(Component.literal(String.format("Packet from client to server received!º")));

        ctx.setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //ModMessages.sendToServer(new Client2Server());
}
