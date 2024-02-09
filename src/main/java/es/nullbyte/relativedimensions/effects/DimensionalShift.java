package es.nullbyte.relativedimensions.effects;

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
    public static final double TP_DISTANCE = 14.0;

    public DimensionalShift() {
        super(MobEffectCategory.HARMFUL, 0x3D1585); // Dark red color for the effect
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (!level.isClientSide && entity instanceof Player) {
            // Simplified check, since the instanceof Player check is redundant here
            tputils.teleportRandomly(entity, TP_DISTANCE);
            int duration;
            try {
                duration = entity.getEffect(this).getDuration();
            } catch (NullPointerException e) {
                duration = 7*20;
            }
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, 0, false, true, true));
        }
    }


}
