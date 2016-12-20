package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.event.LandUnclaimEvent;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.FPerm;
import com.massivecraft.factions.struct.Permission;
import org.bukkit.Bukkit;

public class CmdUnclaim extends FCommand {

    public CmdUnclaim() {
        aliases.add("unclaim");
        aliases.add("declaim");

        // this.requiredArgs.add("");
        // this.optionalArgs.put("", "");
        permission = Permission.UNCLAIM.node;
        disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        FLocation flocation = new FLocation(fme);
        Faction otherFaction = Board.getFactionAt(flocation);

        if (!FPerm.TERRITORY.has(sender, otherFaction, true)) {
            return;
        }

        LandUnclaimEvent unclaimEvent = new LandUnclaimEvent(flocation, otherFaction, fme);
        Bukkit.getServer().getPluginManager().callEvent(unclaimEvent);
        if (unclaimEvent.isCancelled()) {
            return;
        }

        // String moneyBack = "<i>";
        if (Econ.shouldBeUsed()) {
            double refund = Econ.calculateClaimRefund(myFaction.getLandRounded());

            if (Conf.bankEnabled && Conf.bankFactionPaysLandCosts) {
                if (!Econ.modifyMoney(myFaction, refund, "to unclaim this land", "for unclaiming this land")) {
                    return;
                }
            } else if (!Econ.modifyMoney(fme, refund, "to unclaim this land", "for unclaiming this land")) {
                return;
            }
        }

        Board.removeAt(flocation);
        myFaction.msg("%s<i> unclaimed some land.", fme.describeTo(myFaction, true));

        if (Conf.logLandUnclaims) {
            P.p.log(fme.getName() + " unclaimed land at (" + flocation.getCoordString() + ") from the faction: " + otherFaction.getTag());
        }
    }

}
