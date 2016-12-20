package com.massivecraft.factions;

import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.FFlag;
import com.massivecraft.factions.struct.FPerm;
import com.massivecraft.factions.struct.Rel;
import com.massivecraft.factions.struct.Warp;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.zcore.persist.Entity;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Faction extends Entity implements EconomyParticipator {
    // FIELD: relationWish

    private Map<String, Rel> relationWish;

    // FIELD: fplayers
    // speedy lookup of players in faction
    private transient Set<FPlayer> fplayers = new HashSet<>();

    // FIELD: invites
    // Where string is a lowercase player name
    private Set<String> invites;

    public Set<String> getInvites() {
        return invites;
    }

    public void invite(FPlayer fplayer) {
        invites.add(fplayer.getId());
    }

    public void deinvite(FPlayer fplayer) {
        invites.remove(fplayer.getId());
    }

    public boolean isInvited(FPlayer fplayer) {
        return invites.contains(fplayer.getId());
    }

    //FIELD: warps
    @Getter
    private List<Warp> warps;

    public void addWarp(Warp warp) {
        warps.add(warp);
    }

    // FIELD: open
    private boolean open;

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean isOpen) {
        open = isOpen;
    }

    // FIELD: tag
    private String tag;

    public String getTag() {
        return tag;
    }

    public String getTag(String prefix) {
        return prefix + tag;
    }

    public String getTag(RelationParticipator observer) {
        if (observer == null) {
            return getTag();
        }
        return this.getTag(getColorTo(observer).toString());
    }

    public void setTag(String str) {
        if (Conf.factionTagForceUpperCase) {
            str = str.toUpperCase();
        }
        tag = str;
    }

    public String getComparisonTag() {
        return MiscUtil.getComparisonString(tag);
    }

    // FIELD: description
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description = value;
    }

    // FIELD: home
    private LazyLocation home;

    public void setHome(Location home) {
        this.home = new LazyLocation(home);
    }

    public boolean hasHome() {
        return getHome() != null;
    }

    public Location getHome() {
        confirmValidHome();
        return home != null ? home.getLocation() : null;
    }

    public void confirmValidHome() {
        if (!Conf.homesMustBeInClaimedTerritory || home == null || home.getLocation() != null && Board.getFactionAt(new FLocation(home.getLocation())) == this) {
            return;
        }

        msg("<b>Your faction home has been un-set since it is no longer in your territory.");
        home = null;
    }

    // FIELD: lastOnlineTime
    private long lastOnlineTime;

    // FIELD: account (fake field)
    // Bank functions
    public double money;

    @Override
    public String getAccountId() {
        String aid = "faction-" + getId();

        // We need to override the default money given to players.
        if (!Econ.hasAccount(aid)) {
            Econ.setBalance(aid, 0);
        }

        return aid;
    }

    // FIELD: powerBoost
    // special increase/decrease to default and max power for this faction
    private double powerBoost;

    public double getPowerBoost() {
        return powerBoost;
    }

    public void setPowerBoost(double powerBoost) {
        this.powerBoost = powerBoost;
    }

    // FIELDS: Flag management
    // TODO: This will save... defaults if they where changed to...
    private Map<FFlag, Boolean> flagOverrides; // Contains the modifications to
    // the default values

    public boolean getFlag(FFlag flag) {
        Boolean ret = flagOverrides.get(flag);
        if (ret == null) {
            ret = flag.getDefault();
        }
        return ret;
    }

    public void setFlag(FFlag flag, boolean value) {
        if (Conf.factionFlagDefaults.get(flag).equals(value)) {
            flagOverrides.remove(flag);
            return;
        }
        flagOverrides.put(flag, value);
    }

    // FIELDS: Permission <-> Groups management
    private Map<FPerm, Set<Rel>> permOverrides; // Contains the modifications to
    // the default values

    public Set<Rel> getPermittedRelations(FPerm perm) {
        Set<Rel> ret = permOverrides.get(perm);
        if (ret == null) {
            ret = perm.getDefault();
        }
        return ret;
    }

    /* public void addPermittedRelation(FPerm perm, Rel rel) { Set<Rel> newPermittedRelations =
     * EnumSet.noneOf(Rel.class); newPermittedRelations.addAll(this.getPermittedRelations(perm));
	 * newPermittedRelations.add(rel); this.setPermittedRelations(perm, newPermittedRelations); }
	 * 
	 * public void removePermittedRelation(FPerm perm, Rel rel) { Set<Rel> newPermittedRelations =
	 * EnumSet.noneOf(Rel.class); newPermittedRelations.addAll(this.getPermittedRelations(perm));
	 * newPermittedRelations.remove(rel); this.setPermittedRelations(perm, newPermittedRelations); } */
    public void setRelationPermitted(FPerm perm, Rel rel, boolean permitted) {
        Set<Rel> newPermittedRelations = EnumSet.noneOf(Rel.class);
        newPermittedRelations.addAll(getPermittedRelations(perm));
        if (permitted) {
            newPermittedRelations.add(rel);
        } else {
            newPermittedRelations.remove(rel);
        }
        this.setPermittedRelations(perm, newPermittedRelations);
    }

    public void setPermittedRelations(FPerm perm, Set<Rel> rels) {
        if (perm.getDefault().equals(rels)) {
            permOverrides.remove(perm);
            return;
        }
        permOverrides.put(perm, rels);
    }

    public void setPermittedRelations(FPerm perm, Rel... rels) {
        Set<Rel> temp = new HashSet<>();
        temp.addAll(Arrays.asList(rels));
        this.setPermittedRelations(perm, temp);
    }

    // -------------------------------------------- //
    // Construct
    // -------------------------------------------- //
    public Faction() {
        relationWish = new HashMap<>();
        invites = new HashSet<>();
        open = Conf.newFactionsDefaultOpen;
        tag = "???";
        description = "Default faction description :(";
        money = 0.0;
        powerBoost = 0.0;
        flagOverrides = new LinkedHashMap<>();
        permOverrides = new LinkedHashMap<>();
        warps = new ArrayList<>();
    }

    // -------------------------------
    // Understand the types
    // -------------------------------
    // TODO: These should be gone after the refactoring...
    public boolean isNormal() {
        // return ! (this.isNone() || this.isSafeZone() || this.isWarZone());
        return !isNone();
    }

    public boolean isNone() {
        return getId().equals("0");
    }

    // -------------------------------
    // Relation and relation colors
    // -------------------------------
    @Override
    public String describeTo(RelationParticipator observer, boolean ucfirst) {
        return RelationUtil.describeThatToMe(this, observer, ucfirst);
    }

    @Override
    public String describeTo(RelationParticipator observer) {
        return RelationUtil.describeThatToMe(this, observer);
    }

    @Override
    public Rel getRelationTo(RelationParticipator observer) {
        return RelationUtil.getRelationOfThatToMe(this, observer);
    }

    @Override
    public Rel getRelationTo(RelationParticipator observer, boolean ignorePeaceful) {
        return RelationUtil.getRelationOfThatToMe(this, observer, ignorePeaceful);
    }

    @Override
    public ChatColor getColorTo(RelationParticipator observer) {
        return RelationUtil.getColorOfThatToMe(this, observer);
    }

    public Rel getRelationWish(Faction otherFaction) {
        if (relationWish.containsKey(otherFaction.getId())) {
            return relationWish.get(otherFaction.getId());
        }
        return Rel.NEUTRAL;
    }

    public void setRelationWish(Faction otherFaction, Rel relation) {
        if (relationWish.containsKey(otherFaction.getId()) && relation.equals(Rel.NEUTRAL)) {
            relationWish.remove(otherFaction.getId());
        } else {
            relationWish.put(otherFaction.getId(), relation);
        }
    }

    public Map<Rel, List<String>> getFactionTagsPerRelation(RelationParticipator rp) {
        return getFactionTagsPerRelation(rp, false);
    }

    // onlyNonNeutral option provides substantial performance boost on large
    // servers for listing only non-neutral factions
    public Map<Rel, List<String>> getFactionTagsPerRelation(RelationParticipator rp, boolean onlyNonNeutral) {
        Map<Rel, List<String>> ret = new HashMap<>();
        for (Rel rel : Rel.values()) {
            ret.put(rel, new ArrayList<String>());
        }
        for (Faction faction : Factions.i.get()) {
            Rel relation = faction.getRelationTo(this);
            if (onlyNonNeutral && relation == Rel.NEUTRAL) {
                continue;
            }
            ret.get(relation).add(faction.getTag(rp));
        }
        return ret;
    }

    // TODO: Implement a has enough feature.
    // ----------------------------------------------//
    // Power
    // ----------------------------------------------//
    public double getPower() {
        if (getFlag(FFlag.INFPOWER)) {
            return 999999;
        }

        double ret = 0;
        for (FPlayer fplayer : fplayers) {
            ret += fplayer.getPower();
        }
        if (Conf.powerFactionMax > 0 && ret > Conf.powerFactionMax) {
            ret = Conf.powerFactionMax;
        }
        return ret + powerBoost;
    }

    public double getPowerMax() {
        if (getFlag(FFlag.INFPOWER)) {
            return 999999;
        }

        double ret = 0;
        for (FPlayer fplayer : fplayers) {
            ret += fplayer.getPowerMax();
        }
        if (Conf.powerFactionMax > 0 && ret > Conf.powerFactionMax) {
            ret = Conf.powerFactionMax;
        }
        return ret + powerBoost;
    }

    public int getPowerRounded() {
        return (int) Math.round(getPower());
    }

    public int getPowerMaxRounded() {
        return (int) Math.round(getPowerMax());
    }

    public int getLandRounded() {
        return Board.getFactionCoordCount(this);
    }

    public int getLandRoundedInWorld(String worldName) {
        return Board.getFactionCoordCountInWorld(this, worldName);
    }

    public boolean hasLandInflation() {
        return getLandRounded() > getPowerRounded();
    }

    // -------------------------------
    // FPlayers
    // -------------------------------
    // maintain the reference list of FPlayers in this faction
    public void refreshFPlayers() {
        fplayers.clear();
        if (isNone()) {
            return;
        }

        for (FPlayer fplayer : FPlayers.i.get()) {
            if (fplayer.getFaction() == this) {
                fplayers.add(fplayer);
            }
        }
    }

    protected boolean addFPlayer(FPlayer fplayer) {
        if (isNone()) {
            return false;
        }

        return fplayers.add(fplayer);
    }

    protected boolean removeFPlayer(FPlayer fplayer) {
        if (isNone()) {
            return false;
        }

        return fplayers.remove(fplayer);
    }

    public Set<FPlayer> getFPlayers() {
        // return a shallow copy of the FPlayer list, to prevent tampering and
        // concurrency issues
        Set<FPlayer> ret = new HashSet<>(fplayers);
        return ret;
    }

    public Set<FPlayer> getFPlayersWhereOnline(boolean online) {
        Set<FPlayer> ret = new HashSet<>();

        for (FPlayer fplayer : fplayers) {
            if (fplayer.isOnline() == online) {
                ret.add(fplayer);
            }
        }

        return ret;
    }

    public FPlayer getFPlayerLeader() {
        // if ( ! this.isNormal()) return null;

        for (FPlayer fplayer : fplayers) {
            if (fplayer.getRole() == Rel.LEADER) {
                return fplayer;
            }
        }
        return null;
    }

    public ArrayList<FPlayer> getFPlayersWhereRole(Rel role) {
        ArrayList<FPlayer> ret = new ArrayList<>();
        // if ( ! this.isNormal()) return ret;

        for (FPlayer fplayer : fplayers) {
            if (fplayer.getRole() == role) {
                ret.add(fplayer);
            }
        }

        return ret;
    }

    public ArrayList<Player> getOnlinePlayers() {
        ArrayList<Player> ret = new ArrayList<>();
        // if (this.isPlayerFreeType()) return ret;

        for (Player player : P.p.getServer().getOnlinePlayers()) {
            FPlayer fplayer = FPlayers.i.get(player);
            if (fplayer.getFaction() == this) {
                ret.add(player);
            }
        }

        return ret;
    }

    // used when current leader is about to be removed from the faction;
    // promotes new leader, or disbands faction if no other members left
    public void promoteNewLeader() {
        if (!isNormal()) {
            return;
        }
        if (getFlag(FFlag.PERMANENT) && Conf.permanentFactionsDisableLeaderPromotion) {
            return;
        }

        FPlayer oldLeader = getFPlayerLeader();

        // get list of officers, or list of normal members if there are no
        // officers
        ArrayList<FPlayer> replacements = getFPlayersWhereRole(Rel.OFFICER);
        if (replacements == null || replacements.isEmpty()) {
            replacements = getFPlayersWhereRole(Rel.MEMBER);
        }

        if (replacements == null || replacements.isEmpty()) { // faction leader
            // is the only
            // member;
            // one-man
            // faction
            if (getFlag(FFlag.PERMANENT)) {
                if (oldLeader != null) {
                    oldLeader.setRole(Rel.MEMBER);
                }
                return;
            }

            // no members left and faction isn't permanent, so disband it
            if (Conf.logFactionDisband) {
                P.p.log("The faction " + this.getTag() + " (" + getId() + ") has been disbanded since it has no members left.");
            }

            for (FPlayer fplayer : FPlayers.i.getOnline()) {
                fplayer.msg("The faction %s<i> was disbanded.", this.getTag(fplayer));
            }

            detach();
        } else { // promote new faction leader
            if (oldLeader != null) {
                oldLeader.setRole(Rel.MEMBER);
            }
            replacements.get(0).setRole(Rel.LEADER);
            msg("<i>Faction leader <h>%s<i> has been removed. %s<i> has been promoted as the new faction leader.", oldLeader == null ? "" : oldLeader.getName(), replacements.get(0).getName());
            P.p.log("Faction " + this.getTag() + " (" + getId() + ") leader was removed. Replacement leader: " + replacements.get(0).getName());
        }
    }

    // ----------------------------------------------//
    // Land
    // ----------------------------------------------//
    public Set<FLocation> getClaims() {
        return Board.getFactionClaims(this);
    }

    public void unclaimAll() {
        Board.unclaimAll(id);
    }

    // ----------------------------------------------//
    // Messages
    // ----------------------------------------------//
    @Override
    public void msg(String message, Object... args) {
        message = P.p.txt.parse(message, args);

        for (FPlayer fplayer : getFPlayersWhereOnline(true)) {
            fplayer.sendMessage(message);
        }
    }

    public void sendMessage(String message) {
        for (FPlayer fplayer : getFPlayersWhereOnline(true)) {
            fplayer.sendMessage(message);
        }
    }

    public void sendMessage(List<String> messages) {
        for (FPlayer fplayer : getFPlayersWhereOnline(true)) {
            fplayer.sendMessage(messages);
        }
    }

    // ----------------------------------------------//
    // Offline Faction Protection
    // ----------------------------------------------//
    public void updateLastOnlineTime() {
        // We have either gained or a lost a player.
        if (isNone()) {
            return;
        }

        lastOnlineTime = System.currentTimeMillis();
    }

    public boolean hasOfflineExplosionProtection() {
        if (!Conf.protectOfflineFactionsFromExplosions || isNone()) {
            return false;
        }

        long timeUntilNoboom = getLastOnlineTime() + (long) (Conf.offlineExplosionProtectionDelay * 60 * 1000);

        // No protection if players are online.
        if (getOnlinePlayers().size() > 0) {
            return false;
        }

        // No Protection while timeUntilNoboom is greater than current system
        // time.
        if (timeUntilNoboom > System.currentTimeMillis()) {
            return false;
        }

        return true;
    }

    public long getLastOnlineTime() {
        return lastOnlineTime;
    }

    // ----------------------------------------------//
    // Deprecated
    // ----------------------------------------------//

    /**
     * @deprecated As of release 1.7, replaced by {@link #getFPlayerLeader()}
     */
    @Deprecated
    public FPlayer getFPlayerAdmin() {
        return getFPlayerLeader();
    }

    /**
     * @deprecated As of release 1.7, replaced by {@link #getFlag()}
     */
    @Deprecated
    public boolean isPeaceful() {
        return getFlag(FFlag.PEACEFUL);
    }

    /**
     * @deprecated As of release 1.7, replaced by {@link #getFlag()}
     */
    @Deprecated
    public boolean getPeacefulExplosionsEnabled() {
        return getFlag(FFlag.EXPLOSIONS);
    }

    /**
     * @deprecated As of release 1.7, replaced by {@link #getFlag()}
     */
    @Deprecated
    public boolean noExplosionsInTerritory() {
        return !getFlag(FFlag.EXPLOSIONS);
    }

    /**
     * @deprecated As of release 1.7, replaced by {@link #getFlag()}
     */
    @Deprecated
    public boolean isSafeZone() {
        return !getFlag(FFlag.EXPLOSIONS);
    }

    // ----------------------------------------------//
    // Persistance and entity management
    // ----------------------------------------------//
    @Override
    public void postDetach() {
        if (Econ.shouldBeUsed()) {
            Econ.setBalance(getAccountId(), 0);
        }

        // Clean the board
        Board.clean();

        // Clean the fplayers
        FPlayers.i.clean();
    }

}
