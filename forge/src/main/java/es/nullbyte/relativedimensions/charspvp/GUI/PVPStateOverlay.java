package es.nullbyte.relativedimensions.charspvp.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class PVPStateOverlay{
    //if you need custom textures they should be placed unde resources/assets/yourmodid/textures/gui (gui interchangable with other folders)
    //https://www.youtube.com/watch?v=J3a7JT0rxTM
    //Refer to ClientEvents.java for the gui registration
    private static final int logoxoffset = 285;
    private static final int logoyoffset = -150;
    private static final int rextanglexstart= -35; //+30
    private static final int rextangleystart = 13;
    private static final int rextanglexend= 60; //+30
    private static final int rextangleyend = 70;
    private static final int textxoffset = 12;
    private static final int textyoffset = 20;

    private static int textX = 12;
    private static  int textY = 20;

    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/mainvisor/fhcmclogo.png");
    public static final IGuiOverlay PVP_STATE_HUD = ((forgeGui, guiGraphics, partialTick, width, height) -> {
        int x = width / 2;
        PoseStack poseStack = guiGraphics.pose();
        //Push the matrix to the stack, so that the scale and translation don't affect other elements
        //At the end, pop the matrix from the stack so that subsequent renders aren't affected!
        poseStack.pushPose();
        poseStack.scale(0.8F,0.8F,0.8F);

        //Text tiempo restante
        Font font = Minecraft.getInstance().font;
        Component timeRemaining = Component.translatable("gui.pvpoverlay.time_remaining");
        int textWidth = font.width(timeRemaining);
        textX = (x + logoxoffset + textxoffset - textWidth / 2);
        textY =(height + logoyoffset + textyoffset);
        guiGraphics.drawString(font, timeRemaining, textX, textY, 0xFFFFFFFF);


        // Draw "HH:MM:SS" text
        long timer = es.nullbyte.relativedimensions.charspvp.GUI.LocalTimeState.localtimers.get(Minecraft.getInstance().player.getUUID());
        String remainingTime = LocalTime.ofSecondOfDay(timer).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        int color;
        if (timer >= 3600 && timer != 86399){
            color = 0xFFFFFFFF; //Blanco
        } else if (timer >= 1800){
            color = 0xFFFAFF83; //Amarillo muy claro
        }else if (timer >= 900){
            color = 0xFFF9D71C; //Amarillo
        }else if (timer >= 300){
            color = 0xFFFFFF00; //Naranha
        } else if (timer >= 60){
            color = 0xFFFF0000; //Red
        } else {
            color = 0xFF0B0054;
        }
        textWidth = font.width(remainingTime);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 2; // add some space between the two lines of text
        guiGraphics.drawString(font, remainingTime, textX, textY, color);
        //font.draw(poseStack, remainingTime, textX, textY, color);


        // Draw PVP State text
        String pvpstate = "PVP:"; // replace with your logic to get the remaining time
        textWidth = font.width(pvpstate);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 8; // add some space between the two lines of text
        guiGraphics.drawString(font, pvpstate, textX, textY, 0xFFFFFFFF);


        //Draw the pvp state itself
        int intstate = es.nullbyte.relativedimensions.charspvp.GUI.LocalTimeState.PVPstate;
        String statePVP; // replace with your logic to get the remaining time
        if (intstate ==-1) {
            statePVP = "OFF";
            color = 0xFF00AA00;
        } else if (intstate == 0){
            statePVP = "ON";
            color = 0xFFAA0000;
        } else {
            statePVP = "ULTRA";
            color = 0xFFAA00AA;
        }
        textWidth = font.width(statePVP);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 2; // add some space between the two lines of text
        guiGraphics.drawString(font, statePVP, textX, textY, color);
        //font.draw(poseStack, statePVP, textX, textY, color);

        // Draw semi-transparent grey rectangle
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 0.5F);
        guiGraphics.fill(x + logoxoffset + rextanglexstart, height + logoyoffset + rextangleystart, x + logoxoffset + rextanglexend, height + logoyoffset + rextangleyend,0xFFFFFFFF);//Pos and then scale

        //Render the logo
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        //Make a guiGraphics.blit call to render the texture located in resources/assets/relativedimensions/textures/ainvisor/fhcmclogo.png
        guiGraphics.blit(TEXTURE, x + logoxoffset, height + logoyoffset, 0, 0, 25, 25, 25, 25);
        poseStack.popPose();
    });
}
