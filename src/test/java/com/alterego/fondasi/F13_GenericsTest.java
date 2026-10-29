package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F13 · Generics")
class F13_GenericsTest {

    @Test
    @DisplayName("firstOr bekerja untuk tipe apa pun")
    void firstOr() {
        assertThat(Boxes.firstOr(List.of("NINO", "ACES"), "-")).isEqualTo("NINO");
        assertThat(Boxes.firstOr(List.<String>of(), "-")).isEqualTo("-");
        int first = Boxes.firstOr(List.of(7, 8), 0);
        assertThat(first).isEqualTo(7);
    }

    @Test
    @DisplayName("repeat membuat list berisi item yang sama")
    void repeat() {
        assertThat(Boxes.repeat("GG", 3)).containsExactly("GG", "GG", "GG");
        assertThat(Boxes.repeat(1, 0)).isEmpty();
    }

    @Test
    @DisplayName("invert menukar kunci dan nilai")
    void invert() {
        Map<String, String> roles = Map.of("nino", "EXP", "kiboy", "RM");
        assertThat(Boxes.invert(roles)).containsOnly(Map.entry("EXP", "nino"), Map.entry("RM", "kiboy"));
        Map<Integer, String> byNumber = Boxes.invert(Map.of("one", 1));
        assertThat(byNumber).containsOnly(Map.entry(1, "one"));
    }

    @Test
    @DisplayName("TypedBag menyimpan satu nilai per tipe dan mengembalikannya tanpa cast")
    void typedBag() {
        TypedBag bag = new TypedBag();
        bag.put(String.class, "NINO");
        bag.put(Integer.class, 7);
        bag.put(String.class, "ACES");

        String name = bag.get(String.class).orElseThrow();
        int number = bag.get(Integer.class).orElseThrow();
        assertThat(name).isEqualTo("ACES");
        assertThat(number).isEqualTo(7);
        assertThat(bag.get(LocalDate.class)).isEmpty();
    }

    @Test
    @DisplayName("record generik Pair<A, B> buatanmu dengan swap()")
    void pair() {
        Class<?> type = Reflect.type("Pair");
        assertThat(type.isRecord()).as("Pair harus record").isTrue();
        assertThat(type.getTypeParameters()).as("Pair butuh dua type parameter").hasSize(2);

        Object p = Reflect.create("Pair", "NINO", 12);
        assertThat(Reflect.prop(p, "first")).isEqualTo("NINO");
        assertThat(Reflect.prop(p, "second")).isEqualTo(12);

        Object swapped = Reflect.call(p, "swap");
        assertThat(Reflect.prop(swapped, "first")).isEqualTo(12);
        assertThat(Reflect.prop(swapped, "second")).isEqualTo("NINO");
    }
}
