package es.nullbyte.charmiscmods.events;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;
import static es.nullbyte.charmiscmods.PlayerTimeLimit.GUI.PVPStateOverlay.mainoverlay;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
            // Register the renderers
            event.registerAboveAll("pvpfullstate", mainoverlay);
        }
    }


}
