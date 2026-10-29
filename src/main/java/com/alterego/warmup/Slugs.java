package com.alterego.warmup;

import java.text.Normalizer;

/**
 * Quest Q01 — Slug.
 *
 * Ubah teks bebas menjadi slug URL. Aturan lengkap ada di quest board (Q01).
 * Contoh: "M7 World Championship" -> "m7-world-championship".
 */
public final class Slugs {

    private Slugs() {
    }

    public static String toSlug(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Input tidak boleh null atau kosong.");
        }

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{M}", "");
        String lowerText = withoutAccents.toLowerCase();
        String slug = lowerText.replaceAll("[^a-z0-9]+", "-");
        slug = slug.replaceAll("^-+|-+$", "");
        if (slug.isEmpty()) {
            throw new IllegalArgumentException("Hasil teks setelah diproses tidak valid (kosong).");
        }
        return slug;

    }
}
