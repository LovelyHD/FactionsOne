package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;

public class CmdLock extends FCommand {
	
	// TODO: This solution needs refactoring.
	/* factions.lock: description: use the /f lock [on/off] command to temporarily lock the data
	 * files from being overwritten default: op */
	
	public CmdLock() {
		super();
		aliases.add("lock");
		
		// this.requiredArgs.add("");
		optionalArgs.put("on/off", "flip");
		
		permission = Permission.LOCK.node;
		disableOnLock = false;
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		senderMustBeOfficer = false;
		senderMustBeLeader = false;
	}
	
	@Override
	public void perform() {
		p.setLocked(this.argAsBool(0, !p.getLocked()));
		
		if (p.getLocked()) {
			msg("<i>Factions is now locked");
		} else {
			msg("<i>Factions in now unlocked");
		}
	}
	
}
