package com.massivecraft.factions;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.zcore.persist.PlayerEntityCollection;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class FPlayers extends PlayerEntityCollection<FPlayer> {
    public static FPlayers i = new FPlayers();
    private final P p = P.p;

    private FPlayers() {
        super(FPlayer.class, new CopyOnWriteArrayList<>(), new ConcurrentSkipListMap<>(CASE_INSENSITIVE_ORDER), new File(P.p.getDataFolder(), "players.json"), P.p.gson);
        setCreative(true);
    }

    @Override
    public Type getMapType() {
        return new TypeToken<Map<String, FPlayer>>() {
        }.getType();
    }

    public void clean() {
        get().forEach(fplayer -> {
            if (!Factions.i.exists(fplayer.getFactionId())) {
                p.log("Reset faction data (invalid faction) for player " + fplayer.getName());
                fplayer.resetFactionData(false);
            }
        });
    }
}
