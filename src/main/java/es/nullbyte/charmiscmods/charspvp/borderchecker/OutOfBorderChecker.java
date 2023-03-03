package es.nullbyte.charmiscmods.charspvp.borderchecker;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OutOfBorderChecker {
    private final int warningTimeInSeconds = 5;

    private int  playertick = 0;
    private int playerseconds = 0;
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        WorldBorder worldBorder = player.getServer().overworld().getWorldBorder();
        if (event.phase == TickEvent.Phase.END && !event.player.isSpectator()) {
            // Get the player's current position
            BlockPos pos = event.player.getOnPos();

            // Check if the player is outside the world border
            if (!worldBorder.isWithinBounds(pos)) {
                // Calculate the nearest point within the bounds
                Vec3 playerPos = player.position(); // User position
                Vec3 center = worldBorder.getCenter(); // Border center
                double radius = worldBorder.getSize() / 2; // Border radius
                double angle = Math.atan2(center.z - playerPos.z, center.x - playerPos.x); // Angle between user position and border center
                Vec3 nearestBoundsPoint = new Vec3(center.x + radius * Math.cos(angle), playerPos.y, center.z + radius * Math.sin(angle)); // Calculate nearest point

                // Compute angle between player's direction and nearest point
                double playerYaw = Math.toRadians(player.yRot); // Convert to radians
                double xDiff = nearestBoundsPoint.x - playerPos.x;
                double zDiff = nearestBoundsPoint.z - playerPos.z;

                double angleToBorder = Math.toDegrees(Math.atan2(zDiff, xDiff)) - Math.toDegrees(playerYaw);

                // Computation has a 90 degrees offset. Fix below:
                angleToBorder = (angleToBorder + 360) % 360;
                angleToBorder = angleToBorder - 90;
                if (angleToBorder < 0) {
                    angleToBorder = 360 + angleToBorder;
                }

                // Set player's invulnerability status
                player.setInvulnerable(true);

            } else {
                // Set player's invulnerability status
                player.setInvulnerable(false);
            }
        }
    }

}

