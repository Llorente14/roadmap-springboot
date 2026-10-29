package com.alterego.fondasi;

/**
 * F02 — if/else, loop, switch.
 */
public final class Ranks {

    private Ranks() {
    }

    /**
     * Tier dari poin:
     *   di bawah 1000 -> "Warrior"
     *   1000–1999     -> "Elite"
     *   2000–2999     -> "Master"
     *   3000–3999     -> "Epic"
     *   4000 ke atas  -> "Legend"
     */
    public static String tierOf(int points) {
        throw new UnsupportedOperationException("TODO F02");
    }

    /** 1 + 2 + ... + n memakai loop. n <= 0 -> 0. sumUpTo(4) -> 10 */
    public static int sumUpTo(int n) {
        throw new UnsupportedOperationException("TODO F02");
    }

    /** countdown(3) -> "3 2 1 GO", countdown(0) -> "GO" */
    public static String countdown(int from) {
        throw new UnsupportedOperationException("TODO F02");
    }

    /**
     * Singkatan role (pakai switch):
     *   "Jungler" -> "JG", "Roamer" -> "RM", "Gold Laner" -> "GL",
     *   "EXP Laner" -> "EXP", "Mid Laner" -> "MID", lainnya -> "?"
     */
    public static String roleShort(String role) {
        throw new UnsupportedOperationException("TODO F02");
    }
}
