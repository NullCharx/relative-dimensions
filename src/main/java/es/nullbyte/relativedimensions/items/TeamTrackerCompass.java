package es.nullbyte.relativedimensions.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class TeamTrackerCompass extends Item implements Vanishable {



    public TeamTrackerCompass(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }
//Player capabilities: https://www.youtube.com/watch?v=My70x9LzeUM


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
       ItemStack itemStack = player.getItemInHand(hand);
       Team localUserTeam = player.getTeam();//This will return all the teammembes no matter if they are online or not!!
        if(!itemStack.getOrCreateTag().getBoolean("isArmed")) {
            if ((localUserTeam == null || localUserTeam.getPlayers().size() <= 1)) {
                //User has no team or no other player on team than us.
                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.no_players_found"));
                itemStack.getOrCreateTag().putInt("CustomModelData", 0);
                itemStack.getOrCreateTag().putBoolean("isArmed", false);
            } else {
                //Check if there is actually any online team member. Just check the currentPlayer variable.
                if (world.isClientSide()) {
                    player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.player_found"));
                }
                updateVarsAndCalculate(itemStack, world, player);

            }
        }
        player.getCooldowns().addCooldown(this, 60);
        return super.use(world, player, hand);
    }


    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level itemLevel, @NotNull Entity itemEntity, int itemSlot, boolean isSelected) {
        if (itemEntity instanceof Player userPlayer && itemStack.getOrCreateTag().getBoolean("isArmed") ) {
            if (itemStack.getOrCreateTag().getFloat("DistanceToDetectedPlayer") < 5.0F) {
                //Delete the item from the inventory once "one use is done" (you approach to 8 blocks or less from the tracked player)
                if (itemLevel.isClientSide()) {
                    userPlayer.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.player_too_close"));
                }
                itemStack.getOrCreateTag().putInt("CustomModelData", 0);
                itemStack.getOrCreateTag().putBoolean("isArmed", false);
                Inventory inv = userPlayer.getInventory();
                inv.removeItem(itemStack);
            }else{

                double angle = updateVarsAndCalculate(itemStack, itemLevel, userPlayer);


                int dataStatus = 0;
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
                } else {
                    dataStatus = 0;
                }
                itemStack.getOrCreateTag().putInt("CustomModelData", dataStatus);
            }
        } else {
            System.out.println("waka waka  e e ");
            if (itemLevel.isClientSide() && itemEntity instanceof Player && itemStack.getOrCreateTag().getBoolean("isArmed") ) {
                itemEntity.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.compass_already_armed"));
            }
        }
        super.inventoryTick(itemStack, itemLevel, itemEntity, itemSlot, isSelected);
    }

    public double getPlayerDirectionAngle(Vec3 playerPos, double playerYaw, Vec3 nearestPlayerPos) {
        //Compute the angle between positions taking into account both the relative position between players (plane X-Z) and the direction
        //The user of the item is looking at. (Commented lines compute Y axis, not needed)
        double xDiff = nearestPlayerPos.x - playerPos.x;
        double zDiff = nearestPlayerPos.z - playerPos.z;

        double angle = Math.toDegrees(Math.atan2(zDiff, xDiff)) - playerYaw;

        //Computation has a 90 degrees offset. Fix below:
        angle = (angle + 360) % 360;
        angle = angle - 90;
        if (angle < 0) {
            angle = 360 + angle;
        }
        return angle;
    }

    public double updateVarsAndCalculate(ItemStack itemStack, Level world, Player player) {

        // Get all players on the same team
        List<Player> teamPlayers = world.getEntitiesOfClass(Player.class, new AABB(player.blockPosition()).inflate(6000.0D),
                e -> e != player && e.getScoreboard().getPlayersTeam(e.getName().getString()).equals(player.getScoreboard().getPlayersTeam(player.getName().getString())));

        // Find the nearest player that is not the user and is online
        Player localnearestPlayer = teamPlayers.stream()
                .min(Comparator.comparingDouble(player::distanceToSqr))
                .orElse(null);

        if (localnearestPlayer != null) { //If found, update the pertinent data
            itemStack.getOrCreateTag().putInt("CustomModelData", 0);
            itemStack.getOrCreateTag().putBoolean("isArmed", true);
            itemStack.getOrCreateTag().putFloat("DistanceToDetectedPlayer", player.distanceTo(localnearestPlayer));
            itemStack.getOrCreateTag().putDouble("userPlayerX", player.position().x);
            itemStack.getOrCreateTag().putDouble("userPlayerY", player.position().y);
            itemStack.getOrCreateTag().putDouble("userPlayerZ", player.position().z);
            itemStack.getOrCreateTag().putDouble("userPlayerYaw", player.getRotationVector().y);
            itemStack.getOrCreateTag().putDouble("nearestPlayerX", localnearestPlayer.position().x);
            itemStack.getOrCreateTag().putDouble("nearestPlayerY", localnearestPlayer.position().y);
            itemStack.getOrCreateTag().putDouble("nearestPlayerZ", localnearestPlayer.position().z);

            //Calculate the angle to render the compass needle
            Vec3 userPlayerPos = new Vec3(itemStack.getOrCreateTag().getDouble("userPlayerX"), itemStack.getOrCreateTag().getDouble("userPlayerY"), itemStack.getOrCreateTag().getDouble("userPlayerZ"));
            double userPlayerYaw = itemStack.getOrCreateTag().getDouble("userPlayerYaw");
            Vec3 nearestPlayerPos = new Vec3(itemStack.getOrCreateTag().getDouble("nearestPlayerX"), itemStack.getOrCreateTag().getDouble("nearestPlayerY"), itemStack.getOrCreateTag().getDouble("nearestPlayerZ"));

            return getPlayerDirectionAngle(userPlayerPos, userPlayerYaw, nearestPlayerPos);
        } else {
            if (world.isClientSide()) {
                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.no_players_found"));
            }
            itemStack.getOrCreateTag().putInt("CustomModelData", 0);
            itemStack.getOrCreateTag().putBoolean("isArmed", false);
            return -1;
        }


    }


    /* makes the item enchantable. done in enchants tutorial
    @Override
    public int getEnchantmentValue() {
        return 10;
    }*/

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("isArmed");
    }
    // makes it repairable
    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return false;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.relativedimensions.teamtrackercompass.tooltip"));
        super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
    }

}
