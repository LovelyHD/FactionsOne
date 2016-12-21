package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.FFlag;
import com.massivecraft.factions.struct.Permission;

public class CmdFlag extends FCommand {

    public CmdFlag() {
        super();
        aliases.add("flag");

        // this.requiredArgs.add("");
        optionalArgs.put("faction", "your");
        optionalArgs.put("flag", "all");
        optionalArgs.put("yes/no", "read");

        permission = Permission.FLAG.node;
        disableOnLock = true;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        Faction faction = myFaction;
        if (argIsSet(0)) {
            faction = this.argAsFaction(0);
        }
        if (faction == null) {
            if (senderIsConsole) {
                Language.NOT_ENOUGH_ARGS.sendTo(sender);
                sender.sendMessage(this.getUseageTemplate());
            }
            return;
        }

        if (!argIsSet(1)) {
            msg(p.txt.titleize("Flags for " + faction.describeTo(fme, true)));
            for (FFlag flag : FFlag.values()) {
                msg(flag.getStateInfo(faction.getFlag(flag), true));
            }
            return;
        }

        FFlag flag = this.argAsFactionFlag(1);
        if (flag == null) {
            return;
        }
        if (!argIsSet(2)) {
            msg(p.txt.titleize("Flag for " + faction.describeTo(fme, true)));
            msg(flag.getStateInfo(faction.getFlag(flag), true));
            return;
        }

        Boolean targetValue = this.argAsBool(2);
        if (targetValue == null) {
            return;
        }

        // Do the sender have the right to change flags?
        if (!Permission.FLAG_SET.has(sender, true)) {
            return;
        }

        // Do the change
        msg(p.txt.titleize("Flag for " + faction.describeTo(fme, true)));
        faction.setFlag(flag, targetValue);
        msg(flag.getStateInfo(faction.getFlag(flag), true));
    }

}
