package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.Patch;
import com.massivecraft.factions.struct.Permission;

public class CmdVersion extends FCommand {

    public CmdVersion() {
        aliases.add("version");

        // this.requiredArgs.add("");
        // this.optionalArgs.put("", "");
        permission = Permission.VERSION.node;
        disableOnLock = false;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        msg("<i>You are running " + P.p.getDescription().getFullName() + "\nPatch: " + Patch.getFullName());
    }

}
