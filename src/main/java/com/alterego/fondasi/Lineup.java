package com.alterego.fondasi;

import java.util.ArrayList;
import java.util.List;

/**
 * F06 — Array dan List.
 *
 * Lineup berisi maksimal 5 nickname, urut sesuai urutan masuk.
 */
public class Lineup {
    List<String> lineUpList = new ArrayList<>();

    /**
     * Tambah pemain. Sudah ada atau lineup penuh (5) -> tidak ditambah, kembalikan
     * false.
     */
    public boolean add(String nickname) {
        if (lineUpList.size() >= 5) {
            return false;
        }
        if (lineUpList.contains(nickname)) {
            return false;
        }
        lineUpList.add(nickname);
        return true;

    }

    /** Hapus pemain. Tidak ada -> false. */
    public boolean remove(String nickname) {
        if (!lineUpList.contains(nickname)) {
            return false;
        }
        lineUpList.remove(nickname);
        return true;
    }

    /** Pemain di posisi index (mulai dari 0). */
    public String get(int index) {
        return lineUpList.get(index);

    }

    public int size() {
        return lineUpList.size();
    }

    /**
     * Daftar pemain. Yang memanggil TIDAK boleh bisa mengubah isi lineup lewat list
     * ini
     * (mis. list.clear() dari luar tidak boleh mengosongkan lineup).
     */
    public List<String> players() {
        List<String> salinanLineUp = List.copyOf(lineUpList);
        return salinanLineUp;
    }

    /**
     * Skor terbesar dalam array. Array kosong -> 0. maxScore(new int[]{3, 9, 4}) ->
     * 9
     */
    public static int maxScore(int[] scores) {
        if (scores.length == 0) {
            return 0;
        }
        int maxScore = scores[0];
        for (int score : scores) {
            if (maxScore < score) {
                maxScore = score;
            }
        }
        return maxScore;
    }

    /** Nama dengan panjang minimal minLength, urutan tetap. */
    public static List<String> longNames(List<String> names, int minLength) {
        List<String> filterLineUp = new ArrayList<>();
        for (String name : names) {
            if (name.length() >= minLength) {
                filterLineUp.add(name);
            }
        }
        return filterLineUp;
    }
}
