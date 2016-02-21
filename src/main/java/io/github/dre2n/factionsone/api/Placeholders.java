package io.github.dre2n.factionsone.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.struct.Rel;
import com.massivecraft.factions.zcore.util.TextUtil;

public enum Placeholders {
	
	FACTION_LEADER("%faction_leader%"),
	FACTION_MEMBER_LIST("%faction_member_list%"),
	FACTION_OFFICER_LIST("%faction_officer_list%"),
	FACTION_PLAYER_COUNT("%faction_player_count%"),
	FACTION_PLAYER_LIST("%faction_player_list%"),
	FACTION_RECRUIT_LIST("%faction_recruit_list%"),
	FACTION_TAG("%faction_tag%"),
	RELATION("%relation%"),
	RELATION_COLOR("%relation_color%");
	
	private String placeholder;
	
	Placeholders(String placeholder) {
		this.placeholder = placeholder;
	}
	
	/**
	 * @return the placeholder
	 */
	public String getPlaceholder() {
		return placeholder;
	}
	
	@Override
	public String toString() {
		return placeholder;
	}
	
	// Statics
	
	/**
	 * Replace the faction placeholders in a String automatically.
	 * 
	 * @param string
	 * the String that contains the placeholders
	 * @param faction
	 * the faction the replacements are taken from
	 */
	public static String replaceFactionPlaceholders(String string, Faction faction) {
		string.replaceAll(FACTION_LEADER.toString(), faction.getFPlayerLeader().getName());
		string.replaceAll(FACTION_MEMBER_LIST.toString(), TextUtil.implodeCommaAnd(getNames(faction.getFPlayersWhereRole(Rel.MEMBER))));
		string.replaceAll(FACTION_OFFICER_LIST.toString(), TextUtil.implodeCommaAnd(getNames(faction.getFPlayersWhereRole(Rel.OFFICER))));
		string.replaceAll(FACTION_PLAYER_COUNT.toString(), String.valueOf(faction.getFPlayers().size()));
		string.replaceAll(FACTION_PLAYER_LIST.toString(), TextUtil.implodeCommaAnd(getNames(faction.getFPlayers())));
		string.replaceAll(FACTION_RECRUIT_LIST.toString(), TextUtil.implodeCommaAnd(getNames(faction.getFPlayersWhereRole(Rel.RECRUIT))));
		string.replaceAll(FACTION_TAG.toString(), faction.getTag());
		return string;
	}
	
	/**
	 * Replace the relation placeholders in a String automatically.
	 * 
	 * @param string
	 * the String that contains the placeholders
	 * @param standpoint
	 * the standpoint of this Faction will be chosen for the relation purposes
	 * @param object
	 * the Faction or FPlayer to compare to the standpoint faction
	 */
	public static String replaceRelationPlaceholders(String string, Faction standpoint, RelationParticipator object) {
		string.replaceAll(RELATION.toString(), standpoint.getRelationTo(object).toString());
		string.replaceAll(RELATION_COLOR.toString(), standpoint.getColorTo(object).toString());
		return string;
	}
	
	public static List<String> getNames(Collection<FPlayer> fPlayers) {
		List<String> names = new ArrayList<String>();
		
		for (FPlayer fPlayer : fPlayers) {
			names.add(fPlayer.getName());
		}
		
		return names;
	}
	
}
