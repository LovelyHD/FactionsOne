package com.massivecraft.factions.struct;

import com.massivecraft.factions.util.LazyLocation;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
public class Warp {
    private final String name;
    private final LazyLocation destination;
    private final String password;
    private final List<UUID> accessors;

    public Warp(String name, Player player, String password, List<UUID> accessors) {
        this.name = name;
        this.destination = new LazyLocation(player.getLocation());
        this.password = password;
        this.accessors = accessors;
    }

    public Warp(String name, Player player, List<UUID> accessors) {
        this(name, player, null, accessors);
    }

    public Warp(String name, Player player, String password) {
        this(name, player, password, null);
    }

    public Warp(String name, Player player) {
        this(name, player, null, null);
    }

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }

    public boolean hasAccess(UUID uniqueId) {
        return accessors != null && accessors.contains(uniqueId);
    }
}
