package es.nullbyte.charmiscmods.items;

import es.nullbyte.charmiscmods.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;
public class TransmatBeamEmitter extends Item {
    private static boolean transmatStart = false;
    private static int ticksCounter = 0;
    //https://moddingtutorials.org/advanced-items
    public TransmatBeamEmitter(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called
    }
//Player capabilities: https://www.youtube.com/watch?v=My70x9LzeUM


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // get where the player is looking and move them there
        BlockHitResult ray = rayTrace(world, player, ClipContext.Fluid.NONE); //Calling the function changes the ray distance, changing the range
        BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
        player.setPos(lookPos.getX(), lookPos.getY(), lookPos.getZ());

        //If not checking, the event will fire twice (both in client and server)
        if (world.isClientSide) {
            player.sendSystemMessage(Component.literal(String.format("Stabilishing transmat channel...")));
            transmatStart = true;
            //WAIT STILL NEEDED
            player.sendSystemMessage(Component.literal(String.format("Transmat channel established!")));
        }


        // only allow the player to use it every 3 seconds(60 ticks) (remember, 20 ticks = 1 second)
        player.getCooldowns().addCooldown(this, 30);


        // allow the teleport to cancel fall damage
        player.fallDistance = 0F;

        // play a teleport sound. the last two args are volume and pitch
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

        // reduce durability
        ItemStack stack = player.getItemInHand(hand);
        stack.setDamageValue(stack.getDamageValue() + 1);

        // break if durability gets to 0
        if (stack.getDamageValue() >= stack.getMaxDamage()) stack.setCount(0);

        return super.use(world, player, hand);

    }

    /* makes the item enchantable. done in enchants tutorial
    @Override
    public int getEnchantmentValue() {
        return 10;
    }*/

    // makes it repairable
    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack material) {
        return material.getItem() == ItemInit.TRANSMAT_BEAM_EMITTER.get();
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

    @SubscribeEvent
    public void PlayerTick(TickEvent.PlayerTickEvent event) {
        if (transmatStart) {
            ticksCounter++;
            if (ticksCounter == 30) {
                event.player.sendSystemMessage(Component.literal(String.format("Transmatting...")));
            }else if (ticksCounter == 60) {
                event.player.sendSystemMessage(Component.literal(String.format("Transmatting...")));
            } else if (ticksCounter == 90) {
                event.player.sendSystemMessage(Component.literal(String.format("Transmatting...")));
            } else if (ticksCounter == 110) {
                ticksCounter = 0;
                transmatStart = false;
            }
        } else {
            ticksCounter = 0;
        }
    }
}
