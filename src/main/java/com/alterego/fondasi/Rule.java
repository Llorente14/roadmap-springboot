package com.alterego.fondasi;

/** Syarat seleksi calon pemain (F11). Sudah jadi. */
@FunctionalInterface
public interface Rule {

    boolean accept(Candidate candidate);
}
