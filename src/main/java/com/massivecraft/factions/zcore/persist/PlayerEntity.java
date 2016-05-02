package com.massivecraft.factions.zcore.persist;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerEntity extends Entity {

    public Player getPlayer() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getUniqueId().toString().equals(getId())) {
                return player;
            }
        }
        return null;
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    // make sure target player should be able to detect that this player is
    // online
    public boolean isOnlineAndVisibleTo(Player player) {
        Player target = getPlayer();
        return target != null && player.canSee(target);
    }

    public boolean isOffline() {
        return !isOnline();
    }

    // -------------------------------------------- //
    // Message Sending Helpers
    // -------------------------------------------- //
    public void sendMessage(String msg) {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        player.sendMessage(msg);
    }

    public void sendMessage(List<String> msgs) {
        for (String msg : msgs) {
            this.sendMessage(msg);
        }
    }

}
