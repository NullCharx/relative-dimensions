package es.nullbyte.charmiscmods.items;

import es.nullbyte.charmiscmods.items.init.ItemInit;
import es.nullbyte.charmiscmods.items.network.TransmatTargetHandler;
import es.nullbyte.charmiscmods.items.network.packet.tpCoordsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static es.nullbyte.charmiscmods.CharMiscModsMain.RANDOM;


public class TransmatBeamEmitter extends Item {
    //https://moddingtutorials.org/advanced-items

    private final int numUses = 15;
    private double failure;
    private final double failureChance = 20; //Out of 100

    int ticksCounter = 0;
    private Vec3 targetPos;


    private boolean transmatStart = false;
    private boolean particleStart = false;

    public TransmatBeamEmitter(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        if (world.isClientSide()) {
            player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.stabilishing"));

        }
        failure = (RANDOM.nextInt(101));

        //Start transmat on player tick
        transmatStart = true;

        // allow the teleport to cancel fall damage
        player.fallDistance = 0F;

        // reduce durability
        ItemStack stack = player.getItemInHand(hand);
        stack.setDamageValue(stack.getDamageValue() + (stack.getMaxDamage()/numUses));

        // break if durability gets to 0
        if (stack.getDamageValue() >= stack.getMaxDamage()) stack.setCount(stack.getCount() - 1);

        return super.use(world, player, hand);

    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            if (transmatStart) {
                Player itemUser = event.player;
                Vec3 posInit = itemUser.position();
                Level playerLevel = itemUser.level();

                switch (ticksCounter) {
                    case 0:
                        itemUser.getCooldowns().addCooldown(this, 99999);
                        if (playerLevel.isClientSide()) {
                            itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.locking"));
                        }
                        playerLevel.playSound(itemUser, itemUser.blockPosition(), SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);

                        //Transmat raytrace----------------------------------------
                        BlockHitResult ray = rayTrace(playerLevel, itemUser, ClipContext.Fluid.NONE); //Calling the function changes the ray distance, changing the range
                        BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
                        if (playerLevel.isClientSide()) {
                            itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.targetlacq", lookPos.toString()));
                        }
                        //save the target position
                        targetPos = new Vec3(lookPos.getX(), lookPos.getY(), lookPos.getZ());
                        //---------------------------------------------------

                        //Apply levitation effect
                        MobEffectInstance effectInstanceLevitate = new MobEffectInstance(MobEffects.LEVITATION, 999*20, 1, false, true, false);
                        MobEffectInstance effectInstancemmobile = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 99 * 20, 255, false, true, false);
                        MobEffectInstance effectInstanceNausea = new MobEffectInstance(MobEffects.CONFUSION, 999 * 20, 255, false, true, false);
                        MobEffectInstance effectInstanceBlind = new MobEffectInstance(MobEffects.BLINDNESS, 999 * 20, 255, false, true, false);
                        MobEffectInstance effectInstanceDark = new MobEffectInstance(MobEffects.DARKNESS, 999 * 20, 255, false, true, false);
                        // Apply the effect to the player
                        itemUser.addEffect(effectInstanceLevitate);
                        itemUser.addEffect(effectInstancemmobile);
                        itemUser.addEffect(effectInstanceNausea);
                        itemUser.addEffect(effectInstanceBlind);
                        itemUser.addEffect(effectInstanceDark);

                        particleStart = true;
                        break;
                    case 100:
                        if (playerLevel.isClientSide()) {
                            itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.upstrtransmat"));
                        }
                        break;
                    case 240:
                        if (playerLevel.isClientSide()) {
                            itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.energizing"));
                        }
                        break;
                    case 400:
                        if (playerLevel.isClientSide()) {
                            itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.stabilished"));
                        }
                        itemUser.removeEffect(MobEffects.LEVITATION);
                        effectInstanceLevitate = new MobEffectInstance(MobEffects.LEVITATION, 999*20, 0, false, true, false);
                        itemUser.addEffect(effectInstanceLevitate);

                        System.out.println(itemUser.position());

                        //Random low chance of failure
                        if (failure < failureChance) {
                            if (playerLevel.isClientSide()) {
                                itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.randerror"));
                            }
                            itemUser.getCooldowns().removeCooldown(this);
                            itemUser.getCooldowns().addCooldown(this, 35 * 20);
                            TransmatTargetHandler.sendToServer(new tpCoordsPacket(posInit.x, posInit.y, posInit.z));

                        } else {
                            // play a teleport sound. the last two args are volume and pitch before and after teleport
                            playerLevel.playSound(itemUser, posInit.x(), posInit.y(), posInit.z(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            TransmatTargetHandler.sendToServer(new tpCoordsPacket(targetPos.x, targetPos.y, targetPos.z));
                            playerLevel.playSound(itemUser, targetPos.x, targetPos.y, targetPos.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (playerLevel.isClientSide()) {
                                itemUser.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.success"));
                            }
                            itemUser.getCooldowns().removeCooldown(this);
                            itemUser.getCooldowns().addCooldown(this, 15 * 20);
                        }
                        itemUser.removeEffect(MobEffects.LEVITATION);
                        itemUser.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                        itemUser.removeEffect(MobEffects.CONFUSION);
                        itemUser.removeEffect(MobEffects.BLINDNESS);
                        itemUser.removeEffect(MobEffects.DARKNESS);

                        ticksCounter = -1;
                        particleStart = false;
                        transmatStart = false;
                        break;
                    default:
                        break;
                }
                ticksCounter++;

                if (particleStart) {
                    //Generate particle effect
                    //Generate nether portal particles at player...
                    playerLevel.addParticle(ParticleTypes.PORTAL, posInit.x(), posInit.y() + 1, posInit.z(), 0.0D, 10.0D, 0.0D);

                    //and at target
                    playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 1, targetPos.z, 0.0D, 10.0D, 0.0D);

                    if(!playerLevel.isClientSide()) {
                        //Same in server side to render particles at other players nearby
                        ServerLevel serverLevel = (ServerLevel) itemUser.level();
                        if (serverLevel!=null){
                            serverLevel.sendParticles(ParticleTypes.PORTAL, posInit.x(), posInit.y() + 1, posInit.z(), 100, 0.0D, 10.0D, 0.0D, 1.0D);
                            serverLevel.sendParticles(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 1, targetPos.z, 100, 0.0D, 10.0D, 0.0D, 1.0D);
                        }
                    }

                }
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
