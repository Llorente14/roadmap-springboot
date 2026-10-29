package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Q10 · Profile dev + MongoDB Atlas tanpa bocor rahasia")
class Q10_ProfilesTest {

    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    @DisplayName("application-dev.yaml ada dan connection string diambil dari env var MONGODB_URI")
    void devProfileUsesEnv() throws IOException {
        Path dev = RESOURCES.resolve("application-dev.yaml");
        assertThat(dev).as("Buat src/main/resources/application-dev.yaml").exists();
        assertThat(Files.readString(dev)).contains("${MONGODB_URI}");
    }

    @Test
    @DisplayName("tidak ada connection string asli (user:password) di file konfigurasi mana pun")
    void noSecretsCommitted() throws IOException {
        List<Path> configs;
        try (Stream<Path> files = Files.list(RESOURCES)) {
            configs = files.filter(p -> p.getFileName().toString().matches("application.*\\.(ya?ml|properties)")).toList();
        }
        for (Path config : configs) {
            assertThat(Files.readString(config))
                    .as(config + " memuat kredensial. Pindahkan ke environment variable.")
                    .doesNotContainPattern("mongodb(\\+srv)?://[^\\s$]*@");
        }
    }

    @Test
    @DisplayName(".gitignore mengabaikan file .env (kalau kamu menyimpan MONGODB_URI di sana)")
    void envIgnored() throws IOException {
        assertThat(Files.readAllLines(Path.of(".gitignore"))).contains(".env");
    }
}
