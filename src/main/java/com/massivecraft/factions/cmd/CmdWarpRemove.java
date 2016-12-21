package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.Warp;

import java.util.Optional;

public class CmdWarpRemove extends FCommand {
    public CmdWarpRemove() {
        aliases.add("remove");
        aliases.add("r");

        requiredArgs.add("name");

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
        String name = argAsString(0);
        String password = argAsString(1, "none");

        Optional<Warp> optional = faction.getWarp(name);

        if (!optional.isPresent()) {
            Language.WARP_INVALID.sendTo(fme);
            return;
        }

        Warp warp = optional.get();

        if (warp.hasPassword()) {
            if (password.isEmpty()) {
                if (warp.hasAccess(me.getUniqueId())) {
                    faction.removeWarp(warp);
                } else {
                    Language.WARP_PASSWORD_INCORRECT.sendTo(fme);
                    return;
                }
            } else {
                if (warp.getPassword().equals(password)) {
                    faction.removeWarp(warp);
                } else {
                    Language.WARP_PASSWORD_INCORRECT.sendTo(fme);
                    return;
                }
            }
        } else {
            faction.removeWarp(warp);
        }

        Language.WARP_REMOVED.sendTo(fme);
    }
}
