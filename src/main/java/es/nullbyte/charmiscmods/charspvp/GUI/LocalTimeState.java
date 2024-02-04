package es.nullbyte.charmiscmods.charspvp.timenpvpstate.GUI;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Timer and PVP state recieved. It is only for rendering purposes and does not change the actual time nor
 * the PVP state of the server.
 */
@OnlyIn(Dist.CLIENT)
public class LocalTimeState {
    public static int PVPstate = -1; //-1 PVP off, 0 PVP on, 1 ULTRA
    public static long dailyTL = 4*3600;
    public static String winner = "";
    public static UUID winnerid = new UUID(0,0);

    public static final Map<UUID, Long> localtimers = new HashMap<>();//Hashmap of individual player times that are
    //accessed on-render time.

}
