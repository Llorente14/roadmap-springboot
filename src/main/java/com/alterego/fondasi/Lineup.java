package com.alterego.fondasi;

import java.util.List;

/**
 * F06 — Array dan List.
 *
 * Lineup berisi maksimal 5 nickname, urut sesuai urutan masuk.
 */
public class Lineup {

    /** Tambah pemain. Sudah ada atau lineup penuh (5) -> tidak ditambah, kembalikan false. */
    public boolean add(String nickname) {
        throw new UnsupportedOperationException("TODO F06");
    }

    /** Hapus pemain. Tidak ada -> false. */
    public boolean remove(String nickname) {
        throw new UnsupportedOperationException("TODO F06");
    }

    /** Pemain di posisi index (mulai dari 0). */
    public String get(int index) {
        throw new UnsupportedOperationException("TODO F06");
    }

    public int size() {
        throw new UnsupportedOperationException("TODO F06");
    }

    /**
     * Daftar pemain. Yang memanggil TIDAK boleh bisa mengubah isi lineup lewat list ini
     * (mis. list.clear() dari luar tidak boleh mengosongkan lineup).
     */
    public List<String> players() {
        throw new UnsupportedOperationException("TODO F06");
    }

    /** Skor terbesar dalam array. Array kosong -> 0. maxScore(new int[]{3, 9, 4}) -> 9 */
    public static int maxScore(int[] scores) {
        throw new UnsupportedOperationException("TODO F06");
    }

    /** Nama dengan panjang minimal minLength, urutan tetap. */
    public static List<String> longNames(List<String> names, int minLength) {
        throw new UnsupportedOperationException("TODO F06");
    }
}
