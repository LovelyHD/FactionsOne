package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;

import static com.massivecraft.factions.struct.ChatMode.*;

public class CmdChat extends FCommand {

    public CmdChat() {
        aliases.add("c");
        aliases.add("chat");

        optionalArgs.put("mode", "next");

        permission = Permission.CHAT.node;
        disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        String mode = argAsString(0, "next").toLowerCase();

        if (mode.equals("next")) {
            ChatMode newMode = fme.getChatMode().next();

            fme.msg("<i>Your chat mode has been set to " + newMode.getDisplayName());
            fme.setChatMode(newMode);
            return;
        }

        switch (mode.substring(0, 1)) {

            case "p":
                fme.msg("<i>Your chat mode has been set to " + PUBLIC.getDisplayName());
                fme.setChatMode(PUBLIC);
                break;

            case "a":
                fme.msg("<i>Your chat mode has been set to " + ALLY.getDisplayName());
                fme.setChatMode(ALLY);
                break;

            case "t":
                fme.msg("<i>Your chat mode has been set to " + TRUCE.getDisplayName());
                fme.setChatMode(TRUCE);
                break;

            case "f":
                fme.msg("<i>Your chat mode has been set to " + FACTION.getDisplayName());
                fme.setChatMode(FACTION);
                break;

            default:
                fme.msg("<i>That chat mode is not valid.");
                break;
        }
    }
}
