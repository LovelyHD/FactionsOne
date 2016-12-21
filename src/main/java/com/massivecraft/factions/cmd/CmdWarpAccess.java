package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Warp;

import java.util.Optional;

public class CmdWarpAccess extends FCommand {

    public CmdWarpAccess() {
        super();

        aliases.add("access");
        aliases.add("a");

        requiredArgs.add("name");
        requiredArgs.add("member");

        optionalArgs.put("password", "warp password");

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        if (!fme.hasFaction()) {
            fme.msg("<i>You are not in any faction.");
            return;
        }

        Faction faction = fme.getFaction();
        String name = argAsString(0, "");
        FPlayer fPlayer = argAsFPlayer(1);
        String password = argAsString(2, "");

        Optional<Warp> optional = faction.getWarp(name);

        if (!optional.isPresent()) {
            fme.msg("<i>Your faction does not have a warp by this name.");
            return;
        }

        Warp warp = optional.get();

        if (warp.hasPassword()) {
            if (password.isEmpty()) {
                if (warp.hasAccess(me.getUniqueId())) {
                    if (warp.hasAccess(fPlayer.getUniqueId())) {
                        fme.msg("<i>" + fPlayer.getName() + " has been removed from this warp.");
                        warp.forget(fPlayer.getUniqueId());
                    } else {
                        fme.msg("<i>" + fPlayer.getName() + " has been added to this warp.");
                        warp.remember(fPlayer.getUniqueId());
                    }
                } else {
                    fme.msg("<i>Please enter the password for this warp.");
                }
            } else {
                if (warp.getPassword().equals(password)) {
                    if (warp.hasAccess(fPlayer.getUniqueId())) {
                        fme.msg("<i>" + fPlayer.getName() + " has been removed from this warp.");
                        warp.forget(fPlayer.getUniqueId());
                    } else {
                        fme.msg("<i>" + fPlayer.getName() + " has been added to this warp.");
                        warp.remember(fPlayer.getUniqueId());
                    }
                } else {
                    fme.msg("<i>The password you provided was incorrect.");
                }
            }
        } else {
            if (warp.hasAccess(fPlayer.getUniqueId())) {
                fme.msg("<i>" + fPlayer.getName() + " has been removed from this warp.");
                warp.forget(fPlayer.getUniqueId());
            } else {
                fme.msg("<i>" + fPlayer.getName() + " has been added to this warp.");
                warp.remember(fPlayer.getUniqueId());
            }
        }
    }
}
