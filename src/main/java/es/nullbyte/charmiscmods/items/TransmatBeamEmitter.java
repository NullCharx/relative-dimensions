package es.nullbyte.charmiscmods.items;

import es.nullbyte.charmiscmods.items.init.ItemInit;
import es.nullbyte.charmiscmods.items.network.TransmatBeamHandler;
import es.nullbyte.charmiscmods.items.network.packet.coordPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static es.nullbyte.charmiscmods.CharMiscModsMain.RANDOM;
import static java.lang.Math.floor;

public class TransmatBeamEmitter extends Item {
    private boolean transmatStart = false;
    private boolean particleStart = false;
    private Vec3 posInit = null;
    private Vec3 posInitRounded = null;

    private Vec3 targetPos = null;
    private Player itemUser;

    private final double failureChance = 20;
    private final int numUses = 15;
    private double failure;
    //https://moddingtutorials.org/advanced-items
    public TransmatBeamEmitter(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }
//Player capabilities: https://www.youtube.com/watch?v=My70x9LzeUM


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        int secs = 10;
        //If not checking, the event will fire twice (both in client and server)
        if (world.isClientSide()) {
            player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.stabilishing"));

        }
        failure = (RANDOM.nextInt(101));
        posInit = player.position();
        posInitRounded = new Vec3(floor(player.getX()), floor(player.getY()), floor(player.getZ()));
        itemUser = player;
        transmatStart = true;
        // allow the teleport to cancel fall damage
        player.fallDistance = 0F;

        // only allow the player to use it every 3 seconds(60 ticks) (remember, 20 ticks = 1 second)
        if(failure < failureChance) {
            player.getCooldowns().addCooldown(this, 20*secs*4);
        } else {
            player.getCooldowns().addCooldown(this, 20*secs);
        }
        // reduce durability
        ItemStack stack = player.getItemInHand(hand);
                stack.setDamageValue(stack.getDamageValue() + (stack.getMaxDamage()/numUses));


        // break if durability gets to 0
        if (stack.getDamageValue() >= stack.getMaxDamage()) stack.setCount(0);

        return super.use(world, player, hand);
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

    //TODO change to server tick and redo. SERVERTICK and then use packets to comunicate with client.!!!!
    int ticksCounter = 0;
    @SubscribeEvent
    public void PlayerTick(TickEvent.PlayerTickEvent event) {
        Level playerLevel = event.player.level();

        if (transmatStart) {
            if(ticksCounter == 0) {
                event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.locking"));
                playerLevel.playSound(event.player, event.player.blockPosition(), SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);

                //Transmat raytrace----------------------------------------
                BlockHitResult ray = rayTrace(playerLevel, event.player, ClipContext.Fluid.NONE); //Calling the function changes the ray distance, changing the range
                BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());

                event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.targetlacq", lookPos.toString()));
                //event.player.sendSystemMessage(Component.literal(String.format("Target coordinates acquired: %s", lookPos)));

                //save the target position
                targetPos = new Vec3(lookPos.getX(), lookPos.getY(), lookPos.getZ());
                //---------------------------------------------------

                particleStart = true;
            } else {
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 1, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 2, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 3, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 4, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 5, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 6, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 7, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 8, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 9, targetPos.z, 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 10, targetPos.z, 0.0D, 0.0D, 0.0D);

                if (ticksCounter == 100) {//
                    event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.upstrtransmat"));
                }else if (ticksCounter == 250) {
                    //Play nether treshold sound
                    if (playerLevel.isClientSide()) {
                        event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.energizing"));
                    }
                } else if (ticksCounter == 400) {
                    ticksCounter = 0;
                    particleStart = false;
                    transmatStart = false;
                    event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.stabilished"));
                    System.out.println(event.player.position());
                    //Random low chance of failure
                    if (failure < failureChance) {
                        event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.randerror"));
                    } else {
                        // play a teleport sound. the last two args are volume and pitch before and after teleport
                        playerLevel.playSound(event.player, event.player.getX(), event.player.getY(), event.player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        TransmatBeamHandler.sendToServer(new coordPacket(targetPos.x, targetPos.y, targetPos.z));
                        playerLevel.playSound(event.player, event.player.getX(), event.player.getY(), event.player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        event.player.sendSystemMessage(Component.translatable("item.charmiscmods.transmatbeamemitter.state.success"));
                    }
                    // Reset counters and states
                    ticksCounter = 0;
                    particleStart = false;
                    transmatStart = false;
                }
            }
            if(particleStart) {
                //Generate particle effect
                //Make the player unable to move
                //Generate nether portal particles
                ticksCounter++;
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 1, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 2, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 3, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 4, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 5, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 6, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 7, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 8, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 9, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                playerLevel.addParticle(ParticleTypes.PORTAL, event.player.getX(), event.player.getY() + 10, event.player.getZ(), 0.0D, 0.0D, 0.0D);
                event.player.setPos(posInit.x, posInit.y, posInit.z);

            }
        }

    }


    /* makes the item enchantable. done in enchants tutorial
    @Override
    public int getEnchantmentValue() {
        return 10;
    }*/

    // makes it repairable
    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack tool, ItemStack material) {
        return material.getItem() == ItemInit.TRANSMAT_BEAM_EMITTER.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.charmiscmods.transmatbeamemitter.tooltip"));
        super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
    }
}
