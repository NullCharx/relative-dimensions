package es.nullbyte.charmiscmods.charspvp.enablewinner;

import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.renderer.entity.FireworkEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.FireworkStarItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            if (fireworktick % new Random().nextInt(10,61) == 0 && fireworktick != 0) {
                int colorCount = new Random().nextInt(1, 5); // Random number of colors
                int fadeCount = new Random().nextInt(colorCount + 1); // Random number of fade colors

                ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);

                CompoundTag fireworksTag = new CompoundTag();
                fireworksTag.putInt("Flight", 1);

                ListTag explosionsList = new ListTag();

                CompoundTag explosionTag = new CompoundTag();
                explosionTag.putIntArray("Colors", generateRandomColors(colorCount));
                explosionTag.putIntArray("FadeColors", generateRandomColors(fadeCount));
                explosionTag.putByte("Type", (byte)  new Random().nextInt(5));
                explosionTag.putBoolean("Flicker",  new Random().nextBoolean());
                explosionTag.putBoolean("Trail",  new Random().nextBoolean());

                explosionsList.add(explosionTag);
                fireworksTag.put("Explosions", explosionsList);

                fireworkItem.setTag(fireworksTag);
                // Create and spawn the firework rocket entity
                double x = pos.getX() + (new Random().nextDouble() * 20 - 10);

                double z = pos.getZ() + (new Random().nextDouble()  * 20 - 10);
                BlockPos ypos = new BlockPos(x, 350, z);
                while (ypos.getY() > -64 && world.isEmptyBlock(ypos)) {
                    ypos = ypos.below();
                }
                double y = ypos.above().getY();
                FireworkRocketEntity rocket = new FireworkRocketEntity(world, x, y, z, ItemStack.EMPTY);
                rocket.load(fireworksTag);
                //world.addFreshEntity(rocket);

            }
            fireworktick++;
        }
    }

    // Helper method to get a random firework color
    private static int[] generateRandomColors(int count) {
        int[] colors = new int[count];

        for (int i = 0; i < count; i++) {
            colors[i] =  new Random().nextInt(0x1000000);
        }

        return colors;
    }
}