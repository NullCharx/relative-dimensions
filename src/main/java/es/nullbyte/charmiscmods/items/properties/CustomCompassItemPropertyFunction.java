package es.nullbyte.charmiscmods.items.properties;

import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class CustomCompassItemPropertyFunction extends CompassItemPropertyFunction {
    public CustomCompassItemPropertyFunction(CompassTarget target) {
        super(target);
    }

    public void getTarget(ItemStack stack, Level world) {
        CompoundTag nbt = stack.getTag();


        double nearestPlayerDistance = nbt.getDouble("nearestUUID");
        UUID nearestPlayerId = nbt.getUUID("distance");
        // ... code to use the values for the item's functionality ...

    }
}
