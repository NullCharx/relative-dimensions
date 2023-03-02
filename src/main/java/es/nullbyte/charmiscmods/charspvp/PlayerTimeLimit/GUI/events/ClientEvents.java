package es.nullbyte.charmiscmods.charspvp.PlayerTimeLimit.GUI.events;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import es.nullbyte.charmiscmods.charspvp.PlayerTimeLimit.GUI.PVPStateOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    //@Mod.EventBusSubscriber(modid = CharMiscModsMain.MOD_ID, value = Dist.CLIENT)
   // public static class ClientForgeEvents {

   // }

    @Mod.EventBusSubscriber(modid = CharMiscModsMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {


        //Both the id of the hud and the path to gui resource must contain no uppercase letters or speciañ characters other than underscore
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("pvpbackgroundov", PVPStateOverlay.HUD_BACKGROUND);
            event.registerAboveAll("pvptexttimeov", PVPStateOverlay.HUD_TEXTTIME);
            event.registerAboveAll("pvptexttimectrov", PVPStateOverlay.HUD_TEXTTIMECOUNT);
            event.registerAboveAll("pvptextstateov", PVPStateOverlay.HUD_TEXTSTATE);
            event.registerAboveAll("pvplogoov", PVPStateOverlay.HUD_LOGO);
        }
    }
}