package es.nullbyte.charmiscmods.charspvp.borderchecker;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OutOfBorderChecker {
    private final int warningTimeInSeconds = 5;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        MinecraftServer server = player.getServer();
        WorldBorder worldBorder = player.getServer().overworld().getWorldBorder();
        if (event.phase == TickEvent.Phase.END && !event.player.isSpectator()) {
            // Get the player's current position
            BlockPos pos = event.player.getOnPos();

            // Check if the player is outside the world border
            if (!worldBorder.isWithinBounds(pos)) {
                // Alert the player in chat
                player.sendSystemMessage(Component.literal("You are outside the world border! Teleporting in " + warningTimeInSeconds + " seconds..."));

                // Start the countdown
                CountDownLatch latch = new CountDownLatch(1);
                for (int i = warningTimeInSeconds; i >= 1; i--) {
                    int countdownTime = i;
                    server.submit(() -> {
                        // Send the countdown message to the player in chat
                        event.player.sendMessage(new LiteralText("Teleporting in " + countdownTime + " seconds...").formatted(Formatting.RED));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Teleport the player to the closest location inside the world border
                server.submit(() -> {
                    event.player.teleport(worldBorder.getClosestPoint(pos));
                    event.player.sendMessage(new LiteralText("You have been teleported to the closest location inside the world border!").formatted(Formatting.GREEN));
                    latch.countDown();
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
}
