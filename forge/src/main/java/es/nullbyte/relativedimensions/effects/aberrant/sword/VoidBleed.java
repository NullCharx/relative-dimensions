package es.nullbyte.relativedimensions.effects.aberrant.sword;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.RANDOM;

//This effect is applied to the wielder of the AberrantSword when it is used to attack an entity, randomly.
//It is a harmful effect that causes the player to take damage over time.
//Like poison, it cant kill the player, but it can leave them with half a heart.
public class VoidBleed extends MobEffect {

    private final boolean doHalf = RANDOM.nextBoolean();
    public VoidBleed(MobEffectCategory category, int color) {
        super(category, color); // Color of the effect, e.g., dark red
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Every 20 ticks is approximately one second in Minecraft. Do not execute if player only has half heart left.
        if (!entity.level().isClientSide() && entity instanceof Player && entity.getHealth() > 1.0F) {
            entity.hurt(entity.damageSources().fellOutOfWorld(), 1.0F); // 0.5 hearts damage
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int durationRemaining, int amplifier) {
        return durationRemaining % 20 == 0; // Apply effect every 20 ticks (1 second)
    }
}
