package es.nullbyte.relativedimensions.items.aberrant;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.effects.ModEffects;
import es.nullbyte.relativedimensions.effects.utils.tputils;
import es.nullbyte.relativedimensions.items.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.RANDOM;


public class AberrantAxe extends AxeItem {

    //probabilities over 100
    private final static double TP_CHANCE = 10.0;
    private final static double WIELDER_SUFFERING_CHANCE = 15.0;
    private final static double WIELDER_SUFFERING_DURATION_SECS = 15.0;
    private final static double TARGET_TP_DIZZY = 7.0;
    private static final double TP_DISTANCE = 14.0;
    private static final double WOOD_CORRUPTION = 5.0;

    private final static List<MobEffect> harmfulEffectslist = new ArrayList<>();
    private final static List<MobEffect> harmfulLongEffectslist = new ArrayList<>();
    private final static List<MobEffect> harmfulShortishEffectslist = new ArrayList<>();
    private final static List<MobEffect> harmfulShortEffectslist = new ArrayList<>();


    public AberrantAxe(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);

        //Fill the harmfulEffectslist
        harmfulShortEffectslist.add(MobEffects.LEVITATION);
        harmfulShortEffectslist.add(MobEffects.WEAKNESS);
        harmfulShortEffectslist.add(MobEffects.DARKNESS);

        harmfulShortishEffectslist.add(MobEffects.CONFUSION);
        harmfulShortishEffectslist.add(MobEffects.MOVEMENT_SLOWDOWN);
        harmfulShortishEffectslist.add(MobEffects.BLINDNESS);

        harmfulLongEffectslist.add(MobEffects.BAD_OMEN);
        harmfulLongEffectslist.add(MobEffects.UNLUCK);
        harmfulLongEffectslist.add(MobEffects.HUNGER);
        harmfulShortEffectslist.add(MobEffects.DIG_SLOWDOWN);

