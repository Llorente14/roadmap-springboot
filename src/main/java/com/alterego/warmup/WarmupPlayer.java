package com.alterego.warmup;

/**
 * Data pemain versi latihan (Q02, Q04). Sudah jadi, tidak perlu diubah.
 * Perhatikan: ini sebuah record. Baca dulu apa yang otomatis dibuatkan Java untuk record.
 */
public record WarmupPlayer(String id, String game, String nickname, String role, String status, int displayOrder) {
}
