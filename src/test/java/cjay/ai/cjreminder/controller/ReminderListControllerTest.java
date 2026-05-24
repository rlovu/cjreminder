package cjay.ai.cjreminder.controller;

import cjay.ai.cjreminder.repository.ReminderListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderListRepository reminderListRepository;

    @BeforeEach
    void setUp() {
        reminderListRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("POST /api/lists - 리스트를 생성하고 201을 반환한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "개인", "color": "#007AFF"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("개인"))
                .andExpect(jsonPath("$.color").value("#007AFF"))
                .andExpect(jsonPath("$.reminderCount").value(0))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/lists - 전체 리스트를 조회한다")
    void findAll() throws Exception {
        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "개인", "color": "#007AFF"}
                        """));
        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "업무", "color": "#FF3B30"}
                        """));

        mockMvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("개인"))
                .andExpect(jsonPath("$[1].name").value("업무"));
    }

    @Test
    @DisplayName("GET /api/lists/{id} - 단건 조회한다")
    void findById() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "개인", "color": "#007AFF"}
                                """))
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(get("/api/lists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("개인"));
    }

    @Test
    @DisplayName("GET /api/lists/{id} - 존재하지 않는 ID는 404를 반환한다")
    void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/lists/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/lists/{id} - 리스트를 수정한다")
    void update() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "쇼핑", "color": "#34C759"}
                                """))
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(put("/api/lists/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "장보기", "color": "#FF9500"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("장보기"))
                .andExpect(jsonPath("$.color").value("#FF9500"));
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} - 리스트를 삭제하고 204를 반환한다")
    void deleteList() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "삭제용", "color": "#FF3B30"}
                                """))
                .andReturn().getResponse().getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(delete("/api/lists/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/lists/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} - 존재하지 않는 ID는 404를 반환한다")
    void deleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/lists/{id}", 999))
                .andExpect(status().isNotFound());
    }
}
