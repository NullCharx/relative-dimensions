package es.nullbyte.charmiscmods.items;

import net.minecraft.world.item.ItemStack;

public interface CompassItemAccessor {
    void setAngle(ItemStack stack, double angle);

    void setPitch(ItemStack stack, double pitch);
}
