package es.nullbyte.charmiscmods.PlayerTimeLimit.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.font.FontRenderContext;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class PVPStateOverlay{
    //if you need custom textures they should be placed unde resources/assets/yourmodid/textures/gui (gui interchangable with other folders)
    //https://www.youtube.com/watch?v=J3a7JT0rxTM
    //Refer to ClientEvents.java for the gui registration
    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/mainvisor/fhcmclogo.png");
    public static final IGuiOverlay HUD_MAINOVERLAY = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;
        int logoxoffset = 145;
        int logoyoffset = -200;
        int rectanglexoffset = -35;
        int rectangleyoffset = 0;
        int componentWidth = 60;
        int componentHeight = 80;
        int textxoffset = 12;
        int textyoffset = 20;

        final double scaled = 1.f / gui.getMinecraft().getWindow().getGuiScale();
        final float scale = (float) scaled;
        poseStack.scale(scale, scale, scale);

        // Draw semi-transparent grey rectangle
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 0.5F);
        GuiComponent.fill(poseStack, x + logoxoffset + rectanglexoffset, y + logoyoffset + rectangleyoffset, x + logoxoffset + componentWidth, y + logoyoffset + componentHeight, 0xFFFFFFFF);

        // Draw "Time remaining" text
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        String timeRemaining = "Tiempo restante:";
        int textWidth = font.width(timeRemaining);
        int textX = (x + logoxoffset + textxoffset - textWidth / 2);
        int textY =(y + logoyoffset + textyoffset);
        GuiComponent.drawString(poseStack, font, timeRemaining, textX, textY, 0xFFFFFFFF);

        // Draw "HH:MM:SS" text
        String remainingTime = "HH:MM:SS"; // replace with your logic to get the remaining time
        textWidth = font.width(remainingTime);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 2; // add some space between the two lines of text
        font.draw(poseStack, remainingTime, textX, textY, 0xFFFFFFFF);

        // Draw PVP State text
        String pvpstate = "PVP:"; // replace with your logic to get the remaining time
        textWidth = font.width(pvpstate);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 8; // add some space between the two lines of text
        font.draw(poseStack, pvpstate, textX, textY, 0xFFFFFFFF);

        // Draw "OFF/ON/HARDCORE" text
        String statePVP = "HARDCORE"; // replace with your logic to get the remaining time
        textWidth = font.width(statePVP);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 2; // add some space between the two lines of text
        font.draw(poseStack, statePVP, textX, textY, 0xFFFFFFFF);

        //Render the logo
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        GuiComponent.blit(poseStack, x + logoxoffset, y + logoyoffset, 0, 0, 25, 25,
                25, 25);

    });
}
