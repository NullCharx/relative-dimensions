package es.nullbyte.charmiscmods.PlayerTimeLimit.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PVPStateOverlay{
    //if you need custom textures they should be placed unde resources/assets/yourmodid/textures/gui (gui interchangable with other folders)
    //https://www.youtube.com/watch?v=J3a7JT0rxTM

    public static final IGuiOverlay mainoverlay = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //draw a grey semitransparent rectangle to the right of the screen
        GuiComponent.fill(poseStack, x, 0, width, height, 0x7F000000);
        //Draw text on top that writes the remaining time
        gui.getMinecraft().font.draw(poseStack, "HH:MM_SS", x, y, 0xFFFFFF);
        //Below the tezt, draw more text that says "PVP is active"
        gui.getMinecraft().font.draw(poseStack, "PVP is active", x, y + 10, 0xFFFFFF);
    });
}
