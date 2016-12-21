package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Language;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;

public class CmdVersion extends FCommand {

    public CmdVersion() {
        aliases.add("version");

        permission = Permission.VERSION.node;

        disableOnLock = false;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        Language.VERSION.sendTo(sender,
                "%version%", P.p.getDescription().getFullName());
    }
}
