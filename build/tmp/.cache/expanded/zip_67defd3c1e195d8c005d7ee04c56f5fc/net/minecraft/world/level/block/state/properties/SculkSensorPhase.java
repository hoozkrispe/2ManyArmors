package net.minecraft.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum SculkSensorPhase implements StringRepresentable {
    INACTIVE("inactive"),
    ACTIVE("active"),
    COOLDOWN("cooldown");

    private final String name;

    private SculkSensorPhase(final String pName) {
        this.name = pName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}