package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;

public class CmdAdmin extends FCommand {
	public CmdAdmin() {
		super();
		aliases.add("admin");
		
		// this.requiredArgs.add("");
		optionalArgs.put("on/off", "flip");
		
		permission = Permission.ADMIN.node;
		disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		senderMustBeOfficer = false;
		senderMustBeLeader = false;
	}
	
	@Override
	public void perform() {
		fme.setHasAdminMode(this.argAsBool(0, !fme.hasAdminMode()));
		
		if (fme.hasAdminMode()) {
			fme.msg("<i>You have enabled admin bypass mode.");
			P.p.log(fme.getName() + " has ENABLED admin bypass mode.");
		} else {
			fme.msg("<i>You have disabled admin bypass mode.");
			P.p.log(fme.getName() + " DISABLED admin bypass mode.");
		}
	}
}
