package es.nullbyte.charmiscmods.items;

import es.nullbyte.charmiscmods.items.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static es.nullbyte.charmiscmods.CharMiscModsMain.RANDOM;


public class TransmatBeamEmitter extends Item {
    //https://moddingtutorials.org/advanced-items

    //Settable variables
    private final int NUMUSES;
    private final double FAILURE_CHANCE; //Out of 100


    //Internal variables
    MobEffectInstance effectInstanceLevitate;
    MobEffectInstance effectInstancemmobile;
    MobEffectInstance effectInstanceNausea;
    MobEffectInstance effectInstanceBlind;
    MobEffectInstance effectInstanceDark;


    public TransmatBeamEmitter(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
        //Can be changed (or set ot a config file)
        FAILURE_CHANCE = 20;
        NUMUSES = 15;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        Vec3 posInit;
        Vec3 targetPos;
        double failure = (RANDOM.nextInt(101));

        if (world.isClientSide()) {
            player.displayClientMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.stabilishing"),false);

        }

        //Get the initial position of the player
        posInit = player.position();
        //Get the target position of the player
        //Transmat raytrace----------------------------------------
        BlockHitResult ray = rayTrace(world, player, ClipContext.Fluid.NONE); //Calling the function changes the ray distance, changing the range
        BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
        if (world.isClientSide()) {
            player.displayClientMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.targetlacq", lookPos.toString()),false);
        }
        //save the target position
        targetPos = new Vec3(lookPos.getX(), lookPos.getY(), lookPos.getZ());
        //---------------------------------------------------
        //Stack of the player that uses the compass - important to keep track of the item nbttags
        ItemStack itemStack = player.getItemInHand(hand);

        //Set NBT data
        //Set initial and target positions
        itemStack.getOrCreateTag().putDouble("initX", posInit.x());
        itemStack.getOrCreateTag().putDouble("inity", posInit.y());
        itemStack.getOrCreateTag().putDouble("initz", posInit.z());
        itemStack.getOrCreateTag().putDouble("targx", targetPos.x());
        itemStack.getOrCreateTag().putDouble("targy", targetPos.y());
        itemStack.getOrCreateTag().putDouble("targz", targetPos.z());

        //Initiate ticks counter
        itemStack.getOrCreateTag().putInt("ticksCounter", -1);
        //Set active state
        itemStack.getOrCreateTag().putBoolean("isActive", true);
        //Failure chance
        itemStack.getOrCreateTag().putBoolean("isFailure", failure <= FAILURE_CHANCE);

        // reduce durability
        ItemStack stack = player.getItemInHand(hand);
        stack.setDamageValue(stack.getDamageValue() + (stack.getMaxDamage()/ NUMUSES));

        // break if durability gets to 0
        if (stack.getDamageValue() >= stack.getMaxDamage()) stack.setCount(stack.getCount() - 1);
        player.getCooldowns().addCooldown(this, 999 * 20);
        return super.use(world, player, hand);

    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level itemLevel, @NotNull Entity itemEntity, int itemSlot, boolean isSelected) {
        //Increment variable if item was used
        if (itemStack.getOrCreateTag().getBoolean("isActive") && itemEntity instanceof Player itemPlayer) {//Set entity player
            itemStack.getOrCreateTag().putInt("ticksCounter", itemStack.getOrCreateTag().getInt("ticksCounter") + 1);
            if(itemStack.getOrCreateTag().getInt("ticksCounter") == 1) {


                if (itemLevel.isClientSide()) {
                    itemPlayer.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.locking"));
                }
                itemLevel.playSound(itemPlayer, itemPlayer.blockPosition(), SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);

                //Apply visual effects durinng transmat charge
                effectInstanceLevitate = new MobEffectInstance(MobEffects.LEVITATION, 7 * 20, 1, true, false, false);
                effectInstancemmobile = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6 * 20, 1, true, false, false);
                effectInstanceNausea = new MobEffectInstance(MobEffects.CONFUSION, 7 * 20, 255, true, false, false);
                effectInstanceBlind = new MobEffectInstance(MobEffects.BLINDNESS, 7 * 20, 1, true, false, false);
                effectInstanceDark = new MobEffectInstance(MobEffects.DARKNESS, 7 * 20, 1, true, false, false);
                itemPlayer.addEffect(effectInstanceLevitate);
                itemPlayer.addEffect(effectInstancemmobile);
                itemPlayer.addEffect(effectInstanceNausea);
                itemPlayer.addEffect(effectInstanceBlind);
                itemPlayer.addEffect(effectInstanceDark);
            } else if (itemStack.getOrCreateTag().getInt("ticksCounter") == 51) {
                if (itemLevel.isClientSide()) {
                    itemPlayer.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.upstrtransmat"));
                }
            } else if (itemStack.getOrCreateTag().getInt("ticksCounter") == 86) {
                if (itemLevel.isClientSide()) {
                    itemPlayer.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.energizing"));
                }
            } else if (itemStack.getOrCreateTag().getInt("ticksCounter") == 121) {
                if (itemLevel.isClientSide()) {
                    itemPlayer.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.stabilished"));
                }
                System.out.println(itemPlayer.position());
                //Random low chance of failure
                if (itemStack.getOrCreateTag().getBoolean("isFailure")) {
                    if (itemLevel.isClientSide()) {
                        itemPlayer.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.randerror"));
                    }

                    if (itemPlayer instanceof ServerPlayer serverPlayer) {
                        serverPlayer.teleportTo(itemStack.getOrCreateTag().getDouble("initx"), itemStack.getOrCreateTag().getDouble("inity"),itemStack.getOrCreateTag().getDouble("initz"));
                    }
                    itemPlayer.getCooldowns().removeCooldown(this);
                    itemPlayer.getCooldowns().addCooldown(this, 35 * 20);
                } else {

                    //Teleport player with sound
                    itemLevel.playSound(itemPlayer, itemPlayer.getX(), itemPlayer.getY(), itemPlayer.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (itemPlayer instanceof ServerPlayer serverPlayer) {
                        serverPlayer.teleportTo(itemStack.getOrCreateTag().getDouble("targx"),itemStack.getOrCreateTag().getDouble("targy"), itemStack.getOrCreateTag().getDouble("targz"));
                    }
                    itemLevel.playSound(itemPlayer, itemPlayer.getX(), itemPlayer.getY(), itemPlayer.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (itemLevel.isClientSide()) {
                        itemPlayer.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.success"));
                    }
                    itemPlayer.getCooldowns().removeCooldown(this);
                    itemPlayer.getCooldowns().addCooldown(this, 15 * 20);
                }
                // allow the teleport to cancel fall damage
                itemPlayer.fallDistance = 0F;
                System.out.println(itemPlayer.position());
                //State cleanup
                itemPlayer.removeEffect(MobEffects.LEVITATION);
                itemPlayer.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                itemPlayer.removeEffect(MobEffects.CONFUSION);
                itemPlayer.removeEffect(MobEffects.BLINDNESS);
                itemPlayer.removeEffect(MobEffects.DARKNESS);

                //Reset ticks counter
                itemStack.getOrCreateTag().putInt("ticksCounter", -1);
                //Stop transmat
                itemStack.getOrCreateTag().putBoolean("isActive", false);
                itemStack.getOrCreateTag().putBoolean("isFailure", false);

            }
            //Particle effect
            if (itemLevel instanceof ServerLevel serverLevel && itemStack.getOrCreateTag().getBoolean("isActive") && itemStack.getOrCreateTag().getInt("ticksCounter") > 0) {
                serverLevel.sendParticles(ParticleTypes.PORTAL, itemStack.getOrCreateTag().getDouble("initx"), itemStack.getOrCreateTag().getDouble("inity") + 1, itemStack.getOrCreateTag().getDouble("initz"), 100, 0.0D, 10.0D, 0.0D, 1.0D);
                serverLevel.sendParticles(ParticleTypes.PORTAL, itemStack.getOrCreateTag().getDouble("targx"), itemStack.getOrCreateTag().getDouble("targy") + 1, itemStack.getOrCreateTag().getDouble("targz"), 100, 0.0D, 10.0D, 0.0D, 1.0D);
            }
        }

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
        pTooltipComponents.add(Component.translatable("item.charmiscmods.avidsdpt.tooltip"));
        super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
    }

    //Custom raytrace method, does the same as standard ,method, but block distance can be set (range)
    //There was a protected static <BlockRayTraceResult> here before, but it was removed because it was not needed
    protected static BlockHitResult rayTrace(Level world, Player player, ClipContext.Fluid fluidMode) {
        double range = 100;//Block distance
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vector3d = player.getEyePosition(1.0F);
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vector3d1 = vector3d.add((double)f6 * range, (double)f5 * range, (double)f7 * range);
        return world.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, fluidMode, player));
    }
}