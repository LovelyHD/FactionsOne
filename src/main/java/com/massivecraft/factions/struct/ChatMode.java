package com.massivecraft.factions.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.apache.commons.lang.WordUtils.capitalizeFully;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ChatMode {
    FACTION,
    ALLY(Rel.ALLY),
    TRUCE(Rel.TRUCE),
    PUBLIC;

    private Rel rel;

    public ChatMode next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public String getDisplayName() {
        return capitalizeFully(this.name());
    }
}
