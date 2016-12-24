package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Language;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import static java.lang.Math.round;
import static org.bukkit.block.BlockFace.*;

public class CmdClaimLine extends FCommand {
    private final BlockFace[] axis = {SOUTH, WEST, NORTH, EAST};

    public CmdClaimLine() {
        aliases.add("line");
        aliases.add("l");

        optionalArgs.put("distance", "1");
        optionalArgs.put("direction", "facing");
        optionalArgs.put("faction", "you");

        disableOnLock = true;
        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        int distance = argAsInt(0, 1);

        if (distance > Conf.claimLineLimit) {
            Language.CLAIM_LINE_LIMIT.sendTo(fme);
            return;
        }

        BlockFace direction = null;

        for (BlockFace face : axis) {
            if (face.name().equalsIgnoreCase(argAsString(1))) {
                direction = face;
                break;
            }
        }

        Location location = me.getLocation();

        if (direction == null) {
            direction = axis[round(location.getYaw() / 90.0F) & 3];
        }

        Faction faction = argAsFaction(2, fme.getFaction());

        for (int i = 0; i < distance; i++) {
            fme.attemptClaim(faction, location, true);
            location = location.add((direction.getModX() * 16), 0.0D, (direction.getModZ() * 16));
        }
    }
}
