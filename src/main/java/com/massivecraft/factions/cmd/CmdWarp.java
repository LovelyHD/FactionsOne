package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Warp;

import java.util.Optional;

public class CmdWarp extends FCommand {
    private final CmdWarpCreate cmdWarpCreate = new CmdWarpCreate();
    private final CmdWarpRemove cmdWarpRemove = new CmdWarpRemove();
    private final CmdWarpList cmdWarpList = new CmdWarpList();
    private final CmdWarpAccess cmdWarpAccess = new CmdWarpAccess();

    public CmdWarp() {
        aliases.add("warp");
        aliases.add("w");

        optionalArgs.put("name", "warp name");
        optionalArgs.put("password", "warp password");

        permission = Permission.WARP.node;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;

        addSubCommand(cmdWarpCreate);
        addSubCommand(cmdWarpRemove);
        addSubCommand(cmdWarpList);
        addSubCommand(cmdWarpAccess);
    }

    @Override
    public void perform() {
        String name = argAsString(0, "");
        String password = argAsString(1, "");

        commandChain.add(this);

        // make sure name isn't empty
        if (name.isEmpty()) {
            cmdWarpList.execute(sender, args, commandChain);
            return;
        }

        // make sure they have a faction
        if (!fme.hasFaction()) {
            Language.NO_FACTION.sendTo(fme);
            return;
        }

        Faction faction = fme.getFaction();

        Optional<Warp> optional = faction.getWarp(name);

        if (!optional.isPresent()) {
            Language.WARP_INVALID.sendTo(fme);
            return;
        }

        Warp warp = optional.get();

        if (warp.hasPassword()) {
            if (warp.hasAccess(me.getUniqueId())) {
                Language.WARP_TELEPORTED.sendTo(fme,
                        "%warp%", warp.getName());

                warp.teleport(me);
            } else {
                if (!password.isEmpty() && warp.getPassword().equals(password)) {
                    Language.WARP_TELEPORTED.sendTo(fme,
                            "%warp%", warp.getName());

                    Language.WARP_REMEMBER_ME.sendTo(fme);

                    warp.teleport(me);
                    warp.remember(me.getUniqueId());
                } else {
                    Language.WARP_PASSWORD_INCORRECT.sendTo(fme);
                }
            }
        } else {
            Language.WARP_TELEPORTED.sendTo(fme,
                    "%warp%", warp.getName());

            warp.teleport(me);
        }
    }
}
