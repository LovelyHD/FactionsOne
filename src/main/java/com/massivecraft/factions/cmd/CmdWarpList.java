package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.Warp;

import java.util.stream.Collectors;

public class CmdWarpList extends FCommand {
    public CmdWarpList() {
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
            Language.NO_FACTION.sendTo(fme);
            return;
        }

        String warps = fme.getFaction().getWarps()
                .stream()
                .map(Warp::getName)
                .collect(Collectors.joining(", "));

        if (warps.isEmpty()) {
            Language.WARP_EMPTY.sendTo(fme);
        } else {
            Language.WARP_LIST_PREFIX.sendTo(fme,
                    "%warps%", warps);
        }
    }
}
