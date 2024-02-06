package es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.packet;

import es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.AberrantOreProxHandler;
import es.nullbyte.charmiscmods.blocks.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class C2SProximityStateReq {

    //Player current position coordinates
    private double x;
    private double y;
    private double z;

    private final int PROXIMITY_THRESHOLD = 3;

    public C2SProximityStateReq(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public C2SProximityStateReq(FriendlyByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        //After recieving packet from client, do something ALWAYS ON SERVER SIDE NO NEED TO CHECK
        ServerPlayer player = ctx.getSender();
        BlockPos pos = new BlockPos((int)x, (int)y, (int)z);

        //Check client or server side
        if (player != null && !player.level().isClientSide) {
            //Check in all directions in a PROXIMITY_THRESHOLD block radius for instances of ABERRANT_ORE
            //Return the distance to the closest block if found, -1 otherwise
            AberrantOreProxHandler.sendToPlayer(new S2CProximityStateRes(checkProximity(player, pos, PROXIMITY_THRESHOLD)), player);
        }
        ctx.setPacketHandled(true);
    }


    private int checkProximity(Player player, BlockPos playerPos, int proximityThreshold) {
        int closestDistance = proximityThreshold + 1; // Start with a distance larger than the threshold

        for (int x = -proximityThreshold; x <= proximityThreshold; x++) {
            for (int y = -proximityThreshold; y <= proximityThreshold; y++) {
                for (int z = -proximityThreshold; z <= proximityThreshold; z++) {
                    BlockPos checkPos = playerPos.offset(x, y, z);
                    if (player.level().getBlockState(checkPos).is(BlockInit.ABERRANT_ORE.get())) {
                        int distance;
                        if(x==0 && z==0){
                            distance = y;
                        } else {
                            distance = Math.min(Math.abs(x), Math.abs(z));
                        }
                        if (distance < closestDistance) {
                            closestDistance = distance; // Update the closest distance
                        }
                    }
                }
            }
        }

        return closestDistance <= proximityThreshold ? closestDistance : -1; // Return -1 if no block is within the threshold
    }
}
