package com.alterego.warmup;

import java.util.function.Supplier;

/**
 * Quest Q07 — Dependency Injection manual (Spring versi mini).
 *
 *  - register(type, factory): daftarkan cara membuat objek. Factory BELUM dipanggil di sini.
 *  - get(type): buat objek saat pertama diminta, lalu kembalikan instance yang SAMA setiap kali (singleton).
 *  - type yang belum terdaftar: IllegalStateException yang menyebut nama class-nya.
 */
public class MiniContainer {

    public <T> void register(Class<T> type, Supplier<? extends T> factory) {
        throw new UnsupportedOperationException("TODO Q07");
    }

    public <T> T get(Class<T> type) {
        throw new UnsupportedOperationException("TODO Q07");
    }
}
