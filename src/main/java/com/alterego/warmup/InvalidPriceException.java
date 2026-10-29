package com.alterego.warmup;

/**
 * Quest Q05 — Exception.
 *
 * Kelas ini sengaja diturunkan dari Exception (checked). Test Q05 menuntut sesuatu yang lain.
 * Cari tahu beda checked vs unchecked exception, lalu putuskan dan perbaiki.
 */
public class InvalidPriceException extends Exception {

    public InvalidPriceException(String message) {
        super(message);
    }
}
