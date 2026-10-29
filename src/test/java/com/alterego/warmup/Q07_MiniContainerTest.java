package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Q07 · Dependency Injection manual")
class Q07_MiniContainerTest {

    interface PlayerSource {
        List<String> nicknames();
    }

    static class RosterService {
        private final PlayerSource source;

        RosterService(PlayerSource source) {
            this.source = source;
        }

        String headline() {
            return String.join(", ", source.nicknames());
        }
    }

    @Test
    @DisplayName("objek dibuat lewat factory dan dependensinya diambil dari container")
    void wiresDependencies() {
        MiniContainer c = new MiniContainer();
        c.register(PlayerSource.class, () -> () -> List.of("NINO", "YAZUKEE"));
        c.register(RosterService.class, () -> new RosterService(c.get(PlayerSource.class)));

        assertThat(c.get(RosterService.class).headline()).isEqualTo("NINO, YAZUKEE");
    }

    @Test
    @DisplayName("singleton: get berulang mengembalikan instance yang sama")
    void singleton() {
        MiniContainer c = new MiniContainer();
        c.register(RosterService.class, () -> new RosterService(List::of));

        assertThat(c.get(RosterService.class)).isSameAs(c.get(RosterService.class));
    }

    @Test
    @DisplayName("lazy: factory belum dipanggil saat register, dan hanya dipanggil sekali")
    void lazy() {
        AtomicInteger calls = new AtomicInteger();
        MiniContainer c = new MiniContainer();
        c.register(PlayerSource.class, () -> {
            calls.incrementAndGet();
            return List::of;
        });

        assertThat(calls).hasValue(0);
        c.get(PlayerSource.class);
        c.get(PlayerSource.class);
        assertThat(calls).hasValue(1);
    }

    @Test
    @DisplayName("tipe yang belum terdaftar: IllegalStateException yang menyebut nama class")
    void missing() {
        MiniContainer c = new MiniContainer();
        assertThatThrownBy(() -> c.get(RosterService.class))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RosterService");
    }
}
