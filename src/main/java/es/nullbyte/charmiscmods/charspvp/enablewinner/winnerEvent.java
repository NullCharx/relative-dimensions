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

                ListTag explosionsList = new ListTag();
                CompoundTag explosionsTag = new CompoundTag();
                explosionsTag.putByte("Type", (byte) 3);
                explosionsTag.putByte("Flicker", (byte) 0);
                explosionsTag.putByte("Trail", (byte) 1);
                explosionsTag.putIntArray("Colors", new int[] {5635925});
                explosionsTag.putIntArray("FadeColors", new int[] {11141120});
                //List of explosion parameters (I just copied from a /summon example to test it)
                explosionsList.add(explosionsTag);
                //There might be multiple customized explosions, so they go in a list

                CompoundTag fireworksTag = new CompoundTag();
                fireworksTag.putInt("Flight", 2);
                fireworksTag.put("Explosions", explosionsList);
                // Flight:2,Explosions:[{Type:3,Flicker:0,Trail:1,Colors:[I;5635925],FadeColors:[I;11141120]}]
                //The whole firework data, including how high up will it go, go in a higher nested tag

                //NOW you create a rocket item stack and add ONLY the fireworks tag to it. The rest of the needed tags will be read for the entity later
                ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
                CompoundTag fireWorkitemTag = fireworkItem.getOrCreateTag();
                fireWorkitemTag.put("Fireworks", fireworksTag);
                // (The full item tag will have the item id and the stack Count) Fireworks:{Flight:2,Explosions:[{Type:3,Flicker:0,Trail:1,Colors:[I;5635925],FadeColors:[I;11141120]}]}

                // Create and spawn the firework rocket entity
                double x = pos.getX() + (new Random().nextDouble() * 20 - 10);

                double z = pos.getZ() + (new Random().nextDouble()  * 20 - 10);
                BlockPos ypos = new BlockPos(x, 350, z);
                while (ypos.getY() > -64 && world.isEmptyBlock(ypos)) {
                    ypos = ypos.below();
                }
                double y = ypos.above().getY();

                //Here we use the already crated rocket itemstsack to feed the entity: It will read all the tags (Including the needed ID and stackcount from the stack itself) Lastly, we also specify the lifetime of the rocket. This goes directly before the item data.
                FireworkRocketEntity rocket = new FireworkRocketEntity(world, x, y, z, fireworkItem);
                CompoundTag fireworkEntityTag = new CompoundTag();
                fireworkEntityTag.putInt("LifeTime", 40);
                rocket.addAdditionalSaveData(fireworkEntityTag);
                // LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:3,Flicker:0,Trail:1,Colors:[I;5635925],FadeColors:[I;11141120]}]}}} This is what the full /summon command looks like and how the order of the tags ends up looking like (I think)
                world.addFreshEntity(rocket); //Spawn the rocket entity

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