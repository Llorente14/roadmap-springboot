package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

import tools.jackson.databind.JsonNode;

@DisplayName("Q25 · Produk: seed + parsing harga + GET /api/v1/products")
class Q25_ProductTest extends QuestSpringTest {

    private static final String JERSEY = "Alter Ego Esports Homura 26/27 Official Jersey - Home";

    @Test
    @DisplayName("semua produk tersedia, default urut nama A-Z")
    void listAll() throws Exception {
        JsonNode body = getJson("/api/v1/products");

        assertThat(body.size()).isEqualTo(SeedFiles.products().size());
        List<String> names = values(body, "name");
        assertThat(names).isSortedAccordingTo(Comparator.naturalOrder());
        JsonNode first = body.get(0);
        assertThat(first.has("slug") && first.has("priceMin") && first.has("priceMax") && first.has("imageUrl")).isTrue();
    }

    @Test
    @DisplayName("harga diparse jadi angka; slug dibuat dari nama")
    void parsedFields() throws Exception {
        JsonNode jersey = find(getJson("/api/v1/products"), JERSEY);
        assertThat(jersey.path("slug").asString()).isEqualTo("alter-ego-esports-homura-26-27-official-jersey-home");
        assertThat(jersey.path("priceMin").asLong()).isEqualTo(585000);
        assertThat(jersey.path("priceMax").asLong()).isEqualTo(585000);
    }

    @Test
    @DisplayName("harga rentang (\"Rp325.000 - Rp335.000\") -> priceMin & priceMax berbeda")
    void priceRange() throws Exception {
        String rangeName = SeedFiles.products().stream()
                .filter(p -> p.path("harga").asString().contains("-"))
                .map(p -> p.path("nama").asString())
                .findFirst().orElseThrow();

        JsonNode product = find(getJson("/api/v1/products"), rangeName);
        assertThat(product.path("priceMin").asLong()).isEqualTo(325000);
        assertThat(product.path("priceMax").asLong()).isEqualTo(335000);
    }

    @Test
    @DisplayName("?maxPrice= dan ?sort=price_asc / price_desc")
    void filterAndSort() throws Exception {
        JsonNode cheap = getJson("/api/v1/products?maxPrice=100000&sort=price_asc");
        assertThat(cheap.size()).isPositive();
        List<Long> mins = new ArrayList<>();
        cheap.forEach(p -> mins.add(p.path("priceMin").asLong()));
        assertThat(mins).allMatch(v -> v <= 100000).isSorted();

        JsonNode desc = getJson("/api/v1/products?sort=price_desc");
        List<Long> maxes = new ArrayList<>();
        desc.forEach(p -> maxes.add(p.path("priceMax").asLong()));
        assertThat(maxes).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("GET /api/v1/products/{slug} -> 200; slug tidak ada -> 404; sort tidak dikenal -> 400")
    void detailAndErrors() throws Exception {
        String slug = getJson("/api/v1/products").get(0).path("slug").asString();

        assertThat(getJson("/api/v1/products/" + slug).path("slug").asString()).isEqualTo(slug);
        mvc.perform(get("/api/v1/products/tidak-ada-produk-ini")).andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/products?sort=random")).andExpect(status().isBadRequest());
    }

    private static JsonNode find(JsonNode array, String name) {
        for (JsonNode p : array) {
            if (name.equals(p.path("name").asString())) {
                return p;
            }
        }
        throw new AssertionError("Produk '" + name + "' tidak ada di respons");
    }

    private static List<String> values(JsonNode array, String field) {
        List<String> out = new ArrayList<>();
        array.forEach(n -> out.add(n.path(field).asString()));
        return out;
    }
}
