package es.nullbyte.charmiscmods.charspvp.PlayerTimeLimit.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Client2Server {

    public Client2Server() {

    }

    public Client2Server(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            //After recieving packet from client, do something
            Player player = ctx.get().getSender();
            player.sendSystemMessage(Component.literal(String.format("Packet from client to server received!º")));

        });
        ctx.get().setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only client side:
    //ModMessages.sendToServer(new Client2Server());
}
