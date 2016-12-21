package com.massivecraft.factions.struct;

import com.massivecraft.factions.Conf;
import org.bukkit.ChatColor;

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

    public String getDescPlayerOne() {
        return descPlayerOne;
    }

    private final String descPlayerMany;

    public String getDescPlayerMany() {
        return descPlayerMany;
    }

    private final String descFactionOne;

    public String getDescFactionOne() {
        return descFactionOne;
    }

    private final String descFactionMany;

    public String getDescFactionMany() {
        return descFactionMany;
    }

    private Rel(final int value, final String descPlayerOne, final String descPlayerMany, final String descFactionOne, final String descFactionMany) {
        this.value = value;
        this.descPlayerOne = descPlayerOne;
        this.descPlayerMany = descPlayerMany;
        this.descFactionOne = descFactionOne;
        this.descFactionMany = descFactionMany;
    }

    public static Rel parse(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }

        str = str.toLowerCase();

        // These are to allow conversion from the old system.
        if (str.equals("admin")) {
            return LEADER;
        }

        if (str.equals("moderator")) {
            return OFFICER;
        }

        if (str.equals("normal")) {
            return MEMBER;
        }

        // This is how we check: Based on first char.
        char c = str.charAt(0);

        if (c == 'l') {
            return LEADER;
        }
        if(c == 'c') {
            return COLEADER;
        }
        if (c == 'o') {
            return OFFICER;
        }
        if (c == 'm') {
            return MEMBER;
        }
        if (c == 'r') {
            return RECRUIT;
        }
        if (c == 'a') {
            return ALLY;
        }
        if (c == 't') {
            return TRUCE;
        }
        if (c == 'n') {
            return NEUTRAL;
        }
        if (c == 'e') {
            return ENEMY;
        }
        return null;
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
        } else if (this == ALLY) {
            return Conf.colorAlly;
        } else if (this == NEUTRAL) {
            return Conf.colorNeutral;
        } else if (this == TRUCE) {
            return Conf.colorTruce;
        } else {
            return Conf.colorEnemy;
        }
    }

    public String getPrefix() {
        switch (this) {
            case LEADER:
            case COLEADER:
                return Conf.prefixLeader;

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
