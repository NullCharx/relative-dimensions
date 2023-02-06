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

    private static final double RANGEOFDETECTION = 100.00;
    private static int currentindex = 1;
    public PlayerTrackerCompass(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }
//Player capabilities: https://www.youtube.com/watch?v=My70x9LzeUM


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        itemStack.getOrCreateTag().putInt("CustomModelData", 0);

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
            }
        }else {
            Vec3 playerPos = player.position();
            Vec3 nearestPlayerPos = nearestPlayer.position();
            double xDiff = nearestPlayerPos.x - playerPos.x;
            double zDiff = nearestPlayerPos.z - playerPos.z;
            double angle = Math.toDegrees(Math.atan2(zDiff, xDiff));
            angle = (angle + 360) % 360;
            if (world.isClientSide()) {
                player.sendSystemMessage(Component.literal(String.format("BINGO BONGO..." + nearestPlayer.getName().getString() + ", " + distanceToItemUser + " blocks away")));
            }

            if (angle >= 337.5 || angle < 22.5) {
                // North

                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the north")));
            } else if (angle >= 22.5 && angle < 67.5) {
                // Northeast
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the northeast")));
            } else if (angle >= 67.5 && angle < 112.5) {
                // East
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the east")));
            } else if (angle >= 112.5 && angle < 157.5) {
                // Southeast
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the southeast")));
            } else if (angle >= 157.5 && angle < 202.5) {
                // South
                itemStack.getOrCreateTag().putInt("CustomModelData", 1);
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the south")));
            } else if (angle >= 202.5 && angle < 247.5) {
                // Southwest
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the southwest")));
            } else if (angle >= 247.5 && angle < 292.5) {
                // West
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the west")));
            } else if (angle >= 292.5 && angle < 337.5) {
                // Northwest
                player.sendSystemMessage(Component.literal(String.format("Nearest player is to the northwest")));
            }
        }


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
