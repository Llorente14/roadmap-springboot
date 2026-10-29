package com.alterego.warmup;

import java.util.List;

/**
 * Quest Q06 — Membaca annotation lewat reflection.
 *
 * Kembalikan nama field ber-@Required yang nilainya null, atau String kosong/spasi saja.
 * Urutan mengikuti urutan deklarasi field. Ini versi mini dari cara kerja Bean Validation (@NotBlank).
 */
public final class RequiredFieldChecker {

    private RequiredFieldChecker() {
    }

    public static List<String> missing(Object target) {
        throw new UnsupportedOperationException("TODO Q06");
    }
}
