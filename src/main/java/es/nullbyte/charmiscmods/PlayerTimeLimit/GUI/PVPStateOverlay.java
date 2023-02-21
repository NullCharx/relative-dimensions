package es.nullbyte.charmiscmods.PlayerTimeLimit.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.font.FontRenderContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class PVPStateOverlay{
    //if you need custom textures they should be placed unde resources/assets/yourmodid/textures/gui (gui interchangable with other folders)
    //https://www.youtube.com/watch?v=J3a7JT0rxTM
    //Refer to ClientEvents.java for the gui registration
    private static final int logoxoffset = 145;
    private static final int logoyoffset = -225;
    private static final int rextanglexstart= -35;
    private static final int rextangleystart = 13;
    private static final int rextanglexend= 60;
    private static final int rextangleyend = 70;
    private static final int textxoffset = 12;
    private static final int textyoffset = 20;
    private static final Font font = Minecraft.getInstance().font;

    private static int textX = 12;
    private static  int textY = 20;

    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/mainvisor/fhcmclogo.png");
    public static final IGuiOverlay HUD_BACKGROUND = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;


        // Draw semi-transparent grey rectangle
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 0.5F);
        GuiComponent.fill(poseStack,x + logoxoffset + rextanglexstart, y + logoyoffset + rextangleystart, x + logoxoffset + rextanglexend, y + logoyoffset + rextangleyend,0xFFFFFFFF);//Pos and then scale


    });

    public static final IGuiOverlay HUD_TEXTTIME = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        String timeRemaining = "Tiempo restante:";
        int textWidth = font.width(timeRemaining);
        textX = (x + logoxoffset + textxoffset - textWidth / 2);
        textY =(y + logoyoffset + textyoffset);
        GuiComponent.drawString(poseStack, font, timeRemaining, textX, textY, 0xFFFFFFFF);



    });

    public static final IGuiOverlay HUD_TEXTTIMECOUNT = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;


        // Draw "HH:MM:SS" text
        String remainingTime = LocalTime.ofSecondOfDay(PlayerTimeManager.getDailyTimeLimit() - PlayerTimeManager.getTracker(gui.getMinecraft().player.getUUID()).getSecsPlayed()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        int textWidth = font.width(remainingTime);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 2; // add some space between the two lines of text
        font.draw(poseStack, remainingTime, textX, textY, 0xFFFFFFFF);


    });

    public static final IGuiOverlay HUD_TEXTSTATE = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;


        // Draw PVP State text
        String pvpstate = "PVP:"; // replace with your logic to get the remaining time
        int textWidth = font.width(pvpstate);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 8; // add some space between the two lines of text
        GuiComponent.drawString(poseStack, font, pvpstate, textX, textY, 0xFFFFFFFF);

        // Draw "OFF/ON/HARDCORE" text
        String statePVP = "ULTRA"; // replace with your logic to get the remaining time
        textWidth = font.width(statePVP);
        textX = x + logoxoffset + textxoffset - textWidth / 2;
        textY += font.lineHeight + 2; // add some space between the two lines of text
        font.draw(poseStack, statePVP, textX, textY, 0xFFFFFFFF);

    });

    public static final IGuiOverlay HUD_LOGO = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;


        //Push the matrix to the stack, so that the scale and translation don't affect other elements
        //At the end, pop the matrix from the stack so that subsequent renders aren't affected!

        //Render the logo
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        GuiComponent.blit(poseStack, x + logoxoffset, y + logoyoffset, 0, 0, 25, 25, 25, 25);

    });
}
