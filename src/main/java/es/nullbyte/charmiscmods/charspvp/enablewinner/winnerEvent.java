package es.nullbyte.charmiscmods.charspvp.enablewinner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.logging.Level;

@Mod.EventBusSubscriber
public class winnerEvent {

    public static boolean isWinnerSet = false;
    public static ServerPlayer player;
    private static int fireworktick = 0;

    public winnerEvent() {
        MinecraftForge.EVENT_BUS.addListener(winnerEvent::onServerTick);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (isWinnerSet && player != null && !player.isSpectator()) {
            ServerLevel world = player.getLevel();
            BlockPos pos = player.getOnPos();
            if (fireworktick % 20 == 0 && fireworktick != 0) {

            }
            fireworktick++;
        }
    }

    // Helper method to get a random firework color
    private static int getRandomColor() {
        int[] colors = new int[] { 0xFF0000, 0xFFA500, 0xFFFF00, 0x008000, 0x0000FF, 0x4B0082, 0xEE82EE };
        return colors[new Random().nextInt(colors.length)];
    }
}