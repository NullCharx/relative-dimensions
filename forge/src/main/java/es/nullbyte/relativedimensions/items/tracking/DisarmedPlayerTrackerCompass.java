    package es.nullbyte.relativedimensions.items.tracking;

    import es.nullbyte.relativedimensions.items.ModItems;
    import net.minecraft.core.BlockPos;
    import net.minecraft.nbt.CompoundTag;
    import net.minecraft.network.chat.Component;
    import net.minecraft.world.InteractionHand;
    import net.minecraft.world.InteractionResultHolder;
    import net.minecraft.world.entity.ai.targeting.TargetingConditions;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.item.Item;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.item.TooltipFlag;
    import net.minecraft.world.item.Vanishable;
    import net.minecraft.world.level.Level;
    import org.jetbrains.annotations.NotNull;

    import javax.annotation.Nullable;
    import java.util.List;

    //LOGICA DE ACTIVACIÓN AQUÍ. CREAR UN STACK NUEVO CON LA BRUJULA ACTIVADA Y PONERLO EN EL INVENTARIO DEL JUGADOR CON LOS DATOS NBT CORRESPONDIENTES
    public class DisarmedPlayerTrackerCompass extends Item implements Vanishable {

        //ARGUMENTOS DE ORDEN INTERNO: (No cambian)
        private static final double RANGEOFDETECTION = 1000.00; //Player range of detection (in block units). This translates to a radius of RANGEOFDETECTION/2 blocks in every direction
        private static final TargetingConditions CONDITIONS = TargetingConditions.DEFAULT.range(RANGEOFDETECTION); //Default targeting conditions to get nearest player
        //--------------------
        public DisarmedPlayerTrackerCompass(Properties properties) {
            super(properties);
        }

        //States: 0 for disarmed, 1 for searching 2 for locked 3 for no players found
        @Override
        public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
            if (!world.isClientSide) {
                ItemStack compassStack = player.getItemInHand(hand);
                // Verificar si la brújula está desarmada antes de activarla
                if (compassStack.getItem() instanceof DisarmedPlayerTrackerCompass) {
                    Player nearestPlayer = world.getNearestPlayer(CONDITIONS, player);
                    if (nearestPlayer != null) {
                        // Crear un nuevo ItemStack de la brújula armada
                        ItemStack armedCompassStack = new ItemStack(ModItems.PLAYER_TRACKER_COMPASS.get());
                        CompoundTag compassTag = armedCompassStack.getOrCreateTag();

                        // Configurar la brújula armada con los datos del jugador más cercano
                        setCompassTarget(compassTag, nearestPlayer);
                        compassTag.putInt("State", 2); // Asegurar que el estado esté configurado correctamente

                        // Reemplazar la brújula actual con la armada (Siempre es stack de 1 por lo que se borra el stack actual)
                        compassStack.shrink(1);

                        player.setItemInHand(hand, armedCompassStack);

                        player.sendSystemMessage(Component.literal("Compass is now armed and pointing to the nearest player."));
                        return InteractionResultHolder.success(armedCompassStack);
                    } else {
                        player.sendSystemMessage(Component.literal("No players found in range."));
                        return InteractionResultHolder.fail(compassStack);
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


        @Override
        public boolean isRepairable(@NotNull ItemStack stack) {
            return false;
        }
        @Override
        public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
            pTooltipComponents.add(Component.translatable("item.relativedimensions.disarmedtrackercompass.tooltip"));
            super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
        }
    }
