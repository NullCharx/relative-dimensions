package es.nullbyte.relativedimensions.forge;

import dev.architectury.platform.forge.EventBuses;
import es.nullbyte.relativedimensions.___AndRelativeDimensionsInMinecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(___AndRelativeDimensionsInMinecraft.MOD_ID)
public class AndRelativeDimensionsInMinecraftForge {
    public AndRelativeDimensionsInMinecraftForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(___AndRelativeDimensionsInMinecraft.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ___AndRelativeDimensionsInMinecraft.init();
    }
}