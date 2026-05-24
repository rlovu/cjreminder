package cjay.ai.cjreminder.service;

import cjay.ai.cjreminder.domain.Priority;
import cjay.ai.cjreminder.domain.Reminder;
import cjay.ai.cjreminder.domain.ReminderList;
import cjay.ai.cjreminder.dto.SummaryResponse;
import cjay.ai.cjreminder.repository.ReminderListRepository;
import cjay.ai.cjreminder.repository.ReminderRepository;
import cjay.ai.cjreminder.service.ports.inp.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderListRepository reminderListRepository;

    private ReminderList testList;

    @BeforeEach
    void setUp() {
        reminderRepository.deleteAllInBatch();
        reminderListRepository.deleteAllInBatch();
        testList = reminderListRepository.save(ReminderList.create("테스트", "#007AFF"));
    }

    @Test
    @DisplayName("create - 리마인더를 생성한다")
    void create() {
        Reminder result = reminderService.create(testList.getId(), "할 일");

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("할 일");
        assertThat(result.getList().getId()).isEqualTo(testList.getId());
        assertThat(result.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("findByListId - 리스트의 미완료 리마인더를 조회한다")
    void findByListId() {
        reminderService.create(testList.getId(), "할 일 1");
        reminderService.create(testList.getId(), "할 일 2");
        Reminder completed = reminderService.create(testList.getId(), "완료됨");
        reminderService.toggleComplete(completed.getId());

        List<Reminder> result = reminderService.findByListId(testList.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("update - 리마인더를 수정한다")
    void update() {
        Reminder reminder = reminderService.create(testList.getId(), "원래 제목");

        Reminder result = reminderService.update(reminder.getId(), "수정된 제목", "메모",
                LocalDate.now(), Priority.HIGH, true, null);

        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getNotes()).isEqualTo("메모");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.isFlagged()).isTrue();
    }

    @Test
    @DisplayName("delete - 리마인더를 삭제한다")
    void deleteReminder() {
        Reminder reminder = reminderService.create(testList.getId(), "삭제용");
        Long id = reminder.getId();

        reminderService.delete(id);

        assertThat(reminderRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("toggleComplete - 완료 상태를 토글한다")
    void toggleComplete() {
        Reminder reminder = reminderService.create(testList.getId(), "할 일");

        Reminder toggled = reminderService.toggleComplete(reminder.getId());
        assertThat(toggled.isCompleted()).isTrue();
        assertThat(toggled.getCompletedAt()).isNotNull();

        Reminder unToggled = reminderService.toggleComplete(reminder.getId());
        assertThat(unToggled.isCompleted()).isFalse();
        assertThat(unToggled.getCompletedAt()).isNull();
    }

    @Test
    @DisplayName("toggleFlag - 플래그 상태를 토글한다")
    void toggleFlag() {
        Reminder reminder = reminderService.create(testList.getId(), "할 일");

        Reminder flagged = reminderService.toggleFlag(reminder.getId());
        assertThat(flagged.isFlagged()).isTrue();

        Reminder unflagged = reminderService.toggleFlag(reminder.getId());
        assertThat(unflagged.isFlagged()).isFalse();
    }

    @Test
    @DisplayName("findToday - 오늘 기한인 미완료 리마인더를 조회한다")
    void findToday() {
        Reminder r = reminderService.create(testList.getId(), "오늘 할 일");
        reminderService.update(r.getId(), "오늘 할 일", null, LocalDate.now(), Priority.NONE, false, null);
        reminderService.create(testList.getId(), "기한 없음");

        List<Reminder> result = reminderService.findToday();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("오늘 할 일");
    }

    @Test
    @DisplayName("findFlagged - 플래그된 미완료 리마인더를 조회한다")
    void findFlagged() {
        Reminder r = reminderService.create(testList.getId(), "플래그");
        reminderService.toggleFlag(r.getId());
        reminderService.create(testList.getId(), "일반");

        List<Reminder> result = reminderService.findFlagged();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search - 제목/메모에서 키워드 검색한다")
    void search() {
        reminderService.create(testList.getId(), "장보기");
        Reminder r2 = reminderService.create(testList.getId(), "기타");
        reminderService.update(r2.getId(), "기타", "장보기 목록 확인", null, Priority.NONE, false, null);

        List<Reminder> result = reminderService.search("장보기");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("summary - 스마트 리스트 카운트를 반환한다")
    void summary() {
        Reminder r1 = reminderService.create(testList.getId(), "오늘");
        reminderService.update(r1.getId(), "오늘", null, LocalDate.now(), Priority.NONE, false, null);
        Reminder r2 = reminderService.create(testList.getId(), "플래그");
        reminderService.toggleFlag(r2.getId());
        Reminder r3 = reminderService.create(testList.getId(), "완료");
        reminderService.toggleComplete(r3.getId());

        SummaryResponse result = reminderService.summary();

        assertThat(result.getTodayCount()).isEqualTo(1);
        assertThat(result.getAllCount()).isEqualTo(2);
        assertThat(result.getCompletedCount()).isEqualTo(1);
        assertThat(result.getFlaggedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("findById - 존재하지 않는 ID는 예외 발생")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderService.findById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
