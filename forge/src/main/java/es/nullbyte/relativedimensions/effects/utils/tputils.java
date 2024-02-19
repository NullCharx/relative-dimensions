package es.nullbyte.relativedimensions.effects.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.RANDOM;

public class tputils {

    public static void teleportRandomly(LivingEntity entity, double distance) {
        Level level = entity.level();
        double dX = entity.getX() + (RANDOM.nextDouble() - 0.5) * 2 * distance;
        double dY = entity.getY() + (RANDOM.nextDouble() - 0.5) * 2 * distance;
        double dZ = entity.getZ() + (RANDOM.nextDouble() - 0.5) * 2 * distance;

        // Try to find a safe location up to 10 attempts
        for (int i = 0; i < 10; i++) {
            if (isLocationSafe(entity, dX, dY, dZ)) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                entity.teleportTo(dX, dY, dZ);
                level.playSound(null, dX, dY, dZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                return;
            }
        }
    }

    public static boolean isLocationSafe(LivingEntity entity, double x, double y, double z) {
        BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
        BlockPos above = pos.above();
        BlockPos twoAbove = above.above();
        return entity.level().isEmptyBlock(pos) && entity.level().isEmptyBlock(above) && entity.level().isEmptyBlock(twoAbove);
    }
}
