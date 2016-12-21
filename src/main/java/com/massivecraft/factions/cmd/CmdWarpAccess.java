package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.Warp;

import java.util.Optional;

public class CmdWarpAccess extends FCommand {
    public CmdWarpAccess() {
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
            Language.NO_FACTION.sendTo(fme);
            return;
        }

        Faction faction = fme.getFaction();
        String name = argAsString(0, "");
        FPlayer fPlayer = argAsFPlayer(1);
        String password = argAsString(2, "");

        Optional<Warp> optional = faction.getWarp(name);

        if (!optional.isPresent()) {
            Language.WARP_INVALID.sendTo(fme);
            return;
        }

        Warp warp = optional.get();

        if (warp.hasPassword()) {
            if (password.isEmpty()) {
                if (warp.hasAccess(me.getUniqueId())) {
                    changeAccess(fPlayer, warp, warp.hasAccess(fPlayer.getUniqueId()));
                } else {
                    Language.WARP_PASSWORD_INCORRECT.sendTo(fme);
                }
            } else {
                if (warp.getPassword().equals(password)) {
                    changeAccess(fPlayer, warp, warp.hasAccess(fPlayer.getUniqueId()));
                } else {
                    Language.WARP_PASSWORD_INCORRECT.sendTo(fme);
                }
            }
        } else {
            changeAccess(fPlayer, warp, warp.hasAccess(fPlayer.getUniqueId()));
        }
    }

    private void changeAccess(FPlayer fPlayer, Warp warp, boolean state) {
        if (state) {
            Language.WARP_ACCESS_CHANGED.sendTo(fme,
                    "%player%", fPlayer.getName(),
                    "%state%", "removed");

            warp.forget(fPlayer.getUniqueId());
            return;
        }

        Language.WARP_ACCESS_CHANGED.sendTo(fme,
                "%player%", fPlayer.getName(),
                "%state%", "added");

        warp.remember(fPlayer.getUniqueId());
    }
}
