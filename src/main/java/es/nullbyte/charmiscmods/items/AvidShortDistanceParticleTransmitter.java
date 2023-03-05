package es.nullbyte.charmiscmods.items;

import es.nullbyte.charmiscmods.charspvp.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;


public class AvidShortDistanceParticleTransmitter extends Item {
    //https://moddingtutorials.org/advanced-items
    public AvidShortDistanceParticleTransmitter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // get where the player is looking and move them there
        BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE); //This makes the range the same as the mining range!
        BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
        player.setPos(lookPos.getX(), lookPos.getY(), lookPos.getZ());

        // only allow the player to use it every 3 seconds
        player.getCooldowns().addCooldown(this, 60);

        // allow the teleport to cancel fall damage
        player.fallDistance = 0F;

        // play a teleport sound. the last two args are volume and pitch
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

        // reduce durability
        ItemStack stack = player.getItemInHand(hand);
        stack.setDamageValue(stack.getDamageValue() + 3);

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
        return material.getItem() == ItemInit.AVID_SDPT.get();
    }
}
