package com.alterego.quest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Basis semua test quest Spring (Q08+). JANGAN DIUBAH.
 *
 * - Menyalakan aplikasi utuh dengan profil "test" (lihat src/test/resources/application-test.yaml).
 * - MongoDB embedded, seed dari classpath:seed-test/.
 * - MockMvc untuk memanggil endpoint tanpa membuka port.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class QuestSpringTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected MongoTemplate mongo;

    @Autowired
    protected ApplicationContext ctx;

    private static final JsonMapper JSON = JsonMapper.builder().build();

    /** GET url, wajib 200, kembalikan body sebagai JSON. */
    protected JsonNode getJson(String url) throws Exception {
        String body = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return JSON.readTree(body);
    }

    protected static JsonNode parse(String body) {
        return JSON.readTree(body);
    }
}