        harmfulEffectslist.add(MobEffects.LEVITATION);
        harmfulEffectslist.add(MobEffects.WEAKNESS);
        harmfulEffectslist.add(MobEffects.HUNGER);
        harmfulEffectslist.add(MobEffects.DIG_SLOWDOWN);
        harmfulEffectslist.add(MobEffects.DARKNESS);
        harmfulEffectslist.add(MobEffects.CONFUSION);
        harmfulEffectslist.add(MobEffects.MOVEMENT_SLOWDOWN);
        harmfulEffectslist.add(MobEffects.BAD_OMEN);
        harmfulEffectslist.add(MobEffects.UNLUCK);
        harmfulEffectslist.add(MobEffects.BLINDNESS);

    }

    //https://moddingtutorials.org/advanced-items


    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        Boolean damage = super.hurtEnemy(stack, target, attacker);
        Player attackerPlayer = (Player) attacker;
        //Test frist for the TP_CHANCE
        if (RANDOM.nextDouble(100.0) < TP_CHANCE) {
            if (target instanceof Player targetPlayer){
                tputils.teleportRandomly(target, TP_DISTANCE);
                targetPlayer.addEffect(new MobEffectInstance(ModEffects.DIMENSIONAL_NAUSEA.get(), (int) TARGET_TP_DIZZY *20 , 0, true, true, true));
                //Negate target fall damage
                targetPlayer.fallDistance = 0.0F;
            } else {
                tputils.teleportRandomly(target, TP_DISTANCE);
            }
            //Play endder teleport sound
        }
        //Now test for the WIELDER_SUFFERING_CHANCE
        if (RANDOM.nextDouble(100.0) < WIELDER_SUFFERING_CHANCE) {
            //Get all the effects labeled as harmful
            MobEffect chosenEffect = harmfulEffectslist.get(RANDOM.nextInt(harmfulEffectslist.size()));
            if (harmfulShortEffectslist.contains(chosenEffect)) {
                if (chosenEffect == MobEffects.LEVITATION) {
                    attackerPlayer.addEffect(new MobEffectInstance(chosenEffect, (int) (4*20), 0, true, true, true));
                } else {
                    attackerPlayer.addEffect(new MobEffectInstance(chosenEffect, (int) (WIELDER_SUFFERING_DURATION_SECS*20), 0, true, true, true));
                }
            } else if (harmfulShortishEffectslist.contains(chosenEffect)) {
                //If the player already has two effects of this category applied, do nothing.
                if (!((attackerPlayer.hasEffect(MobEffects.CONFUSION) && attackerPlayer.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) ||
                    (attackerPlayer.hasEffect(MobEffects.CONFUSION) && attackerPlayer.hasEffect(MobEffects.BLINDNESS)) ||
                        (attackerPlayer.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && attackerPlayer.hasEffect(MobEffects.BLINDNESS)))){
                    attackerPlayer.addEffect(new MobEffectInstance(chosenEffect, (int) (WIELDER_SUFFERING_DURATION_SECS*20 *2), 0, true, true, true));
                }
            } else if (harmfulLongEffectslist.contains(chosenEffect) && !attackerPlayer.hasEffect(MobEffects.BAD_OMEN)
                    && !attackerPlayer.hasEffect(MobEffects.UNLUCK) && !attackerPlayer.hasEffect(MobEffects.HUNGER)
                    && !attackerPlayer.hasEffect(MobEffects.DIG_SLOWDOWN)){
                //see if player already has a harmful long effect using sets
                attackerPlayer.addEffect(new MobEffectInstance(chosenEffect, (int) (WIELDER_SUFFERING_DURATION_SECS*20*8), 0, true, true, true));
            }
            attackerPlayer.addEffect(new MobEffectInstance(ModEffects.VOID_BLEED.get(), (int) ( RANDOM.nextInt(1, 10)*20), 0, true, true, true));
        }
        return damage;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull net.minecraft.core.BlockPos pos, @NotNull LivingEntity entityLiving) {

        //Check on a small area (say a 7x7x7 block) around the block mined for any wood based block which have an
        //aberrant counterpart: Logs, wood, planks, stripped logs, stripped wood, leaves...
        //TODO: doors, trapdoors, fences, fence gates, pressure plates, buttons, signs, boats, chests, crafting tables, ladders, beds, bookshelves, jukeboxes, note blocks, lecterns, barrels, composters, beehives, honey blocks, scaffolding, campfires, soul campfires, lanterns, soul lanterns, chains, and all the other blocks that can be made of wood.
        //If any of these blocks are found, replace them with their aberrant counterpart.
        if (!level.isClientSide && (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS) ||
                state.is(Blocks.STRIPPED_OAK_LOG) || state.is(Blocks.STRIPPED_OAK_WOOD))) {
                    BlockPos.betweenClosedStream(pos.offset(-2, -2, -2), pos.offset(3, 3, 3))
                    .forEach(adjacentPos -> {
                net.minecraft.world.level.block.state.BlockState adjacentState = level.getBlockState(adjacentPos);
                if (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.SAPLINGS)
                                || state.is(BlockTags.PLANKS) || state.is(Blocks.STRIPPED_OAK_LOG) || state.is(Blocks.STRIPPED_OAK_WOOD)) {
                    if (RANDOM.nextDouble(100) < WOOD_CORRUPTION) { // 5% chance to corrupt
                        Block aberrantBlock = getAberrantCounterpartForTag(adjacentState.getBlock());
                        if (aberrantBlock != null) {
                            level.setBlock(adjacentPos, aberrantBlock.defaultBlockState(), 3); // Replace with aberrant counterpart
                        }
                    }
                }
            });
        }
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    // This method would need to be implemented to return the aberrant counterpart based on the block's tag
    public static Block getAberrantCounterpartForTag(Block block) {
        // Implement logic to determine and return the aberrant counterpart based on the block's tag
        // Example: If the block is any kind of log, return the aberrant log block
        if (block.defaultBlockState().is(BlockTags.LOGS)) {
            return BlockInit.ABERRANT_LOG.get();
        } else if (block.defaultBlockState().is(BlockTags.LEAVES)) {
            return BlockInit.ABERRANT_LEAVE.get();
        } else if (block.defaultBlockState().is(BlockTags.SAPLINGS)) {
            return BlockInit.ABERRANT_SAPLING.get();
        } else if (block.defaultBlockState().is(BlockTags.PLANKS)) {
            return BlockInit.ABERRANT_PLANK.get();
        } else if (block.defaultBlockState().is(Blocks.STRIPPED_OAK_LOG)) {
            return BlockInit.STRIPPED_ABERRANT_LOG.get();
        } else if (block.defaultBlockState().is(Blocks.STRIPPED_OAK_WOOD)) {
            return BlockInit.STRIPPED_ABERRANT_WOOD.get();
        }
        // Extend the logic to cover other block types and tags as needed
        return null;
    }
    // makes it repairable
    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack tool, ItemStack material) {
        return material.getItem() == ItemInit.AVID_SDPT.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.relativedimensions.aberrant_axe.tooltip"));
        pTooltipComponents.add(Component.translatable("item.relativedimensions.aberrant_axe.tooltip_modifiers_list").withStyle(ChatFormatting.BOLD, ChatFormatting.LIGHT_PURPLE));

        super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
    }
}
