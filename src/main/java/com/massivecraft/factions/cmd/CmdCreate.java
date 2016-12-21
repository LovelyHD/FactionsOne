package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Rel;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class CmdCreate extends FCommand {
    public CmdCreate() {
        aliases.add("create");

        requiredArgs.add("faction tag");

        permission = Permission.CREATE.node;
        disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        String tag = argAsString(0);

        if (fme.hasFaction()) {
            Language.MUST_LEAVE_CURRENT_FACTION.sendTo(fme);
            return;
        }

        if (Factions.i.isTagTaken(tag)) {
            Language.TAG_IN_USE.sendTo(fme);
            return;
        }

        ArrayList<String> tagValidationErrors = Factions.validateTag(tag);
        if (tagValidationErrors.size() > 0) {
            sendMessage(tagValidationErrors);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this
        // command has a cost set, make sure they can pay
        if (!canAffordCommand(Conf.econCostCreate, "to create a new faction")) {
            return;
        }

        // trigger the faction creation event (cancellable)
        FactionCreateEvent createEvent = new FactionCreateEvent(me, tag);
        Bukkit.getServer().getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (!payForCommand(Conf.econCostCreate, "to create a new faction", "for creating a new faction")) {
            return;
        }

        Faction faction = Factions.i.create();
        faction.setTag(tag);

        // trigger the faction join event for the creator
        FPlayerJoinEvent joinEvent = new FPlayerJoinEvent(FPlayers.i.get(me), faction, FPlayerJoinEvent.PlayerJoinReason.CREATE);
        Bukkit.getServer().getPluginManager().callEvent(joinEvent);
        // join event cannot be cancelled or you'll have an empty faction

        // finish setting up the FPlayer
        fme.setRole(Rel.LEADER);
        fme.setFaction(faction);

        for (FPlayer follower : FPlayers.i.getOnline()) {
            Language.FACTION_CREATED.sendTo(follower,
                    "%player%", fme.describeTo(follower, true),
                    "%tag%", faction.getTag(follower));
        }

        msg("<i>You should now: %s", p.cmdBase.cmdDescription.getUseageTemplate());

        if (Conf.logFactionCreate) {
            P.p.log(fme.getName() + " created a new faction: " + tag);
        }
    }
}
