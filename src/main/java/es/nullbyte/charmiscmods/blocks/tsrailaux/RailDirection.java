package es.nullbyte.charmiscmods.blocks.tsrailaux;

import net.minecraft.util.StringRepresentable;

public enum RailDirection implements StringRepresentable {
    NONE("none"),
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west"),
    SOUTH_EAST("south_east"),
    SOUTH_WEST("south_west"),
    NORTH_WEST("north_west"),
    NORTH_EAST("north_east");

    private final String name;

    private RailDirection(String p_61743_) {
        this.name = p_61743_;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }


    public String getSerializedName() {
        return this.name;
    }
}