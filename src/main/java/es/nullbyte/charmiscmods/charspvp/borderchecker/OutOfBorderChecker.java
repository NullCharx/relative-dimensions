package es.nullbyte.charmiscmods.charspvp.borderchecker;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OutOfBorderChecker {
    private static int warningTimeInSeconds;

    private static int playerbrdrtick = 0;
    private static int playerbrdrseconds = 0;


    public OutOfBorderChecker(int warningTimeInSeconds) {
        OutOfBorderChecker.warningTimeInSeconds = warningTimeInSeconds;
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
        MinecraftForge.EVENT_BUS.addListener(OutOfBorderChecker::onServerTick);
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        for(Player player: event.getServer().getPlayerList().getPlayers()){
            WorldBorder worldBorder = player.level().getWorldBorder();
            if (event.phase == TickEvent.Phase.END && !player.isSpectator()) {
                // Get the player's current position and rotation
                BlockPos playerPos = player.getOnPos();
                float playerYaw = player.getRotationVector().y;
                double playerX = playerPos.getX() + 0.5;
                double playerZ = playerPos.getZ() + 0.5;

                // Check if the player is outside the world border
                if (!worldBorder.isWithinBounds(playerPos)) {
                    player.setInvulnerable(true);
                    if (playerbrdrtick == 0) {
                        playerbrdrtick++;
                        // Alert the player in chat
                        player.sendSystemMessage(Component.literal("¡Estas fuera de limites! Teletransportando dentro del área de juego en " + warningTimeInSeconds + " segundos..."));
                    } else {
                        playerbrdrtick++;
                        if (playerbrdrtick % 20 == 0) {
                            playerbrdrseconds++;
                            player.sendSystemMessage(Component.literal("¡Estas fuera de limites! Teletransportando dentro del área de juego en " + (warningTimeInSeconds-playerbrdrseconds) + " segundos..."));
                        } else if (playerbrdrseconds >= warningTimeInSeconds){
                            player.sendSystemMessage(Component.literal("Iniciando teletransporte..."));

                            // Calculate the point on the world border closest to the player
                            double distToBorder = worldBorder.getDistanceToBorder(player);
                            double borderX = playerX + Math.cos(Math.toRadians(playerYaw)) * distToBorder;
                            double borderZ = playerZ + Math.sin(Math.toRadians(playerYaw)) * distToBorder;

                            // Calculate the direction that the player is facing
                            double directionX = -Math.sin(Math.toRadians(playerYaw));
                            double directionZ = Math.cos(Math.toRadians(playerYaw));

                            // Calculate the offset from the player's current position
                            double xOffset = directionX * (distToBorder + 10);
                            double zOffset = directionZ * (distToBorder + 10);

                            // Calculate the new position
                            double newX = borderX + xOffset;
                            double newZ = borderZ + zOffset;

                            // Clamp the position to within the world border
                            newX = Math.max(worldBorder.getMinX() + 50, Math.min(worldBorder.getMaxX() - 50, newX));
                            newZ = Math.max(worldBorder.getMinZ() + 50, Math.min(worldBorder.getMaxZ() - 50, newZ));

                            BlockPos fullpos = new BlockPos((int)newX, 350, (int)newZ);
                            while (fullpos.getY() > -64 && player.level().isEmptyBlock(fullpos)) {
                                fullpos = fullpos.below();
                            }
                            fullpos = fullpos.above();

                            player.setPos(fullpos.getX(), fullpos.getY(), fullpos.getZ());
                            BlockPos currentpos = new BlockPos((int) player.position().x, (int) player.position().y, (int) player.position().z);
                            if(!currentpos.equals(fullpos)) {
                                player.setPos(fullpos.getX(), fullpos.getY(), fullpos.getZ());
                            }
                        }
                    }
                } else {
                    player.setInvulnerable(false);
                    playerbrdrseconds = 0;
                    playerbrdrtick = 0;
                }
            }
        }
    }

}