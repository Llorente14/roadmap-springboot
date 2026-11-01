package com.alterego.fondasi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * F13 — Class&lt;T&gt; sebagai kunci.
 *
 * Tas yang menyimpan SATU nilai per tipe. Ambil kembali dengan tipe yang benar,
 * tanpa cast di pemanggil.
 */
public class TypedBag {
    private final Map<Class<?>, Object> list = new HashMap<>();

    /** Simpan nilai untuk tipe itu. Nilai lama dengan tipe sama diganti. */
    public <T> void put(Class<T> type, T value) {
        list.put(type, value);
    }

    /** Ambil nilai untuk tipe itu; belum ada -> Optional kosong. */
    public <T> Optional<T> get(Class<T> type) {

        return Optional.ofNullable(type.cast(list.get(type)));
    }
}
