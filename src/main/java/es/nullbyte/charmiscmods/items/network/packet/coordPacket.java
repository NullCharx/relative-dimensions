package es.nullbyte.charmiscmods.items.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class coordPacket {
    private final double x;
    private final double y;
    private final double z;

    public coordPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public coordPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public  void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        //Get the reciever of the packet (player)
        if (ctx.isServerSide()) {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                player.teleportTo(x, y, z); // Teleport player
                // Additional server-side logic if needed
            }
        }
        ctx.setPacketHandled(true);
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
