package com.alterego.warmup;

import java.util.List;

/**
 * Quest Q04 — Interface & polymorphism.
 *
 * Aturan:
 *  - id: hanya huruf kecil, angka, dan tanda minus; tidak boleh kosong
 *  - game: salah satu dari mlbb, pubgm, cs2
 *  - nickname: tidak boleh kosong/spasi saja
 *  - displayOrder: minimal 1
 */
public class PlayerValidator implements Validator<WarmupPlayer> {

    @Override
    public List<String> validate(WarmupPlayer value) {
        throw new UnsupportedOperationException("TODO Q04");
    }
}
