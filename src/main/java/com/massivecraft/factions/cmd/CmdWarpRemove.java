package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Warp;

import java.util.Optional;

public class CmdWarpRemove extends FCommand {

    public CmdWarpRemove() {
        super();

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
            fme.msg("<i>You are not in any faction.");
            return;
        }

        Faction faction = fme.getFaction();
        String name = argAsString(0);
        String password = argAsString(1, "none");

        Optional<Warp> optional = faction.getWarp(name);

        if (!optional.isPresent()) {

            return;
        }

        Warp warp = optional.get();

        if (warp.hasPassword()) {
            if (password.isEmpty()) {
                if (warp.hasAccess(me.getUniqueId())) {
                    faction.removeWarp(warp);
                } else {
                    fme.msg("<i>Please provide the password for that warp.");
                    return;
                }
            } else {
                if(warp.getPassword().equals(password)) {
                    faction.removeWarp(warp);
                } else {
                    fme.msg("<i>The password for that warp was not correct.");
                    //fail
                    return;
                }
            }
        } else {
            faction.removeWarp(warp);
        }

        fme.msg("<i>You successfully removed the faction warp " + warp.getName());
    }
}
