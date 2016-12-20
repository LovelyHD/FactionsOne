package com.massivecraft.factions.struct;

import com.massivecraft.factions.util.LazyLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Warp {
    private final String name;
    private final LazyLocation location;

    private final Optional<String> password;
    private final Optional<List<UUID>> accessors;

    public boolean hasAccess(UUID uniqueId) {
        return accessors.isPresent() && accessors.get().contains(uniqueId);
    }
}
