package com.alterego.warmup;

import java.util.List;

/**
 * Quest Q04 — Generics.
 *
 * Menggabungkan beberapa Validator&lt;T&gt; menjadi satu: semua dijalankan, semua error dikumpulkan berurutan.
 */
public class CompositeValidator<T> implements Validator<T> {

    @SafeVarargs
    public static <T> CompositeValidator<T> of(Validator<T>... validators) {
        throw new UnsupportedOperationException("TODO Q04");
    }

    @Override
    public List<String> validate(T value) {
        throw new UnsupportedOperationException("TODO Q04");
    }
}
