package es.nullbyte.charmiscmods.items;

import com.mojang.math.Vector3d;
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
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class TransmatBeamEmitter extends Item {
    private int messagesent = 0;
    //https://moddingtutorials.org/advanced-items
    public TransmatBeamEmitter(Properties properties) {

        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // get where the player is looking and move them there
        BlockHitResult ray = rayTrace(world, player, ClipContext.Fluid.NONE); //This makes the range the same as the mining range!
        BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
        player.setPos(lookPos.getX(), lookPos.getY(), lookPos.getZ());


        //Do nothing for three seconds using ticks
        //player.sendSystemMessage(Component.literal(String.format("Transmat channel open.")));


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
        return material.getItem() == ItemInit.testitem1.get();
    }

    //Custom raytrace method, does the same as standard ,method, but block distance can be set (range)
    protected static <BlockRayTraceResult> BlockHitResult rayTrace(Level world, Player player, ClipContext.Fluid fluidMode) {
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
