package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Rel;

public class CmdPromote extends FCommand {

    public CmdPromote() {
        super();
        aliases.add("promote");

        requiredArgs.add("player name");
        // this.optionalArgs.put("", "");

        permission = Permission.PROMOTE.node;
        disableOnLock = true;

        // To promote someone from recruit -> member you must be an officer.
        // To promote someone from member -> officer you must be a leader.
        // We'll handle this internally
        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        FPlayer target = this.argAsBestFPlayerMatch(0);

        if (target == null) {
            return;
        }

        if (target.getFaction() != myFaction) {
            msg("%s<b> is not a member in your faction.", target.describeTo(fme, true));
            return;
        }

        if (target == fme) {
            msg("<b>The target player mustn't be yourself.");
            return;
        }

        if (target.getRole() == Rel.RECRUIT) {
            if (!fme.getRole().isAtLeast(Rel.OFFICER)) {
                msg("<b>You must be an officer to promote someone to member.");
                return;
            }
            target.setRole(Rel.MEMBER);
            myFaction.msg("%s<i> was promoted to member of your faction.", target.describeTo(myFaction, true));
        } else if (target.getRole() == Rel.MEMBER) {
            if (!fme.getRole().isAtLeast(Rel.LEADER)) {
                msg("<b>You must be the leader to promote someone to officer.");
                return;
            }
            // Give
            target.setRole(Rel.OFFICER);
            myFaction.msg("%s<i> was promoted to officer in your faction.", target.describeTo(myFaction, true));
        } else if (target.getRole() == Rel.OFFICER) {

            if (!fme.getRole().isAtLeast(Rel.LEADER)) {
                msg("<b>You must be the leader to promote someone to coleader.");
                return;
            }
            // Give
            target.setRole(Rel.COLEADER);
            myFaction.msg("%s<i> was promoted to coleader in your faction.", target.describeTo(myFaction, true));
        }
    }
}
