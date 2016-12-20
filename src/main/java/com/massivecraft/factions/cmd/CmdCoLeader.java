package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Rel;

public class CmdCoLeader extends FCommand {

    public CmdCoLeader() {
        aliases.add("col");

        requiredArgs.add("player coleader");

        permission = Permission.COLEADER.node;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = true;
    }

    @Override
    public void perform() {
        if (!fme.hasFaction()) {
            return;
        }

        Faction faction = fme.getFaction();

        if (assertMinRole(Rel.LEADER)) {
            FPlayer target = this.argAsFPlayer(0);

            if (faction.getFPlayers().contains(target)) {
                fme.msg("<i>" + target.getName() + " is now coleader of your faction.");
                target.setRole(Rel.LEADER);
            }
        }
    }
}
