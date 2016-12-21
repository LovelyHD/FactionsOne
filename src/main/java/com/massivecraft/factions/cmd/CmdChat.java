package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Language;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;

public class CmdChat extends FCommand {
    public CmdChat() {
        aliases.add("c");
        aliases.add("chat");

        optionalArgs.put("mode", "chat mode");

        permission = Permission.CHAT.node;
        disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        if (!fme.hasFaction()) {
            Language.NO_FACTION.sendTo(fme);
            return;
        }

        String mode = argAsString(0, "next").toLowerCase();

        if (mode.equals("next")) {
            ChatMode newMode = fme.getChatMode().next();

            Language.CHAT_MODE_CHANGED.sendTo(fme,
                    "%mode%", newMode.getDisplayName());

            fme.setChatMode(newMode);
            return;
        }

        for (ChatMode chatMode : ChatMode.values()) {
            if (chatMode.name().toLowerCase().startsWith(mode.substring(0, 1))) {
                Language.CHAT_MODE_CHANGED.sendTo(fme,
                        "%mode%", chatMode.getDisplayName());

                fme.setChatMode(chatMode);
                return;
            }
        }

        Language.CHAT_MODE_INVALID.sendTo(fme);
    }
}
