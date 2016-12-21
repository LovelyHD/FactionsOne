package com.massivecraft.factions.struct;

import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.util.LazyLocation;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Warp {
    private final String name;
    private final LazyLocation destination;
    private final String password;
    private final List<UUID> cache = new ArrayList<>();

    public Warp(String name, Player player, String password) {
        this.name = name;
        this.destination = new LazyLocation(player.getLocation());
        this.password = password;
    }

    public Warp(String name, Player player) {
        this(name, player, null);
    }

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }

    public boolean hasAccess(UUID uniqueId) {
        return cache.contains(uniqueId);
    }

    public void remember(UUID uniqueId) {
        cache.add(uniqueId);
    }

    public void forget(UUID uniqueId) {
        cache.remove(uniqueId);
    }

    public void teleport(Player player) {
        Location location = destination.getLocation();

        if (Essentials.handleTeleport(player, location)) {
            return;
        }

        player.teleport(location);
    }
}
