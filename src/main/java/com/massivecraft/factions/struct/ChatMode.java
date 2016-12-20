package com.massivecraft.factions.struct;

import static org.apache.commons.lang.WordUtils.capitalizeFully;

public enum ChatMode {
    FACTION,
    ALLY,
    TRUCE,
    PUBLIC;

    private static ChatMode[] modes = values();

    public ChatMode next() {
        return modes[(ordinal() + 1) % modes.length];
    }

    public String getDisplayName() {
        return capitalizeFully(this.name());
    }
}
