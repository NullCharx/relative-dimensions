package es.nullbyte.relativedimensions.effects.aberrant.sword;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import es.nullbyte.relativedimensions.effects.utils.tputils;

import java.util.Random;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.RANDOM;

public class DimensionalShift extends MobEffect {

    public DimensionalShift(MobEffectCategory category, int color) {
        super(category, color); // Color of the effect, e.g., dark red
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (!level.isClientSide && entity instanceof Player) {
            // Simplified check, since the instanceof Player check is redundant here
            int duration;
            try {
                duration = entity.getEffect(this).getDuration();
            } catch (NullPointerException e) {
                duration = 7*20;
            }
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 3, false, false, false));
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int durationRemaining, int amplifier) {
        return true; // Apply effect every 20 ticks (1 second)
    }


}
