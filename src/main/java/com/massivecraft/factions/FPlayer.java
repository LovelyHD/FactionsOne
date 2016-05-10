package com.massivecraft.factions;

import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.LandClaimEvent;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Worldguard;
import com.massivecraft.factions.struct.FFlag;
import com.massivecraft.factions.struct.FPerm;
import com.massivecraft.factions.struct.Rel;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.zcore.persist.PlayerEntity;
import io.github.dre2n.factionsone.config.FMessages;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Logged in players always have exactly one FPlayer instance. Logged out players may or may not
 * have an FPlayer instance. They will always have one if they are part of a faction. This is
 * because only players with a faction are saved to disk (in order to not waste disk space). The
 * FPlayer is linked to a minecraft player using the player name. The same instance is always
 * returned for the same player. This means you can use the == operator. No .equals method
 * necessary.
 */
// TODO: The players are saved in non order.
public class FPlayer extends PlayerEntity implements EconomyParticipator {
    // FIELD: lastStoodAt

    private transient FLocation lastStoodAt = new FLocation(); // Where did this player stand the last time we checked?

    public FLocation getLastStoodAt() {
        return lastStoodAt;
    }

    public void setLastStoodAt(FLocation flocation) {
        lastStoodAt = flocation;
    }

    // FIELD: factionId
    private String factionId;

    public Faction getFaction() {
        if (factionId == null) {
            return null;
        }
        return Factions.i.get(factionId);
    }

    public String getFactionId() {
        return factionId;
    }

    public boolean hasFaction() {
        return !factionId.equals("0");
    }

    public void setFaction(Faction faction) {
        Faction oldFaction = getFaction();
        if (oldFaction != null) {
            oldFaction.removeFPlayer(this);
        }
        faction.addFPlayer(this);
        factionId = faction.getId();
    }

    // FIELD: role
    private Rel role;

    public Rel getRole() {
        return role;
    }

    public void setRole(Rel role) {
        this.role = role;
    }

    // FIELD: title
    private String title;

    public String getTitle() {
        return hasFaction() ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // FIELD: power
    private double power;

    // FIELD: powerBoost
    // special increase/decrease to min and max power for this player
    private double powerBoost;

    public double getPowerBoost() {
        return powerBoost;
    }

    public void setPowerBoost(double powerBoost) {
        this.powerBoost = powerBoost;
    }

    // FIELD: lastPowerUpdateTime
    private long lastPowerUpdateTime;

    // FIELD: lastLoginTime
    private long lastLoginTime;

    // FIELD: mapAutoUpdating
    private transient boolean mapAutoUpdating;

    public void setMapAutoUpdating(boolean mapAutoUpdating) {
        this.mapAutoUpdating = mapAutoUpdating;
    }

    public boolean isMapAutoUpdating() {
        return mapAutoUpdating;
    }

    // FIELD: autoClaimEnabled
    private transient Faction autoClaimFor;

    public Faction getAutoClaimFor() {
        return autoClaimFor;
    }

    public void setAutoClaimFor(Faction faction) {
        autoClaimFor = faction;
    }

    private transient boolean hasAdminMode = false;

    public boolean hasAdminMode() {
        return hasAdminMode;
    }

    public void setHasAdminMode(boolean val) {
        hasAdminMode = val;
    }

    // FIELD: loginPvpDisabled
    private transient boolean loginPvpDisabled;

    // FIELD: account
    @Override
    public String getAccountId() {
        return getId();
    }

    // -------------------------------------------- //
    // Construct
    // -------------------------------------------- //
    // GSON need this noarg constructor.
    public FPlayer() {
        this.resetFactionData(false);
        power = Conf.powerPlayerStarting;
        lastPowerUpdateTime = System.currentTimeMillis();
        lastLoginTime = System.currentTimeMillis();
        mapAutoUpdating = false;
        autoClaimFor = null;
        loginPvpDisabled = Conf.noPVPDamageToOthersForXSecondsAfterLogin > 0;
        powerBoost = 0.0;

        if (!Conf.newPlayerStartingFactionID.equals("0") && Factions.i.exists(Conf.newPlayerStartingFactionID)) {
            factionId = Conf.newPlayerStartingFactionID;
        }
    }

    public final void resetFactionData(boolean doSpoutUpdate) {
        if (factionId != null && Factions.i.exists(factionId)) // Avoid
        // infinite
        // loop!
        // TODO:
        // I
        // think
        // that
        // this
        // is
        // needed
        // is
        // a
        // sign
        // we
        // need
        // to
        // refactor.
        {
            Faction currentFaction = getFaction();
            if (currentFaction != null) {
                currentFaction.removeFPlayer(this);
            }
        }

        factionId = "0"; // The default neutral faction

        role = Rel.MEMBER;
        title = "";
        autoClaimFor = null;
    }

    public void resetFactionData() {
        this.resetFactionData(true);
    }

    // -------------------------------------------- //
    // Getters And Setters
    // -------------------------------------------- //
    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        losePowerFromBeingOffline();
        this.lastLoginTime = lastLoginTime;
        lastPowerUpdateTime = lastLoginTime;
        if (Conf.noPVPDamageToOthersForXSecondsAfterLogin > 0) {
            loginPvpDisabled = true;
        }
    }

