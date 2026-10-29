package com.alterego.warmup;

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
        throw new UnsupportedOperationException("TODO Q02");
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
