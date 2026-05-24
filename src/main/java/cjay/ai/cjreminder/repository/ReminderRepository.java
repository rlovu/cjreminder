package cjay.ai.cjreminder.repository;

import cjay.ai.cjreminder.domain.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByListIdAndCompletedFalse(Long listId);

    List<Reminder> findByCompletedTrue();

    List<Reminder> findByFlaggedTrueAndCompletedFalse();

    List<Reminder> findByDueDateAndCompletedFalse(LocalDate date);

    List<Reminder> findByDueDateNotNullAndCompletedFalseOrderByDueDate();

    List<Reminder> findByCompletedFalse();

    List<Reminder> findByTitleContainingIgnoreCaseOrNotesContainingIgnoreCase(String title, String notes);

    long countByListIdAndCompletedFalse(Long listId);

    long countByCompletedFalse();

    long countByCompletedTrue();

    long countByFlaggedTrueAndCompletedFalse();

    long countByDueDateAndCompletedFalse(LocalDate date);

    long countByDueDateNotNullAndCompletedFalse();
}
