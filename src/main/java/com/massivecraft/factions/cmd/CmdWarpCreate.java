package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.Warp;

import java.util.Optional;

public class CmdWarpCreate extends FCommand {
    public CmdWarpCreate() {
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
        if (!fme.hasFaction()) {
            Language.NO_FACTION.sendTo(fme);
            return;
        }

        Faction faction = fme.getFaction();

        if (faction.getWarps().size() == Conf.factionWarpLimit) {
            Language.WARP_LIMIT_REACHED.sendTo(fme);
            return;
        }

        if (!fme.isInOwnTerritory()) {
            Language.NOT_IN_TERRITORY.sendTo(fme);
            return;
        }

        String name = argAsString(0);
        Optional<Warp> warp = faction.getWarp(name);

        if (warp.isPresent()) {
            Language.WARP_NAME_EXISTS.sendTo(fme);
            return;
        }

        Language.WARP_CREATED.sendTo(fme);

        String password = argAsString(1, "");

        if (password.isEmpty()) {
            faction.addWarp(new Warp(name, me));
        } else {
            faction.addWarp(new Warp(name, me, password));
        }
    }
}
