package com.alterego.warmup;

/**
 * Quest Q05 — Parsing harga dari data Shopee.
 *
 * Format yang ada di data asli:
 *   "Rp585.000"              -> 585000..585000
 *   "Rp22.500"               -> 22500..22500
 *   "Rp325.000 - Rp335.000"  -> 325000..335000
 * Input lain yang tidak masuk akal -> InvalidPriceException, pesannya memuat input aslinya.
 */
public final class PriceParser {

    private PriceParser() {
    }

    public static PriceRange parse(String raw) {
        throw new UnsupportedOperationException("TODO Q05");
    }
}
