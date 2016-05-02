package com.massivecraft.factions.iface;

import com.massivecraft.factions.struct.Rel;
import org.bukkit.ChatColor;

public interface RelationParticipator {

    public String describeTo(RelationParticipator observer);

    public String describeTo(RelationParticipator observer, boolean ucfirst);

    public Rel getRelationTo(RelationParticipator observer);

    public Rel getRelationTo(RelationParticipator observer, boolean ignorePeaceful);

    public ChatColor getColorTo(RelationParticipator observer);

}
