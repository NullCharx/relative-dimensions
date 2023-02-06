package es.nullbyte.charmiscmods.items;

import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.List;

import static java.lang.Math.floor;

public class PlayerTrackerCompass extends Item implements Vanishable {

    private static final double RANGEOFDETECTION = 100.00; //Player range of detection (in block units)
    private static int dataStatus = 0;
    public PlayerTrackerCompass(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }
//Player capabilities: https://www.youtube.com/watch?v=My70x9LzeUM


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        itemStack.getOrCreateTag().putInt("CustomModelData", dataStatus);

        if (world.isClientSide()) {
            player.sendSystemMessage(Component.literal(String.format("Scanning...")));
        }

        TargetingConditions conditions = TargetingConditions.DEFAULT;
        Player nearestPlayer = world.getNearestPlayer(conditions, player.position().x, player.position().y, player.position().z);


        if (nearestPlayer == null) {
            if (world.isClientSide()) {
                player.sendSystemMessage(Component.literal(String.format("No players found in range...")));
            }
            return super.use(world, player, hand);
        }
        double distanceToItemUser = player.distanceTo(nearestPlayer);
        if ( distanceToItemUser > RANGEOFDETECTION) {
            if (world.isClientSide()) {
                player.sendSystemMessage(Component.literal(String.format("No players found in range...")));
                itemStack.getOrCreateTag().putInt("CustomModelData", 0);

            }
        } else {
            Vec3 playerPos = player.position(); //User position
            Vec3 nearestPlayerPos = nearestPlayer.position(); //Nearest player position

            //ABSOLUTE Physical angle with the item user as  center
            double xDiff = nearestPlayerPos.x - playerPos.x;
            double zDiff = nearestPlayerPos.z - playerPos.z;
            double angle = Math.toDegrees(Math.atan2(zDiff, xDiff));
            angle = (angle + 360) % 360;

            if (world.isClientSide()) {
                player.sendSystemMessage(Component.literal(String.format("BINGO BONGO..." + nearestPlayer.getName().getString() + ", " + distanceToItemUser + " blocks away. Angle separation of " + angle + ". Datastatus;" + dataStatus)));
            }

            if (angle >= 0 && angle < 5.625) {
                //North (Eastern half)
                dataStatus = 17;
            } else if (angle >= 5.625 && angle < 16.875) {
                dataStatus = 18;
            } else if (angle >= 16.875 && angle < 28.125) {
                dataStatus = 19;
            } else if (angle >= 28.125 && angle < 39.375) {
                dataStatus = 20;
            }else if (angle >= 39.375 && angle < 50.625) {
                //Nort-east
                dataStatus = 21;
            }else if (angle >= 50.625 && angle < 61.875) {
                dataStatus = 22;
            }else if (angle >= 61.875 && angle < 73.125) {
                dataStatus = 23;
            }else if (angle >= 73.125 && angle < 84.375) {
                dataStatus = 24;
            }else if (angle >= 84.375 && angle < 95.625) {
                //East
                dataStatus = 25;
            }else if (angle >= 95.625 && angle < 106.875) {
                dataStatus = 26;
            }else if (angle >= 106.875 && angle < 118.125) {
                dataStatus = 27;
            }else if (angle >= 118.125 && angle < 129.375) {
                dataStatus = 28;
            }else if (angle >= 129.375 && angle < 140.625) {
                //South-East
                dataStatus = 29;
            }else if (angle >= 140.625 && angle < 151.875) {
                dataStatus = 30;
            } else if (angle >= 151.875 && angle < 163.125) {
                dataStatus = 31;
            } else if (angle >= 163.125 && angle < 174.375) {
                dataStatus = 32;
            } else if (angle >= 174.375 && angle < 185.625) {
                //South
                dataStatus = 1;
            } else if (angle >= 185.625 && angle < 196.875) {
                dataStatus = 2;
            } else if (angle >= 196.875 && angle < 208.125) {
                dataStatus = 3;
            } else if (angle >= 208.125 && angle < 219.375) {
                dataStatus = 4;
            } else if (angle >= 219.375 && angle < 230.625) {
                //South-west
                dataStatus = 5;
            } else if (angle >= 230.625 && angle < 241.875) {
                dataStatus = 6;
            } else if (angle >= 241.875 && angle < 253.125) {
                dataStatus = 7;
            } else if (angle >= 253.125 && angle < 264.375) {
                dataStatus = 8;
            } else if (angle >= 264.375 && angle < 275.625) {
                //West
                dataStatus = 9;
            } else if (angle >= 275.625 && angle < 286.875) {
                dataStatus = 10;
            } else if (angle >= 286.875 && angle < 298.125) {
                dataStatus = 11;
            } else if (angle >= 298.125 && angle < 309.375) {
                dataStatus = 12;
            } else if (angle >= 309.375 && angle < 320.625) {
                //North-west
                dataStatus = 13;
            } else if (angle >= 320.625 && angle < 331.875) {
                dataStatus = 14;
            } else if (angle >= 331.875 && angle < 343.125) {
                dataStatus = 15;
            } else if (angle >= 343.125 && angle < 354.375) {
                dataStatus = 16;
            } else if (angle >= 354.375 && angle <= 360) {
                //North (western half)
                dataStatus = 17;
            }
        }
        itemStack.getOrCreateTag().putInt("CustomModelData", dataStatus);


        return super.use(world, player, hand);
    }




    @SubscribeEvent
    public void PlayerTick(TickEvent.PlayerTickEvent event) {



    }


    /* makes the item enchantable. done in enchants tutorial
    @Override
    public int getEnchantmentValue() {
        return 10;
    }*/

    // makes it repairable
    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

}
