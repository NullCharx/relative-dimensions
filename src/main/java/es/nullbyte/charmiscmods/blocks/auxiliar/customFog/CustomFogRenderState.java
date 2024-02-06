package es.nullbyte.charmiscmods.blocks.auxiliar.customFog;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.AberrantOreProxHandler;
import es.nullbyte.charmiscmods.blocks.auxiliar.customFog.network.packet.C2SProximityStateReq;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CustomFogRenderState {
    // Indicates whether custom fog rendering should be applied
    private static boolean shouldRenderCustomFog = false;

    // Custom fog color components

    //TARDIS BLUE FOG
    private static float tardisRed = 0.0f;
    private static float tardisGreen = 0.2f;
    private static float tardisBlue = 0.749f;

    //FOg desnisties

    private float defaultRed = 1.0f;
    private float defaultGreen = 1.0f;
    private float defaultBlue = 1.0f;
    private float defaultStart = 1.0f;
    private float defaultEnd = 1.0f;
    private FogShape defaultShape = FogShape.CYLINDER;
    public CustomFogRenderState() {
        defaultRed = RenderSystem.getShaderFogColor()[0];
        defaultGreen = RenderSystem.getShaderFogColor()[1];
        defaultBlue = RenderSystem.getShaderFogColor()[2];
        defaultStart = RenderSystem.getShaderFogStart();
        defaultEnd = RenderSystem.getShaderFogEnd();
        defaultShape = RenderSystem.getShaderFogShape();
        reset();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onFogDensity(ViewportEvent.RenderFog event) {
        // Your logic to determine if custom fog should be applied
        if (shouldRenderCustomFog()) {
            RenderSystem.setShaderFogColor(tardisRed,tardisGreen,tardisBlue);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        }
        System.out.println("Fog density event");
    }

    @SubscribeEvent
    public static void onFogColors(ViewportEvent.ComputeFogColor event) {
        // Get the active render view entity, which is usually the player
        ;

        if (event.getCamera().getEntity() != null && event.getCamera().getEntity() instanceof Player) {
            Player player = (Player) event.getCamera().getEntity();

            // Now you have access to the player's position
            BlockPos playerPos = new BlockPos((int) player.getX(),(int) player.getY(), (int) player.getZ());

            //Send packet to server to check if player is near an aberrant ore
            AberrantOreProxHandler.sendToServer(new C2SProximityStateReq(playerPos.getX(), playerPos.getY(), playerPos.getZ()));
        }
        System.out.println("Fog color event");
    }

    private boolean checkNearbyAberrantOre(BlockPos playerPos) {
        // Your logic to determine if the player is near aberrant ore
        //Send a packet to the server. The server will check if the player is five blocks away or less in any direction from an aberrant ore
        //Then send back the packet to the client with the resul, which will be used to set the shouldRenderCustomFog variable
        //Maybe is not necessary to return anything, as the server will send the packet to the client, and the client will set the shouldRenderCustomFog variable
        //This class must probably be defined at the main mod class, ONLY on the client side

        //Probably have the fog be thicker the closer the player is to the ore
        return false;
    }
    public static boolean shouldRenderCustomFog() {
        return shouldRenderCustomFog;
    }

    public void setShouldRenderCustomFog(boolean shouldRenderCustomFog) {
        CustomFogRenderState.shouldRenderCustomFog = shouldRenderCustomFog;
    }


    public float getRed() {
        return RenderSystem.getShaderFogColor()[0];
    }

    public float getGreen() {
        return RenderSystem.getShaderFogColor()[1];
    }

    public float getBlue() {
        return RenderSystem.getShaderFogColor()[2];
    }

    public float getStart() {
        return RenderSystem.getShaderFogStart();
    }

    public float getEnd() {
        return RenderSystem.getShaderFogEnd();
    }
    public float getShape() {
        return RenderSystem.getShaderFogShape().ordinal();
    }

    public void setFogDensity(float start, float end) {
        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(end);
    }

    // Reset to default state
    public void reset() {
        shouldRenderCustomFog = false;
        RenderSystem.setShaderFogShape(defaultShape);
        RenderSystem.setShaderFogStart(defaultStart);
        RenderSystem.setShaderFogEnd(defaultEnd);
        RenderSystem.setShaderFogColor(defaultRed,defaultGreen,defaultBlue);


    }
}
