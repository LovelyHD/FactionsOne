package com.massivecraft.factions.zcore.persist;

import com.massivecraft.factions.zcore.MPlugin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaveTask implements Runnable {
    private static boolean running = false;
    private final MPlugin plugin;

    @Override
    public void run() {
        if (!plugin.getAutoSave() || running) {
            return;
        }

        running = true;
        plugin.preAutoSave();
        EM.saveAllToDisc();
        plugin.postAutoSave();
        running = false;
    }
}
