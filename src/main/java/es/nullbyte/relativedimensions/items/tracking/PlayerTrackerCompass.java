    package es.nullbyte.relativedimensions.items.tracking;

    import es.nullbyte.relativedimensions.RelativeDimensionsMain;
    import es.nullbyte.relativedimensions.items.tracking.common.ItemUtils;
    import net.minecraft.core.BlockPos;
    import net.minecraft.nbt.CompoundTag;
    import net.minecraft.nbt.NbtOps;
    import net.minecraft.nbt.NbtUtils;
    import net.minecraft.network.chat.Component;
    import net.minecraft.resources.ResourceKey;
    import net.minecraft.world.InteractionHand;
    import net.minecraft.world.InteractionResultHolder;
    import net.minecraft.world.entity.ai.targeting.TargetingConditions;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.item.Item;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    import org.jetbrains.annotations.NotNull;

    import java.util.Optional;
    import java.util.UUID;

    public class PlayerTrackerCompass extends Item {

        //ARGUMENTOS DE ORDEN INTERNO: (No cambian)
        private static final double RANGEOFDETECTION = 1000.00; //Player range of detection (in block units). This translates to a radius of RANGEOFDETECTION/2 blocks in every direction
        private static final TargetingConditions CONDITIONS = TargetingConditions.DEFAULT.range(RANGEOFDETECTION); //Default targeting conditions to get nearest player
        //--------------------
        public PlayerTrackerCompass(Properties properties) {
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
                            Player nearestPlayer = world.getNearestPlayer(CONDITIONS, player);
                            System.out.println("Nearest player: " + nearestPlayer);
                            if (nearestPlayer != null) {
                                //Put player uuid in the compass nbt
                                setCompassTarget(compassTag, nearestPlayer);
                                compassTag.putInt("State", 2);
                                player.sendSystemMessage(Component.literal("Compass is now pointing to the nearest player."));
                            } else {
                                compassTag.putInt("State", 3);
                                player.sendSystemMessage(Component.literal("No players found in range."));
                            }
                            break;
                        case 1: //Case searching
                            compassTag.putInt("State", 0);
                            player.sendSystemMessage(Component.literal("Compass is now disarmed."));
                            break;
                        case 2: //Case: Locked
                            //Get position of player on UUID tag
                            UUID foundPlayerUUID = compassTag.getUUID("PlayerUUID");
                            //Get player from UUID
                            Player foundPlayer = world.getPlayerByUUID(foundPlayerUUID);
                            //If the player is not found
                            if (foundPlayer == null) {
                                compassTag.putInt("State", 3);
                                player.sendSystemMessage(Component.literal("Dimensional signature lost. Compass will disarm."));
                                break;
                            } else if (foundPlayer.level().dimension() != world.dimension()) {
                                player.sendSystemMessage(Component.literal("Dimensional signature lost. Compass will hold last known position until player is found again."));
                            }else {

                                setCompassTarget(compassTag, foundPlayer);
                                compassTag.putInt("State", 2);
                                player.sendSystemMessage(Component.literal("Compass is latched."));
                            }
                            break;
                        case 3: //Case: No players found
                            compassTag.putInt("State", 0);
                            player.sendSystemMessage(Component.literal("Compass is now disarmed."));
                            break;
                    }
                }
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
    }