    public boolean hasLoginPvpDisabled() {
        if (!loginPvpDisabled) {
            return false;
        }
        if (lastLoginTime + Conf.noPVPDamageToOthersForXSecondsAfterLogin * 1000 < System.currentTimeMillis()) {
            loginPvpDisabled = false;
            return false;
        }
        return true;
    }

    // ----------------------------------------------//
    // Title, Name, Faction Tag and Chat
    // ----------------------------------------------//
    public String getName() {
        if (isOnline()) {
            return getPlayer().getName();
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(getId()));
        return player.getName() != null ? player.getName() : getId();
    }

    public String getTag() {
        return hasFaction() ? getFaction().getTag() : "";
    }

    // Base concatenations:
    public String getNameAndSomething(String something) {
        String ret = role.getPrefix();
        if (something.length() > 0) {
            ret += something + " ";
        }
        ret += getName();
        return ret;
    }

    public String getNameAndTitle() {
        return getNameAndSomething(getTitle());
    }

    public String getNameAndTag() {
        return getNameAndSomething(getTag());
    }

    // Colored concatenations:
    // These are used in information messages
    public String getNameAndTitle(Faction faction) {
        return getColorTo(faction) + this.getNameAndTitle();
    }

    public String getNameAndTitle(FPlayer fplayer) {
        return getColorTo(fplayer) + this.getNameAndTitle();
    }

    // Chat Tag:
    // These are injected into the format of global chat messages.
    public String getChatTag() {
        return hasFaction() ? String.format(Conf.chatTagFormat, role.getPrefix() + getTag()) : "";
    }

    // Colored Chat Tag
    public String getChatTag(Faction faction) {
        return hasFaction() ? this.getRelationTo(faction).getColor() + getChatTag() : "";
    }

