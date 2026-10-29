package com.alterego.warmup;

import java.util.List;

/**
 * Kontrak validasi (Q04). Sudah jadi.
 * List kosong = valid. Tiap pesan error diawali nama field + ":", mis. "nickname: tidak boleh kosong".
 */
@FunctionalInterface
public interface Validator<T> {

    List<String> validate(T value);
}
