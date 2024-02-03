package es.nullbyte.charmiscmods.items.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class tpCoordsPacket {
    private final double x;
    private final double y;
    private final double z;
    private final int targetId; //0 for teleporting, 1 for particle effect initial position, 2 for particle effect final position

    public tpCoordsPacket(double x, double y, double z, int targetId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetId = targetId;
    }

    public tpCoordsPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.targetId = buf.readInt();
    }

    public  void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(targetId);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        //Get the reciever of the packet (player)
        if (ctx.isServerSide()) {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                switch (targetId) {
                    case 0:
                        player.teleportTo(x, y, z); // Teleport player
                        break;
                    case 1:
                        // Additional server-side logic if needed
                        break;
                    case 2:
                        // Additional server-side logic if needed
                        break;
                }
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
