package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.FPerm;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.TerritoryAccess;
import com.massivecraft.factions.zcore.util.TextUtil;
import io.github.dre2n.factionsone.config.FMessages;

public class CmdAccess extends FCommand {

    public CmdAccess() {
        super();
        aliases.add("access");

        optionalArgs.put("view|p|f|player|faction", "view");
        optionalArgs.put("name", "you");

        setHelpShort("view or grant access for the claimed territory you are in");

        disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        String type = this.argAsString(0);
        type = type == null ? "" : type.toLowerCase();
        FLocation loc = new FLocation(me.getLocation());

        TerritoryAccess territory = Board.getTerritoryAccessAt(loc);
        Faction locFaction = territory.getHostFaction();
        boolean accessAny = Permission.ACCESS_ANY.has(sender, false);

        if (type.isEmpty() || type.equals("view")) {
            if (!accessAny && !Permission.ACCESS_VIEW.has(sender, true)) {
                return;
            }
            if (!accessAny && !territory.doesHostFactionMatch(fme)) {
                msg(FMessages.CMD_ACCESS_LIST1.getMessage());
                return;
            }
            showAccessList(territory, locFaction);
            return;
        }

        if (!accessAny && !Permission.ACCESS.has(sender, true)) {
            return;
        }
        if (!accessAny && !FPerm.ACCESS.has(fme, locFaction, true)) {
            return;
        }

        boolean doPlayer = true;
        if (type.equals("f") || type.equals("faction")) {
            doPlayer = false;
        } else if (!type.equals("p") && !type.equals("player")) {
            msg(FMessages.ERROR_SPECIFY_PLAYER_OR_FACTION.getMessage());
            msg(FMessages.CMD_ACCESS_LIST2.getMessage());
            msg(FMessages.CMD_ACCESS_LIST3.getMessage());
            return;
        }

        String target = "";
        boolean added;

        if (doPlayer) {
            FPlayer targetPlayer = this.argAsBestFPlayerMatch(1, fme);
            if (targetPlayer == null) {
                return;
            }
            added = territory.toggleFPlayer(targetPlayer);
            target = "Player \"" + targetPlayer.getName() + "\"";
        } else {
            Faction targetFaction = this.argAsFaction(1, myFaction);
            if (targetFaction == null) {
                return;
            }
            added = territory.toggleFaction(targetFaction);
            target = "Faction \"" + targetFaction.getTag() + "\"";
        }

        if (added) {
            msg(FMessages.CMD_ACCESS_LIST4.getMessage(), target);
        } else {
            msg(FMessages.CMD_ACCESS_LIST5.getMessage(), target);
        }

        showAccessList(territory, locFaction);
    }

    private void showAccessList(TerritoryAccess territory, Faction locFaction) {
        msg("<i>Host faction %s has %s<i> in this territory.", locFaction.getTag(), TextUtil.parseColor(territory.isHostFactionAllowed() ? "<lime>normal access" : "<rose>restricted access"));
        String players = territory.fplayerList();
        String factions = territory.factionList();

        if (factions.isEmpty()) {
            msg("No factions have been explicitly granted access.");
        } else {
            msg("Factions with explicit access: " + factions);
        }

        if (players.isEmpty()) {
            msg("No players have been explicitly granted access.");
        } else {
            msg("Players with explicit access: " + players);
        }
    }

}
