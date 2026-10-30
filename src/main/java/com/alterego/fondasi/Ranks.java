package com.alterego.fondasi;

/**
 * F02 — if/else, loop, switch.
 */
public final class Ranks {

    private Ranks() {
    }

    /**
     * Tier dari poin:
     * di bawah 1000 -> "Warrior"
     * 1000–1999 -> "Elite"
     * 2000–2999 -> "Master"
     * 3000–3999 -> "Epic"
     * 4000 ke atas -> "Legend"
     */
    public static String tierOf(int points) {
        if (points >= 4000) {
            return "Legend";
        } else if (points >= 3000) {
            return "Epic";

        } else if (points >= 2000) {
            return "Master";

        } else if (points >= 1000) {
            return "Elite";

        } else {
            return "Warrior";
        }
    }

    /** 1 + 2 + ... + n memakai loop. n <= 0 -> 0. sumUpTo(4) -> 10 */
    public static int sumUpTo(int n) {
        int sum = 0;
        for (int i = n; i >= 0; i--) {
            sum += i;
        }
        return sum;
    }

    /** countdown(3) -> "3 2 1 GO", countdown(0) -> "GO" */
    public static String countdown(int from) {
        StringBuilder sb = new StringBuilder();
        while (from > 0) {
            sb.append(from).append(" ");
            from--;
        }
        sb.append("GO");
        return sb.toString();
    }

    /**
     * Singkatan role (pakai switch):
     * "Jungler" -> "JG", "Roamer" -> "RM", "Gold Laner" -> "GL",
     * "EXP Laner" -> "EXP", "Mid Laner" -> "MID", lainnya -> "?"
     */
    public static String roleShort(String role) {
        switch (role) {
            case "Jungler":
                return "JG";

            case "Roamer":
                return "RM";
            case "Gold Laner":
                return "GL";

            case "EXP Laner":
                return "EXP";

            case "Mid Laner":
                return "MID";

            default:
                return "?";

        }
    }
}
