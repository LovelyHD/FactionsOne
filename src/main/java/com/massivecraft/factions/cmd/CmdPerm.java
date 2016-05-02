package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.FPerm;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Rel;
import com.massivecraft.factions.zcore.Lang;

public class CmdPerm extends FCommand {

    public CmdPerm() {
        super();
        aliases.add("perm");

        optionalArgs.put("faction", "your");
        optionalArgs.put("perm", "all");
        optionalArgs.put("relation", "read");
        optionalArgs.put("yes/no", "read");

        permission = Permission.PERM.node;
        disableOnLock = true;

        errorOnToManyArgs = false;

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
                msg(Lang.commandToFewArgs);
                sender.sendMessage(this.getUseageTemplate());
            }
            return;
        }

        if (!argIsSet(1)) {
            msg(p.txt.titleize("Perms for " + faction.describeTo(fme, true)));
            msg(FPerm.getStateHeaders());
            for (FPerm perm : FPerm.values()) {
                msg(perm.getStateInfo(faction.getPermittedRelations(perm), true));
            }
            return;
        }

        FPerm perm = this.argAsFactionPerm(1);
        if (perm == null) {
            return;
        }
        if (!argIsSet(2)) {
            msg(p.txt.titleize("Perm for " + faction.describeTo(fme, true)));
            msg(FPerm.getStateHeaders());
            msg(perm.getStateInfo(faction.getPermittedRelations(perm), true));
            return;
        }

        // Do the sender have the right to change perms for this faction?
        if (!FPerm.PERMS.has(sender, faction, true)) {
            return;
        }

        Rel rel = this.argAsRel(2);
        if (rel == null) {
            return;
        }

        Boolean val = this.argAsBool(3, null);
        if (val == null) {
            return;
        }

        // Do the change
        faction.setRelationPermitted(perm, rel, val);

        // The following is to make sure the leader always has the right to
        // change perms if that is our goal.
        if (perm == FPerm.PERMS && FPerm.PERMS.getDefault().contains(Rel.LEADER)) {
            faction.setRelationPermitted(FPerm.PERMS, Rel.LEADER, true);
        }

        msg(p.txt.titleize("Perm for " + faction.describeTo(fme, true)));
        msg(FPerm.getStateHeaders());
        msg(perm.getStateInfo(faction.getPermittedRelations(perm), true));
    }

}
