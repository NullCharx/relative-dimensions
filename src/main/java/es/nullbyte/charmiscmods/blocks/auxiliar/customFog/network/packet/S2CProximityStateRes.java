package es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.packet;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import static es.nullbyte.charmiscmods.CharMiscModsMain.USERFOGRENDERER;

public class S2CProximityStateRes {

    private final int blockUnits; //From 0 to PROXIMITY_THRESHOLD units (Defined in C2SProximityStateReq). -1 means no ore found
    private float fogStart0 = 0.5f;
    private float fogEnd0 = 2.0f;
    private float fogStart1 = 0.7f;
    private float fogEnd1 = 2.5f;
    private float fogStart2 = 1.0f;
    private float fogEnd2 = 3.0f;
    private float fogStart3 = 3.0f;
    private float fogEnd3 = 6.0f;


     public S2CProximityStateRes(int blockUnits) {
        this.blockUnits = blockUnits;
    }

    public S2CProximityStateRes(FriendlyByteBuf buf) {
        blockUnits = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
         buf.writeInt(blockUnits);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
         //Already on CLIENT SIDE only
        //Depending on the distance, set the custom fog rendering state density (start and end) and color

        // Check if the player is near an aberrant ore change this to update with distance
        //Change the local method to a packet that sends the distance to the server, and the server sends back the result
        if (ctx.isClientSide() && blockUnits > -1) {
            switch (blockUnits) {
                case 0:
                    //Very close. Start and end  should be very low
                    USERFOGRENDERER.setFogDensity(fogStart0, fogEnd0);
                    break;
                case 1:
                    USERFOGRENDERER.setFogDensity(fogStart1, fogEnd1);
                    break;
                case 2:
                    USERFOGRENDERER.setFogDensity(fogStart2, fogEnd2);
                    break;
                case 3:
                    USERFOGRENDERER.setFogDensity(fogStart3, fogEnd3);
                    break;
                default:
                    USERFOGRENDERER.setFogDensity(fogStart3, fogEnd3);
            }
            USERFOGRENDERER.setShouldRenderCustomFog(true);
        } else {
            USERFOGRENDERER.reset();
        }
        ctx.setPacketHandled(true);
    }

    //Send packet------------------------------------------------
    //Check if only server side:
    //RemainingTimeHandler.sendToClient(new SC2RemainingTime());
}
