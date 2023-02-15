package es.nullbyte.charmiscmods.items;

import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerTrackerCompass extends Item implements Vanishable {

    //ARGUMENTOS DE ORDEN INTERNO: (No cambian)
    private static final double RANGEOFDETECTION = 1000.00; //Player range of detection (in block units). This translates to a radius of RANGEOFDETECTION/2 blocks in every direction
    private static final TargetingConditions conditions = TargetingConditions.DEFAULT; //Default targeting conditions to get nearest player
    //--------------------

    private boolean isArmed ; //Status of the compass: false while not tracking, true upon the moment a player is detected
    private Player userPlayer; //Player that uses the compass
    private ItemStack itemStack; //Stack of the player that uses the compass
    private Level currentWorld; //Current dimension of the player that uses the compass
    private Player nearestPlayer; //Player that uses the compass
    private int dataStatus; //Current status for needle direction pointing (texture)
    public PlayerTrackerCompass(Properties properties) {
        super(properties);
        isArmed = false;
        dataStatus = 0;
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }
//Player capabilities: https://www.youtube.com/watch?v=My70x9LzeUM


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!isArmed){

            itemStack = player.getItemInHand(hand);
            itemStack.getOrCreateTag().putInt("CustomModelData", dataStatus);
            Player localNearestPlayer = world.getNearestPlayer(conditions, player.position().x, player.position().y, player.position().z);

            if (localNearestPlayer == null) {
                if (world.isClientSide()) {
                    player.sendSystemMessage(Component.literal(String.format("No se encontraron jugadores en el área.")));
                }
                dataStatus = 0;
                itemStack.getOrCreateTag().putInt("CustomModelData", dataStatus);
                isArmed = false;

            } else {
                if (world.isClientSide()) {
                    player.sendSystemMessage(Component.literal(String.format("Jugador encontrado. Brújula armada...")));
                }
                userPlayer = player; //Set player
                currentWorld = world;
                nearestPlayer = localNearestPlayer;
                isArmed = true;
            }
        } else {
            if (world.isClientSide()) {
                player.sendSystemMessage(Component.literal(String.format("Un jugador ya está siendo rastreado.")));
            }
        }
        return super.use(world, player, hand);
    }

    @SubscribeEvent
    public void ItemTick(TickEvent.PlayerTickEvent event) {
        if (isArmed){
            updateCompass();
        }
    }

    private void updateCompass() {
        // your code to update the compass direction

        double distanceToItemUser = userPlayer.distanceTo(nearestPlayer);
        if ( distanceToItemUser > RANGEOFDETECTION) {

            if (currentWorld.isClientSide()) {
                userPlayer.sendSystemMessage(Component.literal(String.format("No se encontraron jugadores en el área.")));
            }
            dataStatus = 0;
            isArmed = false;
            itemStack.getOrCreateTag().putInt("CustomModelData", 0);
        } else if (distanceToItemUser < 5) {
            //Delete the item from the inventory once "one use is done" (you approach to 8 blocks or less from the tracked player)
            if (currentWorld.isClientSide()) {
                userPlayer.sendSystemMessage(Component.literal(String.format("Debido a la cercanía con el objetivo, la brújula se ha sobrecargado y ya no funciona.La energía sobrante te ha permitido ver como la brújula se ha volatilizado antes tus ojos")));
            }
            dataStatus = 0;
            isArmed = false;
            Inventory inv = userPlayer.getInventory();
            inv.removeItem(itemStack);
        }else{
            Vec3 playerPos = userPlayer.position(); //User position
            Vec3 nearestPlayerPos = nearestPlayer.position(); //Nearest player position

            //Compute distance taking into account both the relative position between players (plane X-Z) and the direction
            //The user of the item is looking at. (Commented lines compute Y axis, not needed)
            //double  playerPitch = player.getRotationVector().x;
            //double pitch = -Math.toDegrees(Math.atan2(yDiff, distance));
            //double yDiff = nearestPlayerPos.y - playerPos.y + nearestPlayer.getEyeHeight() - player.getEyeHeight();
            //double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
            double playerYaw = userPlayer.getRotationVector().y;
            double xDiff = nearestPlayerPos.x - playerPos.x;
            double zDiff = nearestPlayerPos.z - playerPos.z;

            double angle = Math.toDegrees(Math.atan2(zDiff, xDiff)) - playerYaw;

            //Computation has a 90 degrees offset. Fix below:
            angle = (angle + 360) % 360;
            angle = angle - 90;
            if (angle < 0) {
                angle = 360 + angle;
            }

            //Change dataStatus depending on relative angle between user and objective.
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
