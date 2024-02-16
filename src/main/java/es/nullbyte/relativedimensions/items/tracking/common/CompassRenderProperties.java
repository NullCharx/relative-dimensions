package es.nullbyte.relativedimensions.items.tracking.common;

import es.nullbyte.relativedimensions.items.tracking.PlayerTrackerCompass;
import es.nullbyte.relativedimensions.items.tracking.TeamTrackerCompass;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
@OnlyIn(Dist.CLIENT)
public class CompassRenderProperties {
    @OnlyIn(Dist.CLIENT)

    public static void registerCompassProperty(Item customCompassItem) {
        ItemProperties.register(customCompassItem, new ResourceLocation("angle"), new ClampedItemPropertyFunction() {
            @OnlyIn(Dist.CLIENT)
            private double rotation;
            @OnlyIn(Dist.CLIENT)
            private double rota;
            @OnlyIn(Dist.CLIENT)
            private long lastUpdateTick;

            @OnlyIn(Dist.CLIENT)
            @Override
            public float unclampedCall(@NotNull ItemStack stack, ClientLevel world, LivingEntity entityLiving, int seed) {
                if (entityLiving == null && !stack.isFramed()) { //Check if held by a living entity or framed
                    return -1F;
                } else {
                    final boolean entityExists = entityLiving != null;
                    final Entity entity = entityExists ? entityLiving : stack.getFrame();
                    if (world == null) {
                        assert entity != null;
                        if (entity.level() instanceof ClientLevel) {
                            world = (ClientLevel) entity.level();
                        }
                    }

                    double rotation = entityExists ? (double) entity.getYRot() : getFrameRotation((ItemFrame) entity);
                    rotation = rotation % 360.0D;
                    double angle = getAngle(world, entity, stack);
                    if (angle == 0.69420D) {
                        return (float) angle;
                    }
                    double adjusted = Math.PI - ((rotation - 90.0D) * 0.01745329238474369D - angle);

                    if (entityExists && world != null) {
                        adjusted = wobble(world, adjusted);
                    }

                    final float f = (float) (adjusted / (Math.PI * 2D));
                    return Mth.positiveModulo(f, 1.0F);
                }
            }

            @OnlyIn(Dist.CLIENT)
            private double wobble(ClientLevel world, double amount) {
                if (world.getGameTime() != lastUpdateTick) {
                    lastUpdateTick = world.getGameTime();
                    double d0 = amount - rotation;
                    d0 = Mth.positiveModulo(d0 + Math.PI, Math.PI * 2D) - Math.PI;
                    d0 = Mth.clamp(d0, -1.0D, 1.0D);
                    rota += d0 * 0.1D;
                    rota *= 0.8D;
                    rotation += rota;
                }

                return rotation;
            }

            @OnlyIn(Dist.CLIENT)
            private double getFrameRotation(ItemFrame itemFrame) {
                Direction direction = itemFrame.getDirection();
                int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + itemFrame.getRotation() * 45 + i);
            }

            @OnlyIn(Dist.CLIENT)
            private double getAngle(ClientLevel world, Entity entity, ItemStack stack) {
                if (stack.getItem() instanceof PlayerTrackerCompass || stack.getItem() instanceof TeamTrackerCompass) {
                    BlockPos pos;
                    CompoundTag itemTag = stack.getOrCreateTag();
                    if (itemTag.getInt("State") == 2) {
                        pos = new BlockPos(itemTag.getInt("PlayerPosX"), itemTag.getInt("PlayerPosY"), itemTag.getInt("PlayerPosZ"));
                    } else {

                        return -0.69420D;
                    }
                    return Math.atan2((double) pos.getZ() - entity.position().z(), (double) pos.getX() - entity.position().x());
                }
                return 0.0D;
            }
        });
    }
}
