package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import org.bukkit.ChatColor;

public class CmdMoneyTransferPf extends FCommand {

    public CmdMoneyTransferPf() {
        aliases.add("pf");

        requiredArgs.add("amount");
        requiredArgs.add("player");
        requiredArgs.add("faction");

        // this.optionalArgs.put("", "");
        permission = Permission.MONEY_P2F.node;
        setHelpShort("transfer p -> f");

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        double amount = this.argAsDouble(0, 0d);
        EconomyParticipator from = this.argAsBestFPlayerMatch(1);
        if (from == null) {
            return;
        }
        EconomyParticipator to = this.argAsFaction(2);
        if (to == null) {
            return;
        }

        boolean success = Econ.transferMoney(fme, from, to, amount);

        if (success && Conf.logMoneyTransactions) {
            P.p.log(ChatColor.stripColor(P.p.txt.parse("%s transferred %s from the player \"%s\" to the faction \"%s\"", fme.getName(), Econ.moneyString(amount), from.describeTo(null),
                    to.describeTo(null))));
        }
    }

}
