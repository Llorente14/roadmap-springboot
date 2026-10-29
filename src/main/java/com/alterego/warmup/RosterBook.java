package com.alterego.warmup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Quest Q02 — Roster dalam memori.
 *
 * Identitas pemain = game + id (di data asli, "aces" muncul di mlbb DAN pubgm).
 */
public class RosterBook {

    /**
     * Tambah pemain. Kalau kombinasi game+id sudah ada: lempar
     * IllegalStateException.
     */
    public void add(WarmupPlayer player) {

        WarmupPlayer playerData = new WarmupPlayer(player.id(), player.game(), player.nickname(), player.role(),
                player.status(), player.displayOrder());

        Map<String, String> map = new HashMap<>();

        map.add(playerData);
    }

    /** Cari pemain. Tidak ketemu = Optional kosong, BUKAN null. */
    public Optional<WarmupPlayer> find(String game, String id) {
        throw new UnsupportedOperationException("TODO Q02");
    }

    /** Pemain berstatus "active" di game tertentu, urut displayOrder naik. */
    public List<WarmupPlayer> active(String game) {
        throw new UnsupportedOperationException("TODO Q02");
    }

    public int size() {
        throw new UnsupportedOperationException("TODO Q02");
    }
}
