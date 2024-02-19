    package es.nullbyte.relativedimensions.items.tracking;

    import es.nullbyte.relativedimensions.items.tracking.common.ItemUtils;
    import net.minecraft.core.BlockPos;
    import net.minecraft.nbt.CompoundTag;
    import net.minecraft.network.chat.Component;
    import net.minecraft.world.InteractionHand;
    import net.minecraft.world.InteractionResultHolder;
    import net.minecraft.world.entity.Entity;
    import net.minecraft.world.entity.ai.targeting.TargetingConditions;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.item.Item;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.item.TooltipFlag;
    import net.minecraft.world.item.Vanishable;
    import net.minecraft.world.level.Level;
    import net.minecraft.world.phys.AABB;
    import net.minecraft.world.scores.Team;
    import net.minecraftforge.eventbus.api.SubscribeEvent;
    import org.jetbrains.annotations.NotNull;

    import javax.annotation.Nullable;
    import java.util.Comparator;
    import java.util.List;
    import java.util.UUID;

    public class TeamTrackerCompass extends Item implements Vanishable {

        //ARGUMENTOS DE ORDEN INTERNO: (No cambian)
        private static final double RANGEOFDETECTION = 1000.00; //Player range of detection (in block units). This translates to a radius of RANGEOFDETECTION/2 blocks in every direction
        private static final TargetingConditions CONDITIONS = TargetingConditions.DEFAULT.range(RANGEOFDETECTION); //Default targeting conditions to get nearest player
        private static final float TOTAL_USAGES = 10; //Total usages allowed before the compass breaks

        //--------------------
        public TeamTrackerCompass(Properties properties) {
            super(properties);
        }



        //States: 0 for disarmed, 1 for searching 2 for locked 3 for no players found
        @Override
        public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
            if (!world.isClientSide) {
                ItemStack compassStack = ItemUtils.getHeldPlayerTrackerCompass(player);
                if (!compassStack.isEmpty()) {
                    CompoundTag compassTag = compassStack.getOrCreateTag();
                    if (!compassTag.contains("State")){
                        compassTag.putInt("State", 0);
                    }
                    switch (compassTag.getInt("State")){
                        case 0: //Case: Disarmed
                            Team localUserTeam = player.getTeam(); //This will return all the teammembes no matter if they are online or not!!
                            if (localUserTeam == null || localUserTeam.getPlayers().size() == 1) {
                                //User has no team or no other player on team than us.
                                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.no_players_found"));
                            } else {
                                //Check if there is actually any online team member. Just check the currentPlayer variable.

                                // Get all players on the same team
                                List<Player> teamPlayers = world.getEntitiesOfClass(Player.class, new AABB(player.blockPosition()).inflate(6000.0D),
                                        e -> e != player && e.getScoreboard().getPlayersTeam(e.getName().getString()).equals(player.getScoreboard().getPlayersTeam(player.getName().getString())));

                                // Find the nearest player
                                Player localnearestPlayer = teamPlayers.stream()
                                        .min(Comparator.comparingDouble(player::distanceToSqr))
                                        .orElse(null);

                                if (localnearestPlayer != null) {
                                    player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.player_found"));
                                    compassTag.putInt("State", 2);
                                }else {
                                    player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.no_players_found"));

                                }
                            }
                            break;
                        case 1: //Case searching
                            compassTag.putInt("State", 0);
                            player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.recalibrating"));
                            break;
                        case 2: //Case: Locked on player
                        case 3: //Case:  Locked on player but player is not found
                            //Get position of player on UUID tag
                            UUID foundPlayerUUID = compassTag.getUUID("PlayerUUID");
                            //Get player from UUID
                            Player foundPlayer = world.getPlayerByUUID(foundPlayerUUID);
                            //If the tracked player is not found (player left the game or is not in the same dimension)
                            if (foundPlayer == null) {
                                compassTag.putInt("State", 3);
                                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.errorleftwhiletracking"));
                            }else { //If the tracker player is found
                                setCompassTarget(compassTag, foundPlayer);
                                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.pos_updated"));
                            }
                            break;
                    }
                }
                compassStack.setDamageValue((int) (compassStack.getDamageValue() + (compassStack.getMaxDamage()/TOTAL_USAGES)));

            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }


        private void setCompassTarget(CompoundTag tag, Player player) {
            BlockPos pos = player.blockPosition();
            tag.putInt("PlayerPosX", pos.getX());
            tag.putInt("PlayerPosY", pos.getY());
            tag.putInt("PlayerPosZ", pos.getZ());
            tag.putUUID("PlayerUUID", player.getUUID());

        }



        // makes it repairable
        @Override
        public boolean isRepairable(@NotNull ItemStack stack) {
            return false;
        }
        @Override
        public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
            int currentState = pStack.getOrCreateTag().getInt("State");
            if(currentState == 2) {
                //State 2, make tje text blue
                pTooltipComponents.add(Component.translatable("item.relativedimensions.trackers.state2").withColor(0x0000FF));
            }else if(currentState == 3) {
                //State 3, red
                pTooltipComponents.add(Component.translatable("item.relativedimensions.trackers.state3").withColor(0xFF9900));
            }
            pTooltipComponents.add(Component.translatable("item.relativedimensions.trackercompass.tooltip"));
            super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
        }
    }
