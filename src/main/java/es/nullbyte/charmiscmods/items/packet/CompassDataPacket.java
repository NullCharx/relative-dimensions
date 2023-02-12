package es.nullbyte.charmiscmods.items.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class CompassDataPacket implements Packet {
    private double distanceToItemUser;

    public CompassDataPacket() {}

    public CompassDataPacket(double distanceToItemUser) {
        this.distanceToItemUser = distanceToItemUser;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.distanceToItemUser);
    }

    @Override
    public void handle(PacketListener packetListener) {
        //The data needs to return to the compass class

    }
}
