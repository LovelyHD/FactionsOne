package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.struct.Permission;
import io.github.dre2n.factionsone.api.event.FactionsReloadEvent;

public class CmdReload extends FCommand {

    public CmdReload() {
        super();
        aliases.add("reload");

        // this.requiredArgs.add("");
        optionalArgs.put("file", "all");

        permission = Permission.RELOAD.node;
        disableOnLock = false;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeOfficer = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        FactionsReloadEvent event = new FactionsReloadEvent();

        if (event.isCancelled()) {
            return;
        }

        long timeInitStart = System.currentTimeMillis();
        /* String file = this.argAsString(0, "all").toLowerCase();
		 * 
		 * String fileName;
		 * 
		 * if (file.startsWith("c")) { Conf.load(); fileName = "conf.json"; } else if
		 * (file.startsWith("b")) { Board.load(); fileName = "board.json"; } else if
		 * (file.startsWith("f")) { Factions.i.loadFromDisc(); fileName = "factions.json"; } else if
		 * (file.startsWith("p")) { FPlayers.i.loadFromDisc(); fileName = "players.json"; } else if
		 * (file.startsWith("a")) { fileName = "all"; Conf.load(); FPlayers.i.loadFromDisc();
		 * Factions.i.loadFromDisc(); Board.load(); } else {
		 * P.p.log("RELOAD CANCELLED - SPECIFIED FILE INVALID"); msg(
		 * "<b>Invalid file specified. <i>Valid files: all, conf, board, factions, players" );
		 * return; } */
        Conf.load();

        long timeReload = System.currentTimeMillis() - timeInitStart;

        // msg("<i>Reloaded <h>%s <i>from disk, took <h>%dms<i>.", fileName,
        // timeReload);
        msg("<i>Reloaded <h>config <i>from disk, took <h>%dms<i>.", timeReload);
    }

}
