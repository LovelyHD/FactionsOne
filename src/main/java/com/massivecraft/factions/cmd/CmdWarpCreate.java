package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Warp;
import com.massivecraft.factions.util.LazyLocation;

import java.util.Optional;

public class CmdWarpCreate extends FCommand {

    public CmdWarpCreate() {
        super();

        aliases.add("create");
        aliases.add("c");

        requiredArgs.add("name");

        optionalArgs.put("password", "warp password");

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        String name = argAsString(0);
        String password = argAsString(1, "none");

        Faction faction = fme.getFaction();

        if (fme.isInOwnTerritory() && !faction.isWilderness()) {
            fme.msg("<i>You have set a warp for your faction.");

            if(password.equals("none")) {
                faction.addWarp(new Warp(name, me));
            } else {
                faction.addWarp(new Warp(name, me, password));
            }
        }
    }
}
