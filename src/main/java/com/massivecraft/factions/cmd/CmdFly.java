package com.massivecraft.factions.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;

public class CmdFly extends FCommand {
	
	public CmdFly() {
		super();
		aliases.add("lovelyfly");
		
		
		
		
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		senderMustBeOfficer = false;
		senderMustBeLeader = false;
	}
	
	

	@Override
	public void perform() {
		
		FPlayer fplayer = this.argAsFPlayer(0);
		
		Player player = (Player) this.argAsFPlayer(0);
		
		if(fplayer.isInOwnTerritory()) {
			player.setAllowFlight(true);
			player.setFlying(true);
		} else {
			player.setAllowFlight(false);
			player.setFlying(false);
		}
		
		
	}

}
