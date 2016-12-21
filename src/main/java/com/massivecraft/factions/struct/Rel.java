package com.massivecraft.factions.struct;

import com.massivecraft.factions.Conf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@Getter
@RequiredArgsConstructor
public enum Rel {
    LEADER(70, "your faction leader", "your faction leader", "", ""),
    COLEADER(70, "your faction coleader", "your faction coleader", "", ""),
    OFFICER(60, "an officer in your faction", "officers in your faction", "", ""),
    MEMBER(50, "a member in your faction", "members in your faction", "your faction", "your factions"),
    RECRUIT(45, "a recruit in your faction", "recruits in your faction", "", ""),
    ALLY(40, "an ally", "allies", "an allied faction", "allied factions"),
    TRUCE(30, "someone in truce with you", "those in truce with you", "a faction in truce", "factions in truce"),
    NEUTRAL(20, "someone neutral to you", "those neutral to you", "a neutral faction", "neutral factions"),
    ENEMY(10, "an enemy", "enemies", "an enemy faction", "enemy factions"),;

    private final int value;
    private final String descPlayerOne;
    private final String descPlayerMany;
    private final String descFactionOne;
    private final String descFactionMany;

    public static Rel parse(String input) {
        if (input == null || input.length() < 1) {
            return null;
        }

        input = input.toLowerCase();

        // These are to allow conversion from the old system.
        switch (input) {
            case "admin":
                return LEADER;
            case "moderator":
                return OFFICER;
            case "normal":
                return MEMBER;
        }

        // This is how we check: Based on first char.
        switch (input.charAt(0)) {
            case 'l':
                return LEADER;
            case 'c':
                return COLEADER;
            case 'o':
                return OFFICER;
            case 'm':
                return MEMBER;
            case 'r':
                return RECRUIT;
            case 'a':
                return ALLY;
            case 't':
                return TRUCE;
            case 'n':
                return NEUTRAL;
            case 'e':
                return ENEMY;
            default:
                return null;
        }
    }

    public boolean isAtLeast(Rel rel) {
        return value >= rel.value;
    }

    public boolean isAtMost(Rel rel) {
        return value <= rel.value;
    }

    public boolean isLessThan(Rel rel) {
        return value < rel.value;
    }

    public boolean isMoreThan(Rel rel) {
        return value > rel.value;
    }

    public ChatColor getColor() {
        if (isAtLeast(RECRUIT)) {
            return Conf.colorMember;
        }

        switch (this) {
            case ALLY:
                return Conf.colorAlly;
            case NEUTRAL:
                return Conf.colorNeutral;
            case TRUCE:
                return Conf.colorTruce;
            default:
                return Conf.colorEnemy;
        }
    }

    public String getPrefix() {
        switch (this) {
            case LEADER:
                return Conf.prefixLeader;
            case COLEADER:
                return Conf.prefixColeader;
            case OFFICER:
                return Conf.prefixOfficer;
            case MEMBER:
                return Conf.prefixMember;
            case RECRUIT:
                return Conf.prefixRecruit;
            default:
                return "";
        }
    }

    public double getRelationCost() {
        switch (this) {
            case ENEMY:
                return Conf.econCostEnemy;
            case ALLY:
                return Conf.econCostAlly;
            case TRUCE:
                return Conf.econCostTruce;
            default:
                return Conf.econCostNeutral;
        }
    }
}
