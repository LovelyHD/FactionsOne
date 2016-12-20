package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.struct.Warp;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CmdWarp extends FCommand {
    private final CmdWarpCreate cmdWarpCreate = new CmdWarpCreate();
    private final CmdWarpRemove cmdWarpRemove = new CmdWarpRemove();
    private final CmdWarpList cmdWarpList = new CmdWarpList();

    public CmdWarp() {
        aliases.add("warp");
        aliases.add("w");

        optionalArgs.put("name", "warp name");
        optionalArgs.put("password", "warp password");

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;

        addSubCommand(cmdWarpCreate);
        addSubCommand(cmdWarpRemove);
        addSubCommand(cmdWarpList);
    }

    @Override
    public void perform() {
        String name = argAsString(0, "");
        String password = argAsString(1, "");

        commandChain.add(this);

        // make sure name isn't empty
        if (name.isEmpty()) {
            return;
        }

        // make sure they have a faction
        if(!fme.hasFaction()) {
            fme.msg("<i>You are not in any faction.");
            return;
        }

        Faction faction = fme.getFaction();

        Optional<Warp> optional = faction.getWarp(name);

        if (!optional.isPresent()) {
            fme.msg("<i> The target warp doesn't exist.");
            return;
        }

        Warp warp = optional.get();

        if (warp.hasPassword()) {
            if (!password.isEmpty() && warp.getPassword().equals(password)) {
                tryWrap(me, warp);
            } else {
                fme.msg("<i>You did not enter the password for this warp correctly.");
            }
        } else {
            tryWrap(me, warp);
        }
    }

    private void tryWrap(Player player, Warp warp) {
        Location location = warp.getDestination().getLocation();

        // if Essentials teleport handling is enabled and available, pass the
        // teleport off to it (for delay and cooldown)
        if (Essentials.handleTeleport(me, location)) {
            return;
        }

        player.teleport(location);
    }
}