    public String getChatTag(FPlayer fplayer) {
        return hasFaction() ? getColorTo(fplayer) + getChatTag() : "";
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

    public Rel getRelationToLocation() {
        return Board.getFactionAt(new FLocation(this)).getRelationTo(this);
    }

    @Override
    public ChatColor getColorTo(RelationParticipator observer) {
        return RelationUtil.getColorOfThatToMe(this, observer);
    }

    // ----------------------------------------------//
    // Health
    // ----------------------------------------------//
    public void heal(int amnt) {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        player.setHealth(player.getHealth() + amnt);
    }

    // ----------------------------------------------//
    // Power
    // ----------------------------------------------//
    public double getPower() {
        updatePower();
        return power;
    }

    protected void alterPower(double delta) {
        power += delta;
        if (power > getPowerMax()) {
            power = getPowerMax();
        } else if (power < getPowerMin()) {
            power = getPowerMin();
        }
    }

    public double getPowerMax() {
        return Conf.powerPlayerMax + powerBoost;
    }

    public double getPowerMin() {
        return Conf.powerPlayerMin + powerBoost;
    }

    public int getPowerRounded() {
        return (int) Math.round(getPower());
    }

    public int getPowerMaxRounded() {
        return (int) Math.round(getPowerMax());
    }

    public int getPowerMinRounded() {
        return (int) Math.round(getPowerMin());
    }

    protected void updatePower() {
        if (isOffline()) {
            losePowerFromBeingOffline();
            if (!Conf.powerRegenOffline) {
                return;
            }
        }
        long now = System.currentTimeMillis();
        long millisPassed = now - lastPowerUpdateTime;
        lastPowerUpdateTime = now;

        Player thisPlayer = getPlayer();
        if (thisPlayer != null && thisPlayer.isDead()) {
            return; // don't let dead players regain power until they respawn
        }

        int millisPerMinute = 60 * 1000;
        double powerPerMinute = Conf.powerPerMinute;
        if (Conf.scaleNegativePower && power < 0) {
            powerPerMinute += Math.sqrt(Math.abs(power)) * Math.abs(power) / Conf.scaleNegativeDivisor;
        }
        alterPower(millisPassed * powerPerMinute / millisPerMinute);

    }

    protected void losePowerFromBeingOffline() {
        if (Conf.powerOfflineLossPerDay > 0.0 && power > Conf.powerOfflineLossLimit) {
            long now = System.currentTimeMillis();
            long millisPassed = now - lastPowerUpdateTime;
            lastPowerUpdateTime = now;

            double loss = millisPassed * Conf.powerOfflineLossPerDay / (24 * 60 * 60 * 1000);
            if (power - loss < Conf.powerOfflineLossLimit) {
                loss = power;
            }
            alterPower(-loss);
        }
    }

    public void onDeath() {
        updatePower();
        alterPower(-Conf.powerPerDeath);
    }

    // ----------------------------------------------//
    // Territory
    // ----------------------------------------------//
    public boolean isInOwnTerritory() {
        return Board.getFactionAt(new FLocation(this)) == getFaction();
    }

    /* public boolean isInOthersTerritory() { Faction factionHere = Board.getFactionAt(new
	 * FLocation(this)); return factionHere != null && factionHere.isNormal() && factionHere !=
	 * this.getFaction(); } */
 /* public boolean isInAllyTerritory() { return Board.getFactionAt(new
	 * FLocation(this)).getRelationTo(this) == Rel.ALLY; } */
 /* public boolean isInNeutralTerritory() { return Board.getFactionAt(new
	 * FLocation(this)).getRelationTo(this) == Rel.NEUTRAL; } */
    public boolean isInOthersTerritory() {
        Faction factionHere = Board.getFactionAt(new FLocation(this));
        return factionHere != null && factionHere.isNormal() && factionHere != getFaction();
    }

    public boolean isInAllyTerritory() {
        return Board.getFactionAt(new FLocation(this)).getRelationTo(this) == Rel.ALLY;
    }

    public boolean isInNeutralTerritory() {
        return Board.getFactionAt(new FLocation(this)).getRelationTo(this) == Rel.NEUTRAL;
    }

    public boolean isInEnemyTerritory() {
        return Board.getFactionAt(new FLocation(this)).getRelationTo(this) == Rel.ENEMY;
    }

    public void sendFactionHereMessage() {
        Faction factionHere = Board.getFactionAt(getLastStoodAt());
        String msg = P.p.txt.parse("<i>") + " ~ " + factionHere.getTag(this);
        if (factionHere.getDescription().length() > 0) {
            msg += " - " + factionHere.getDescription();
        }
        this.sendMessage(msg);
    }

    // -------------------------------
    // Actions
    // -------------------------------
    public void leave(boolean makePay) {
        Faction myFaction = getFaction();
        makePay = makePay && Econ.shouldBeUsed() && !hasAdminMode();

        if (myFaction == null) {
            resetFactionData();
            return;
        }

        boolean perm = myFaction.getFlag(FFlag.PERMANENT);

        if (!perm && getRole() == Rel.LEADER && myFaction.getFPlayers().size() > 1) {
            msg(FMessages.ERROR_MUST_PASS_LEADERSHIP.getMessage());
            return;
        }

        if (!Conf.canLeaveWithNegativePower && getPower() < 0) {
            msg(FMessages.ERROR_POWER_NOT_POSITIVE.getMessage());
            return;
        }

        // if economy is enabled and they're not on the bypass list, make sure they can pay
        if (makePay && !Econ.hasAtLeast(this, Conf.econCostLeave, FMessages.ERROR_NOT_ENOUGH_MONEY_TO_LEAVE.getMessage())) {
            return;
        }

        FPlayerLeaveEvent leaveEvent = new FPlayerLeaveEvent(this, myFaction, FPlayerLeaveEvent.PlayerLeaveReason.LEAVE);
        Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
        if (leaveEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (makePay && !Econ.modifyMoney(this, -Conf.econCostLeave, FMessages.ERROR_NOT_ENOUGH_MONEY_TO_LEAVE.getMessage(), FMessages.ERROR_NOT_ENOUGH_MONEY_FOR_LEAVING.getMessage())) {
            return;
        }

        // Am I the last one in the faction?
        if (myFaction.getFPlayers().size() == 1) {
            // Transfer all money
            if (Econ.shouldBeUsed()) {
                Econ.transferMoney(this, myFaction, this, Econ.getBalance(myFaction.getAccountId()));
            }
        }

        if (myFaction.isNormal()) {
            for (FPlayer fplayer : myFaction.getFPlayersWhereOnline(true)) {
                fplayer.msg(FMessages.PLAYER_LEFT.getMessage(), this.describeTo(fplayer, true), myFaction.describeTo(fplayer));
            }

            if (Conf.logFactionLeave) {
                P.p.log(getName() + " left the faction: " + myFaction.getTag());
            }
        }

        this.resetFactionData();
        myFaction.updateLastOnlineTime();

        if (myFaction.isNormal() && !perm && myFaction.getFPlayers().isEmpty()) {
            // Remove this faction
            for (FPlayer fplayer : FPlayers.i.getOnline()) {
                fplayer.msg(FMessages.FACTION_DISBANDED.getMessage(), myFaction.describeTo(fplayer, true));
            }

            myFaction.detach();
            if (Conf.logFactionDisband) {
                P.p.log("The faction " + myFaction.getTag() + " (" + myFaction.getId() + ") was disbanded due to the last player (" + getName() + ") leaving.");
            }
        }
    }

    public boolean canClaimForFactionAtLocation(Faction forFaction, Location location, boolean notifyFailure) {
        String error = null;
        FLocation flocation = new FLocation(location);
        Faction myFaction = getFaction();
        Faction currentFaction = Board.getFactionAt(flocation);
        int ownedLand = forFaction.getLandRounded();

        if (Conf.worldGuardChecking && Worldguard.checkForRegionsInChunk(location)) {
            // Checks for WorldGuard regions in the chunk attempting to be claimed
            error = P.p.txt.parse(FMessages.ERROR_LAND_PROTECTED.getMessage());
        } else if (Conf.worldsNoClaiming.contains(flocation.getWorldName())) {
            error = P.p.txt.parse(FMessages.ERROR_WORLD_NO_CLAIMING.getMessage());
        } else if (hasAdminMode()) {
            return true;
        } else if (forFaction == currentFaction) {
            error = P.p.txt.parse(FMessages.ERROR_LAND_OWNED.getMessage(), forFaction.describeTo(this, true));
        } else if (!FPerm.TERRITORY.has(this, forFaction, true)) {
            return false;
        } else if (forFaction.getFPlayers().size() < Conf.claimsRequireMinFactionMembers) {
            error = P.p.txt.parse(FMessages.ERROR_NOT_ENOUGH_MEMBERS_TO_CLAIM.getMessage(), Conf.claimsRequireMinFactionMembers);
        } else if (ownedLand >= forFaction.getPowerRounded()) {
            error = P.p.txt.parse(FMessages.ERROR_NOT_ENOUGH_POWER_TO_CLAIM.getMessage());
        } else if (Conf.claimedLandsMax != 0 && ownedLand >= Conf.claimedLandsMax && !forFaction.getFlag(FFlag.INFPOWER)) {
            error = P.p.txt.parse(FMessages.ERROR_LAND_LIMIT_REACHED.getMessage());
        } else if (!Conf.claimingFromOthersAllowed && currentFaction.isNormal()) {
            error = P.p.txt.parse(FMessages.ERROR_CLAIMING_FROM_OTHERS.getMessage());
        } else if (currentFaction.getRelationTo(forFaction).isAtLeast(Rel.TRUCE) && !currentFaction.isNone()) {
            error = P.p.txt.parse(FMessages.ERROR_CLAIM_RELATION.getMessage());
        } else if (Conf.claimsMustBeConnected && !hasAdminMode() && myFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0 && !Board.isConnectedLocation(flocation, myFaction)
                && (!Conf.claimsCanBeUnconnectedIfOwnedByOtherFaction || !currentFaction.isNormal())) {
            if (Conf.claimsCanBeUnconnectedIfOwnedByOtherFaction) {
                error = P.p.txt.parse(FMessages.ERROR_LAND_NOT_CONNECTED_OR_OWNED.getMessage());
            } else {
                error = P.p.txt.parse(FMessages.ERROR_LAND_NOT_CONNECTED.getMessage());
            }
        } else if (currentFaction.isNormal()) {
            if (!currentFaction.hasLandInflation()) {
                // TODO more messages WARN current faction most importantly
                error = P.p.txt.parse(FMessages.ERROR_LAND_OWNED_CAN_KEEP.getMessage(), currentFaction.getTag(this));
            } else if (!Board.isBorderLocation(flocation)) {
                error = P.p.txt.parse(FMessages.ERROR_MUST_CLAIM_BORDER.getMessage());
            }
        }

        if (notifyFailure && error != null) {
            msg(error);
        }
        return error == null;
    }

    public boolean attemptClaim(Faction forFaction, Location location, boolean notifyFailure) {
        // notifyFailure is false if called by auto-claim; no need to notify on
        // every failure for it
        // return value is false on failure, true on success

        FLocation flocation = new FLocation(location);
        Faction currentFaction = Board.getFactionAt(flocation);

        int ownedLand = forFaction.getLandRounded();

        if (!canClaimForFactionAtLocation(forFaction, location, notifyFailure)) {
            return false;
        }

        // TODO: Add flag no costs??
        // if economy is enabled and they're not on the bypass list, make sure
        // they can pay
        boolean mustPay = Econ.shouldBeUsed() && !hasAdminMode();
        double cost = 0.0;
        EconomyParticipator payee = null;
        if (mustPay) {
            cost = Econ.calculateClaimCost(ownedLand, currentFaction.isNormal());

            if (Conf.econClaimUnconnectedFee != 0.0 && forFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0 && !Board.isConnectedLocation(flocation, forFaction)) {
                cost += Conf.econClaimUnconnectedFee;
            }

            if (Conf.bankEnabled && Conf.bankFactionPaysLandCosts && hasFaction()) {
                payee = getFaction();
            } else {
                payee = this;
            }

            if (!Econ.hasAtLeast(payee, cost, FMessages.ERROR_NOT_ENOUGH_MONEY_TO_CLAIM.getMessage())) {
                return false;
            }
        }

        LandClaimEvent claimEvent = new LandClaimEvent(flocation, forFaction, this);
        Bukkit.getServer().getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) {
            return false;
        }

        // then make 'em pay (if applicable)
        if (mustPay && !Econ.modifyMoney(payee, -cost, FMessages.ERROR_NOT_ENOUGH_MONEY_TO_CLAIM.getMessage(), FMessages.ERROR_NOT_ENOUGH_MONEY_FOR_CLAIMING.getMessage())) {
            return false;
        }

        // announce success
        Set<FPlayer> informTheseFPlayers = new HashSet<>();
        informTheseFPlayers.add(this);
        informTheseFPlayers.addAll(forFaction.getFPlayersWhereOnline(true));
        for (FPlayer fp : informTheseFPlayers) {
            fp.msg(FMessages.PLAYER_LAND_CLAIMED.getMessage(), this.describeTo(fp, true), forFaction.describeTo(fp), currentFaction.describeTo(fp));
        }

        Board.setFactionAt(forFaction, flocation);

        if (Conf.logLandClaims) {
            P.p.log(getName() + " claimed land at (" + flocation.getCoordString() + ") for the faction: " + forFaction.getTag());
        }

        return true;
    }

    // -------------------------------------------- //
    // Persistance
    // -------------------------------------------- //
    @Override
    public boolean shouldBeSaved() {
        if (hasFaction()) {
            return true;
        }
        if (getPowerRounded() != getPowerMaxRounded() && getPowerRounded() != (int) Math.round(Conf.powerPlayerStarting)) {
            return true;
        }
        return false;
    }

    @Override
    public void msg(String str, Object... args) {
        this.sendMessage(P.p.txt.parse(str, args));
    }

}
