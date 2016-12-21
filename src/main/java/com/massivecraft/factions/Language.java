package com.massivecraft.factions;

import com.massivecraft.factions.iface.EconomyParticipator;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;
import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public enum Language {
    WARP_LIMIT_REACHED("&8[&cGW&8] &7You have reached the warp limit for your faction!"),
    WARP_NAME_EXISTS("&8[&cGW&8] &7Your faction already has a warp by this name."),
    WARP_CREATED("&8[&cGW&8] &7You have created a new warp for your faction!"),
    WARP_INVALID("&8[&cGW&8] &7Your faction does not have a warp by that name."),
    WARP_TELEPORTED("&8[&cGW&8] &7You have been teleported to the faction warp %warp%"),
    WARP_REMEMBER_ME("&8[&cGW&8] &7You can now access this faction warp without a password."),
    WARP_PASSWORD_INCORRECT("&8[&cGW&8] &7Please enter the correct password for this faction warp."),
    WARP_REMOVED("&8[&cGW&8] &7You have successfully remove that faction warp."),
    WARP_LIST_PREFIX("&8[&cGW&8] &7%warps%"),
    WARP_EMPTY("&8[&cGW&8] &7Your faction does not have any warps to display."),
    WARP_ACCESS_CHANGED("&8[&cGW&8] &7%player% has been %state% from this warp."),

    CHAT_MODE_CHANGED("&8[&cGW&8] &7Your chat mode has been changed to %mode%"),
    CHAT_MODE_INVALID("&8[&cGW&8] &7That chat mode doesn't exist!"),

    NOT_IN_TERRITORY("&8[&cGW&8] &7You are not in your own territory!"),
    NO_FACTION("&8[&cGW&8] &7You don't belong to any faction!");

    private String[] messages;

    Language(String... messages) {
        this.messages = messages;
    }

    public void sendTo(EconomyParticipator participator, String... args) {
        stream(messages).forEach(message -> participator.msg(replaceArguments(message, args)));
    }

    public static void load(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "language.yml");

        if (!file.exists()) {
            save(plugin);
        }

        YamlConfiguration configuration = loadConfiguration(file);

        stream(values()).forEach(message -> {
            if (configuration.contains(message.name().toLowerCase().replace("_", "-"))) {
                List<String> configMessage = configuration.getStringList(message.name().toLowerCase().replace("_", "-"));
                String[] configMessageArray = new String[configMessage.size()];

                message.setMessages(configMessage.toArray(configMessageArray));
            }
        });
    }

    private static void save(Plugin plugin) {
        try {
            File file = new File(plugin.getDataFolder(), "language.yml");

            if (!file.exists()) {
                File parentFile = file.getParentFile();

                if (!parentFile.exists()) {
                    parentFile.mkdir();
                }

                file.createNewFile();
            }

            YamlConfiguration configuration = new YamlConfiguration();

            stream(values()).forEach(message -> {
                configuration.set(message.name().toLowerCase().replace("_", "-"), message.getMessages());
            });

            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMessages(String... messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
        }

        this.messages = messages;
    }

    private String[] getMessages(String... args) {
        String[] messages = this.messages;

        for (int i = 0; i < messages.length; i++) {
            messages[i] = replaceArguments(messages[i], args);
        }

        return messages;
    }

    private String replaceArguments(String message, String... arguments) {
        for (int i = 0; i + 2 <= arguments.length; i += 2)
            message = message.replaceAll("(?i)" + Pattern.quote(arguments[i]), arguments[i + 1].replace("$", "\\$"));

        return message;
    }
}
