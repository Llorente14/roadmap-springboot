package com.alterego.fondasi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * F07 — Map sebagai tempat simpan di dalam object.
 *
 * Mencatat role setiap hero. Nama hero unik.
 */
public class HeroPool {
    private final Map<String, String> heroes = new HashMap<>();

    /** Daftarkan hero. Nama sudah terdaftar -> tidak diubah, kembalikan false. */
    public boolean register(String hero, String role) {
        // if (heroes.containsKey(hero)) {
        // return false;
        // }
        // heroes.putIfAbsent(hero, role);
        // return true;
        // Better
        return heroes.putIfAbsent(hero, role) == null;
    }

    /** Role hero; belum terdaftar -> "unknown". */
    public String roleOf(String hero) {
        String role = heroes.getOrDefault(hero, "unknown");
        return role;
    }

    /** Semua hero dengan role tertentu (urutan bebas). */
    public List<String> heroesWithRole(String role) {
        List<String> listHero = new ArrayList<>();
        for (Map.Entry<String, String> hero : heroes.entrySet()) {
            if (hero.getValue().equals(role)) {
                listHero.add(hero.getKey());
            }
        }
        return listHero;
    }

    public int size() {
        return heroes.size();
    }
}
