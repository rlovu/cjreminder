package cjay.ai.cjreminder.controller;

import cjay.ai.cjreminder.repository.ReminderListRepository;
import cjay.ai.cjreminder.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderListRepository reminderListRepository;

    private String listId;

    @BeforeEach
    void setUp() throws Exception {
        reminderRepository.deleteAllInBatch();
        reminderListRepository.deleteAllInBatch();

        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "테스트", "color": "#007AFF"}
                                """))
                .andReturn().getResponse().getContentAsString();
        listId = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();
    }

    @Test
    @DisplayName("POST /api/lists/{listId}/reminders - 리마인더를 생성한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "할 일"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("할 일"))
                .andExpect(jsonPath("$.listId").value(Integer.parseInt(listId)))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("GET /api/lists/{listId}/reminders - 리스트별 리마인더를 조회한다")
    void findByListId() throws Exception {
        mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "할 일 1"}
                        """));
        mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "할 일 2"}
                        """));

        mockMvc.perform(get("/api/lists/{listId}/reminders", listId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("PATCH /api/reminders/{id}/toggle - 완료 토글")
    void toggleComplete() throws Exception {
        String response = mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "할 일"}
                                """))
                .andReturn().getResponse().getContentAsString();
        String reminderId = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(patch("/api/reminders/{id}/toggle", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.completedAt").isNotEmpty());
    }

    @Test
    @DisplayName("PATCH /api/reminders/{id}/flag - 플래그 토글")
    void toggleFlag() throws Exception {
        String response = mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "할 일"}
                                """))
                .andReturn().getResponse().getContentAsString();
        String reminderId = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(patch("/api/reminders/{id}/flag", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flagged").value(true));
    }

    @Test
    @DisplayName("DELETE /api/reminders/{id} - 리마인더를 삭제한다")
    void deleteReminder() throws Exception {
        String response = mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "삭제용"}
                                """))
                .andReturn().getResponse().getContentAsString();
        String reminderId = com.jayway.jsonpath.JsonPath.read(response, "$.id").toString();

        mockMvc.perform(delete("/api/reminders/{id}", reminderId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/reminders/summary - 스마트 리스트 카운트를 반환한다")
    void summary() throws Exception {
        mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "할 일"}
                        """));

        mockMvc.perform(get("/api/reminders/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allCount").value(1))
                .andExpect(jsonPath("$.completedCount").value(0));
    }

    @Test
    @DisplayName("GET /api/reminders/search - 키워드로 검색한다")
    void search() throws Exception {
        mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "장보기"}
                        """));
        mockMvc.perform(post("/api/lists/{listId}/reminders", listId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "운동"}
                        """));

        mockMvc.perform(get("/api/reminders/search").param("q", "장보기"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("장보기"));
    }
}
