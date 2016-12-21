package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Warp;

import java.util.stream.Collectors;

public class CmdWarpList extends FCommand {

    public CmdWarpList() {
        super();

        aliases.add("list");
        aliases.add("l");

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        if (!fme.hasFaction()) {
            fme.msg("<i>You are not in any faction.");
            return;
        }

        Faction faction = fme.getFaction();

        fme.msg("Warps: " + faction.getWarps()
                .stream()
                .map(Warp::getName)
                .collect(Collectors.joining(", ")));
    }
}
