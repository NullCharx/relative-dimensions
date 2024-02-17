    package es.nullbyte.relativedimensions.items.tracking;

    import es.nullbyte.relativedimensions.items.ModItems;
    import es.nullbyte.relativedimensions.items.tracking.common.ItemUtils;
    import net.minecraft.core.BlockPos;
    import net.minecraft.nbt.CompoundTag;
    import net.minecraft.network.chat.Component;
    import net.minecraft.server.level.ServerLevel;
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
    import net.minecraftforge.common.MinecraftForge;
    import net.minecraftforge.event.TickEvent;
    import net.minecraftforge.eventbus.api.SubscribeEvent;
    import org.jetbrains.annotations.NotNull;

    import javax.annotation.Nullable;
    import java.util.List;
    import java.util.UUID;

    public class PlayerTrackerCompass extends Item implements Vanishable {

        //ARGUMENTOS DE ORDEN INTERNO: (No cambian)
        private static final double RANGEOFDETECTION = 1000.00; //Player range of detection (in block units). This translates to a radius of RANGEOFDETECTION/2 blocks in every direction
        private static final TargetingConditions CONDITIONS = TargetingConditions.forNonCombat().range(RANGEOFDETECTION); //Default targeting conditions to get nearest player
        private static final float VOLATILIZATION_RANGE = 5F; //Range in which the compass will be volatilied if the player is near the target
        //--------------------
        public PlayerTrackerCompass(Properties properties) {
            super(properties);
            MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
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
                            Player nearestPlayer = world.getNearestPlayer(CONDITIONS, player);
                            System.out.println("Nearest player: " + nearestPlayer);
                            if (nearestPlayer != null) {
                                //Put player uuid in the compass nbt
                                setCompassTarget(compassTag, nearestPlayer);
                                compassTag.putInt("State", 2);
                                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.player_found"));
                            } else {
                                player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.no_players_found"));
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
            }

            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.START && !event.player.level().isClientSide) {
                // Construct a reference ItemStack for comparison. Note: This may need to be adjusted based on how your compass is uniquely identified.
                ItemStack referenceCompass = new ItemStack(ModItems.PLAYER_TRACKER_COMPASS.get());
                // Optionally set NBT data on the referenceCompass if you have a specific NBT structure to match against
                int compassSlot = event.player.getInventory().findSlotMatchingItem(referenceCompass);
                if (compassSlot != -1) { // If the player has a compass in their inventory
                    ItemStack foundCompass = event.player.getInventory().getItem(compassSlot);
                    CompoundTag nbt = foundCompass.getTag();
                    if (nbt != null && (nbt.getInt("State") == 2 || nbt.getInt("State") == 3)) {
                        //If the compass is locked on a player
                        UUID trackedPlayerUUID = UUID.fromString(nbt.getString("TrackedPlayerUUID"));
                        Player trackedPlayer = (event.player.level()).getPlayerByUUID(trackedPlayerUUID);
                        if (trackedPlayer != null) {
                            //if thet tracked player is found, update the compass position (not offline or in another dimension)
                            setCompassTarget(nbt, trackedPlayer);
                            double distance = event.player.distanceTo(trackedPlayer);
                            if (distance <= VOLATILIZATION_RANGE) {
                                // Remove the compass from the player's inventory if they are near the tracked player
                                event.player.sendSystemMessage(Component.translatable("item.relativedimensions.trackers.player_too_close"));
                                foundCompass.shrink(1); // This removes one item from the stack
                            }
                        }
                    }
                }
            }
        }
        //

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
