package com.alterego.fondasi;

public class NicknameRule implements Rule {
    private final int maxLength;

    public NicknameRule(int maxLength) {
        this.maxLength = maxLength;

    }

    @Override
    public boolean accept(Candidate candidate) {
        return candidate.nickname().length() <= maxLength;
    }
}
