package com.alterego.quest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Helper kit. JANGAN DIUBAH.
 *
 * Membaca file seed test (src/test/resources/seed-test) supaya angka yang diharapkan test
 * dihitung dari data, bukan ditulis tangan. Data AlterEgo berubah? Test tetap benar.
 */
public final class SeedFiles {

    private static final JsonMapper JSON = JsonMapper.builder().build();
    private static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

    private SeedFiles() {
    }

    public static List<JsonNode> players() {
        return items("classpath:seed-test/rosters/*.json", "players");
    }

    public static List<JsonNode> achievements() {
        return items("classpath:seed-test/achievements/*.json", "achievements");
    }

    public static List<JsonNode> achievements(String game) {
        return achievements().stream().filter(a -> game.equals(a.path("game").asString())).toList();
    }

    /** Isi "summary" yang tertulis di file achievement suatu game (bisa berisi null). */
    public static JsonNode declaredSummary(String game) {
        return read("classpath:seed-test/achievements/" + game + ".json").path("summary");
    }

    public static List<JsonNode> products() {
        List<JsonNode> out = new ArrayList<>();
        read("classpath:seed-test/products.json").forEach(out::add);
        return out;
    }

    private static List<JsonNode> items(String pattern, String field) {
        List<JsonNode> out = new ArrayList<>();
        for (Resource r : resources(pattern)) {
            read(r).path(field).forEach(out::add);
        }
        return out;
    }

    private static Resource[] resources(String pattern) {
        try {
            Resource[] found = RESOLVER.getResources(pattern);
            Arrays.sort(found, Comparator.comparing(Resource::getFilename));
            return found;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static JsonNode read(String location) {
        return read(RESOLVER.getResource(location));
    }

    private static JsonNode read(Resource resource) {
        try (InputStream in = resource.getInputStream()) {
            return JSON.readTree(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
