package es.nullbyte.charmiscmods.PlayerTimeLimit;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Timer and PVP state recieved. It is only for rendering purposes and does not change the actual time counted by the server, nor
 * the PVP state of the server.
 */
@OnlyIn(Dist.CLIENT)
public class LocalState {
    public static int PVPstate = 0; //-1 PVP off, 0 PVP on, 1 ULTRA
    public static final Map<UUID, Long> localtimers = new HashMap<>();//Hashmap of individual player trackers


}
