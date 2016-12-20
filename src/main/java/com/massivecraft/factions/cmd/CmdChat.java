package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;

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

        boolean found = false;

        for (ChatMode chatMode : ChatMode.values()) {
            if (chatMode.name().toLowerCase().startsWith(mode.substring(0, 1))) {
                found = true;

                fme.msg("<i>Your chat mode has been set to " + chatMode.getDisplayName());
                fme.setChatMode(chatMode);
                return;
            }
        }

        if(!found) {
            fme.msg("<i>That chat mode doesn't exist!");
        }
    }
}
