package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Warp;

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
        String password = argAsString(1, "");

        if (!fme.hasFaction()) {
            fme.msg("<i>You do not belong to a faction.");
            return;
        }

        Faction faction = fme.getFaction();

        if (!fme.isInOwnTerritory()) {
            fme.msg("<i>You are not in your own territory.");
            return;
        }

        Optional<Warp> warp = faction.getWarp(name);

        if (warp.isPresent()) {
            fme.msg("<i>Your faction already has a warp by this name.");
            return;
        }

        fme.msg("<i>You have set a warp for your faction.");

        if (password.isEmpty()) {
            faction.addWarp(new Warp(name, me));
        } else {
            faction.addWarp(new Warp(name, me, password));
        }
    }
}
