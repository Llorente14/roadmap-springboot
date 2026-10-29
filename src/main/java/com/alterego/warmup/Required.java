package com.alterego.warmup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Quest Q06 — Annotation.
 *
 * Penanda field wajib. Ada satu hal yang kurang di deklarasi ini sehingga
 * RequiredFieldChecker tidak akan pernah "melihat" anotasinya saat program berjalan.
 */
@Target(ElementType.FIELD)
public @interface Required {
}
