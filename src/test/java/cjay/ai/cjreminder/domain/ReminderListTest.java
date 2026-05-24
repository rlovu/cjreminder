package cjay.ai.cjreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderListTest {

    @Test
    @DisplayName("create()로 생성 시 기본 필드가 정상 설정된다")
    void create() {
        LocalDateTime before = LocalDateTime.now();

        ReminderList list = ReminderList.create("개인", "#007AFF");

        assertThat(list.getName()).isEqualTo("개인");
        assertThat(list.getColor()).isEqualTo("#007AFF");
        assertThat(list.getReminders()).isEmpty();
        assertThat(list.getId()).isNull();
    }

    @Test
    @DisplayName("create()로 생성 시 createdAt, updatedAt이 동일하게 설정된다")
    void createSetsTimestamps() {
        LocalDateTime before = LocalDateTime.now();

        ReminderList list = ReminderList.create("업무", "#FF3B30");

        assertThat(list.getCreatedAt()).isNotNull();
        assertThat(list.getUpdatedAt()).isNotNull();
        assertThat(list.getCreatedAt()).isEqualTo(list.getUpdatedAt());
        assertThat(list.getCreatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("update() 시 updatedAt만 갱신되고 createdAt은 변하지 않는다")
    void updateRefreshesUpdatedAt() throws InterruptedException {
        ReminderList list = ReminderList.create("쇼핑", "#34C759");
        LocalDateTime originalCreatedAt = list.getCreatedAt();
        LocalDateTime originalUpdatedAt = list.getUpdatedAt();

        Thread.sleep(10);

        list.update("쇼핑 목록", "#FF9500");

        assertThat(list.getName()).isEqualTo("쇼핑 목록");
        assertThat(list.getColor()).isEqualTo("#FF9500");
        assertThat(list.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(list.getUpdatedAt()).isAfter(originalUpdatedAt);
    }
}
