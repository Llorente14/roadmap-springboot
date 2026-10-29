package com.alterego.fondasi;

import java.util.List;

/**
 * F07 — Map sebagai tempat simpan di dalam object.
 *
 * Mencatat role setiap hero. Nama hero unik.
 */
public class HeroPool {

    /** Daftarkan hero. Nama sudah terdaftar -> tidak diubah, kembalikan false. */
    public boolean register(String hero, String role) {
        throw new UnsupportedOperationException("TODO F07");
    }

    /** Role hero; belum terdaftar -> "unknown". */
    public String roleOf(String hero) {
        throw new UnsupportedOperationException("TODO F07");
    }

    /** Semua hero dengan role tertentu (urutan bebas). */
    public List<String> heroesWithRole(String role) {
        throw new UnsupportedOperationException("TODO F07");
    }

    public int size() {
        throw new UnsupportedOperationException("TODO F07");
    }
}
