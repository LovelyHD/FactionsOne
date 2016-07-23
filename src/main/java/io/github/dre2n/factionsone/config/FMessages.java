package io.github.dre2n.factionsone.config;

import com.massivecraft.factions.zcore.Lang;
import io.github.dre2n.commons.config.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Daniel Saukel
 */
public enum FMessages implements Messages {

    CMD_ACCESS_LIST1("Cmd_AccessList1", "<b>This territory isn't controlled by your faction, so you can't view the access list."),
    CMD_ACCESS_LIST2("Cmd_AccessList2", "<b>ex. /f access p SomePlayer  -or-  /f access f SomeFaction"),
    CMD_ACCESS_LIST3("Cmd_AccessList3", "<b>Alternately, you can use the command with nothing (or \"view\") specified to simply view the access list."),
    CMD_ACCESS_LIST4("Cmd_AccessList4", "<i>%s has been <lime>added to<i> the access list for this territory."),
    CMD_ACCESS_LIST5("Cmd_AccessList5", "<i>%s has been <rose>removed from<i> the access list for this territory."),
    ERROR_BANK_DISABLED("Error_BankDisabled", "<b>The faction bank system is disabled on this server."),
    ERROR_CLAIMING_FROM_OTHERS("Error_ClaimingFromOthers", "<b>You may not claim land from others."),
    ERROR_CLAIM_RELATION("Error_ClaimRelation", "<b>You can't claim this land due to your relation with the current owner."),
    ERROR_COMMAND_SENDER_MUST_BE_PLAYER("Error_SenderMustBePlayer", Lang.commandSenderMustBePlayer),
    ERROR_COMMAND_TOO_FEW_ARGUMENTS("Error_CommandTooFewArguments", Lang.commandToFewArgs),
    ERROR_COMMAND_TOO_MANY_ARGUMENTS("Error_CommandTooManyArguments", Lang.commandToManyArgs),
    ERROR_ECONOMY_DISABLED("Error_EconomyDisabled", "<b>Faction economy features are disabled on this server."),
    ERROR_LAND_LIMIT_REACHED("Error_LandLimitReached", "<b>Limit reached. You can't claim more land!"),
    ERROR_LAND_NOT_CONNECTED("Error_LandNotConnected", "<b>You can only claim additional land which is connected to your first claim!"),
    ERROR_LAND_NOT_CONNECTED_OR_OWNED("Error_LandNotConnectedOrOwned", "<b>You can only claim additional land which is connected to your first claim or controlled by another faction!"),
    ERROR_LAND_OWNED("Error_LandOwned", "%s<i> already own this land."),
    ERROR_LAND_OWNED_CAN_KEEP("Error_LandOwnedCanKeep", "%s<i> owns this land and is strong enough to keep it."),
    ERROR_LAND_PROTECTED("Error_LandProtected", "<b>This land is protected"),
    ERROR_LOCKED("Error_Locked", "<b>Factions was locked by an admin. Please try again later."),
    ERROR_MUST_CLAIM_BORDER("ErrorMustClaimBorder", "<b>You must start claiming land at the border of the territory."),
    ERROR_MUST_PASS_LEADERSHIP("ErrorMustPassLeadership", "<b>You must give the admin role to someone else first."),
    ERROR_NOT_ENOUGH_MEMBERS_TO_CLAIM("Error_NotEnoughMembersToClaim", "Factions must have at least <h>%s<b> members to claim land."),
    ERROR_NOT_ENOUGH_MONEY_TO_CLAIM("Error_NotEnoughMoneyToClaim", "to claim this land"),
    ERROR_NOT_ENOUGH_MONEY_FOR_CLAIMING("Error_NotEnoughMoneyForClaiming", "for claiming this land"),
    ERROR_NOT_ENOUGH_MONEY_FOR_LEAVING("Error_NotEnoughMoneyForLeaving", "for leaving your faction."),
    ERROR_NOT_ENOUGH_MONEY_TO_LEAVE("Error_NotEnoughMoneyToLeave", "to leave your faction."),
    ERROR_NOT_ENOUGH_POWER_TO_CLAIM("Error_NotEnoughPowerToClaim", "<b>You can't claim more land! You need more power!"),
    ERROR_PERM_DO_THAT("Error_PermDoThat", Lang.permDoThat),
    ERROR_PERM_FORBIDDEN("Error_PermForbidden", Lang.permForbidden),
    ERROR_POWER_NOT_POSITIVE("Error_PowerNotPositive", "<b>You cannot leave until your power is positive."),
    ERROR_SPECIFY_PLAYER_OR_FACTION("Error_SpecifyPlayerOrFaction", "<b>You must specify \"p\" or \"player\" to indicate a player or \"f\" or \"faction\" to indicate a faction."),
    ERROR_TAG_NOT_ALPHANUMERIC("Error_TagNotAlphanumeric", "<i>Faction tag must be alphanumeric. \"<h>%s<i>\" is not allowed."),
    ERROR_TAG_TOO_LONG("Error_TagTooLong", "<i>The faction tag can't be longer than <h>%s<i> chars."),
    ERROR_TAG_TOO_SHORT("Error_TagTooShort", "<i>The faction tag can't be shorter than <h>%s<i> chars."),
    ERROR_WORLD_NO_CLAIMING("Error_WorldNoClaiming", "<b>Sorry, this world has land claiming disabled."),
    PLAYER_LAND_CLAIMED("Player_Land_Claimed", "<h>%s<i> claimed land for <h>%s<i> from <h>%s<i>."),
    PLAYER_LEFT("Player_Left", "%s<i> left %s<i>."),
    FACTION_DISBANDED("Faction_Disbanded", "<i>%s<i> was disbanded."),
    FACTION_DISBANDED2("Faction_Disbanded2", "The faction %s<i> was disbanded."),
    FACTION_HOME_UNSET_NOT_IN_TERRITORY("FactionHomeUnsetNotInTerritory", "<b>Your faction home has been un-set since it is no longer in your territory."),
    FACTION_LEADER_REMOVED("Faction_LeaderRemoved", "<i>Faction leader <h>%s<i> has been removed. %s<i> has been promoted as the new faction leader."),;

    private String identifier;
    private String message;

    FMessages(String identifier, String message) {
        this.identifier = identifier;
        this.message = message;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(String... args) {
        String output = getMessage();

        int i = 0;
        for (String arg : args) {
            i++;

            if (arg != null) {
                output = output.replace("&v" + i, arg);

            } else {
                output = output.replace("&v" + i, "null");
            }
        }

        return output;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /* Statics */
    /**
     * @param identifer
     * the identifer to set
     */
    public static Messages getByIdentifier(String identifier) {
        for (Messages message : values()) {
            if (message.getIdentifier().equals(identifier)) {
                return message;
            }
        }

        return null;
    }

    /**
     * @return a FileConfiguration containing all messages
     */
    public static FileConfiguration toConfig() {
        FileConfiguration config = new YamlConfiguration();
        for (FMessages message : values()) {
            config.set(message.getIdentifier(), message.message);
        }

        return config;
    }

}
