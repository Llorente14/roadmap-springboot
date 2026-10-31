package com.alterego.fondasi;

public record MatchResult(String home, String away, int homeScore, int awayScore) {
    public String winner() {
        if (homeScore < awayScore) {
            return away;
        } else if (homeScore == awayScore) {
            return "DRAW";
        } else {
            return home;
        }
    }
}
