package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.LandUnclaimEvent;
import com.massivecraft.factions.struct.FPerm;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.SpiralTask;
import org.bukkit.Bukkit;

public class CmdUnclaim extends FCommand {

    public CmdUnclaim() {
        aliases.add("unclaim");
        aliases.add("declaim");

        optionalArgs.put("radius", "1");
        optionalArgs.put("faction", "your");

        permission = Permission.UNCLAIM.node;
        disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        // Read and validate input
        int radius = this.argAsInt(0, 1);
        Faction forFaction = this.argAsFaction(1, myFaction);

        if (radius < 1) {
            msg("<b>If you specify a radius, it must be at least 1.");
            return;
        }

        if (radius < 2) {
            // single chunk
            unclaimLand(fme, new FLocation(fme));
        } else {
            // radius claim
            if (!Permission.UNCLAIM_RADIUS.has(sender, false)) {
                msg("<b>You do not have permission to unclaim in a radius.");
                return;
            }

            new SpiralTask(new FLocation(me), radius) {
                private int failCount = 0;
                private final int limit = Conf.radiusUnclaimFailureLimit - 1;

                @Override
                public boolean work() {
                    boolean success = unclaimLand(fme, new FLocation(currentLocation()));

                    if (success) {
                        failCount = 0;
                    } else if (!success && failCount++ >= limit) {
                        stop();
                        return false;
                    }

                    return true;
                }
            };
        }
    }

    private boolean unclaimLand(FPlayer sender, FLocation location) {
        Faction otherFaction = Board.getFactionAt(location);

        if (!FPerm.TERRITORY.has(sender, otherFaction, true)) {
            return false;
        }

        LandUnclaimEvent unclaimEvent = new LandUnclaimEvent(location, otherFaction, fme);
        Bukkit.getServer().getPluginManager().callEvent(unclaimEvent);
        if (unclaimEvent.isCancelled()) {
            return false;
        }

        Board.removeAt(location);
        myFaction.msg("%s<i> unclaimed some land.", fme.describeTo(myFaction, true));

        if (Conf.logLandUnclaims) {
            P.p.log(fme.getName() + " unclaimed land at (" + location.getCoordString() + ") from the faction: " + otherFaction.getTag());
        }

        return true;
    }
}
