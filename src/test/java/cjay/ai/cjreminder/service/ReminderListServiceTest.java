package cjay.ai.cjreminder.service;

import cjay.ai.cjreminder.domain.ReminderList;
import cjay.ai.cjreminder.service.ports.inp.ReminderListService;
import cjay.ai.cjreminder.repository.ReminderListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderListServiceTest {

    @Autowired
    private ReminderListService reminderListService;

    @Autowired
    private ReminderListRepository reminderListRepository;

    @BeforeEach
    void setUp() {
        reminderListRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("findAll - 전체 리스트를 반환한다")
    void findAll() {
        reminderListRepository.save(ReminderList.create("개인", "#007AFF"));
        reminderListRepository.save(ReminderList.create("업무", "#FF3B30"));

        List<ReminderList> result = reminderListService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findById - 존재하는 ID로 조회하면 리스트를 반환한다")
    void findById() {
        ReminderList saved = reminderListRepository.save(ReminderList.create("개인", "#007AFF"));

        ReminderList result = reminderListService.findById(saved.getId());

        assertThat(result.getName()).isEqualTo("개인");
        assertThat(result.getColor()).isEqualTo("#007AFF");
    }

    @Test
    @DisplayName("findById - 존재하지 않는 ID로 조회하면 예외가 발생한다")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderListService.findById(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("create - 새 리스트를 생성하고 DB에 저장한다")
    void create() {
        ReminderList result = reminderListService.create("쇼핑", "#34C759");

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("쇼핑");
        assertThat(result.getColor()).isEqualTo("#34C759");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(reminderListRepository.findById(result.getId())).isPresent();
    }

    @Test
    @DisplayName("update - 기존 리스트의 이름과 색상을 수정한다")
    void update() {
        ReminderList saved = reminderListRepository.save(ReminderList.create("쇼핑", "#34C759"));

        ReminderList result = reminderListService.update(saved.getId(), "장보기", "#FF9500");

        assertThat(result.getName()).isEqualTo("장보기");
        assertThat(result.getColor()).isEqualTo("#FF9500");
        assertThat(result.getCreatedAt()).isEqualTo(saved.getCreatedAt());
        assertThat(result.getUpdatedAt()).isAfter(saved.getCreatedAt());
    }

    @Test
    @DisplayName("delete - 리스트를 삭제한다")
    void delete() {
        ReminderList saved = reminderListRepository.save(ReminderList.create("삭제용", "#FF3B30"));
        Long id = saved.getId();

        reminderListService.delete(id);

        assertThat(reminderListRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("delete - 존재하지 않는 ID로 삭제하면 예외가 발생한다")
    void deleteNotFound() {
        assertThatThrownBy(() -> reminderListService.delete(999L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
