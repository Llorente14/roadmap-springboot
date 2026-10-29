package com.alterego.fondasi;

import java.util.Optional;

/**
 * F13 — Class&lt;T&gt; sebagai kunci.
 *
 * Tas yang menyimpan SATU nilai per tipe. Ambil kembali dengan tipe yang benar, tanpa cast di pemanggil.
 */
public class TypedBag {

    /** Simpan nilai untuk tipe itu. Nilai lama dengan tipe sama diganti. */
    public <T> void put(Class<T> type, T value) {
        throw new UnsupportedOperationException("TODO F13");
    }

    /** Ambil nilai untuk tipe itu; belum ada -> Optional kosong. */
    public <T> Optional<T> get(Class<T> type) {
        throw new UnsupportedOperationException("TODO F13");
    }
}
