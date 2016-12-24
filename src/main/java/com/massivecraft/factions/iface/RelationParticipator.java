package com.massivecraft.factions.iface;

import com.massivecraft.factions.struct.Rel;
import org.bukkit.ChatColor;

public interface RelationParticipator {
    String describeTo(RelationParticipator observer);

    String describeTo(RelationParticipator observer, boolean uppercaseFirst);

    Rel getRelationTo(RelationParticipator observer);

    Rel getRelationTo(RelationParticipator observer, boolean ignorePeaceful);

    ChatColor getColorTo(RelationParticipator observer);
}
