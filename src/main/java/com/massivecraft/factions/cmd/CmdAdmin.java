package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.Permission;

public class CmdAdmin extends FCommand {
    public CmdAdmin() {
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

        Language.ADMIN_BYPASS.sendTo(fme,
                "%state%", status);

        Language.ADMIN_BYPASS_CONSOLE.log(
                "%player%", fme.getName(),
                "%state%", status);
    }
}
