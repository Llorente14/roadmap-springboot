package com.alterego.fondasi;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * F08 — null dan Optional.
 *
 * Aturan: method yang mengembalikan Optional TIDAK PERNAH mengembalikan null.
 */
public final class Finder {

    private Finder() {
    }

    /** Nama pertama yang diawali prefix. Tidak ada -> Optional kosong. */
    public static Optional<String> firstWithPrefix(List<String> names, String prefix) {

        for (String nama : names) {
            if (nama.startsWith(prefix)) {
                // Ini saya bisa kasih nullAble, karena saya ga yakkin itu pasti null karean
                // diterima dari luar
                return Optional.ofNullable(nama);
            }
        }
        return Optional.empty();

    }

    /**
     * Tim seorang pemain dari map pemain -> tim. Pemain tidak ada -> Optional
     * kosong.
     */
    public static Optional<String> teamOf(Map<String, String> playerTeams, String player) {
        // Cara Manual
        // for (Map.Entry<String, String> name : playerTeams.entrySet()) {
        // if (name.getKey().equals(player)) {

        // String pickName = name.getValue();
        // return Optional.of(pickName);
        // }
        // }
        // return Optional.empty();

        return Optional.ofNullable(playerTeams.get(player));
    }

    /** Isi Optional, atau "UNKNOWN" kalau kosong. */
    public static String orUnknown(Optional<String> nickname) {
        return nickname.orElse("UNKNOWN");
    }

    /** Panjang nickname di dalam Optional; kosong -> 0. Coba tanpa if. */
    public static int lengthOf(Optional<String> nickname) {
        return nickname.map(String::length).orElse(0);
    }
}
