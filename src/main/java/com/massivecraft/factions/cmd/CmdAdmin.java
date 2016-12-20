package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;

public class CmdAdmin extends FCommand {

    public CmdAdmin() {
        super();
        aliases.add("admin");
        aliases.add("bypass");

        optionalArgs.put("on/off", "flip");

        permission = Permission.ADMIN.node;
        disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        fme.setHasAdminMode(argAsBool(0, !fme.hasAdminMode()));
        String status = fme.hasAdminMode() ? "enabled" : "disabled";

        fme.msg("<i>You have " + status + " admin bypass mode.");
        P.p.log(fme.getName() + " " + status.toUpperCase() + " admin bypass mode.");
    }
}
