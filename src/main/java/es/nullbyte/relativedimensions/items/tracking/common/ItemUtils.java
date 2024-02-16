package es.nullbyte.relativedimensions.items.tracking.common;

import es.nullbyte.relativedimensions.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {

    public static ItemStack getHeldPlayerTrackerCompass(Player player) {
        return getHeldItem(player, ModItems.PLAYER_TRACKER_COMPASS.get());
    }
    public static ItemStack getHeldTeamTrackerCompass(Player player) {
        return getHeldItem(player, ModItems.TEAM_TRACKER_COMPASS.get());
    }

    public static ItemStack getHeldItem(Player player, Item item) {
        // Check if the main hand holds the item
        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() == item) {
            return player.getMainHandItem();
        }
        // Check if the offhand holds the item
        else if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() == item) {
            return player.getOffhandItem();
        }
        // Return an empty ItemStack if the item is not found in either hand
        else {
            return ItemStack.EMPTY;
        }
    }
}
