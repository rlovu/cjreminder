package cjay.ai.cjreminder.service.ports.inp;

import cjay.ai.cjreminder.domain.Priority;
import cjay.ai.cjreminder.domain.Reminder;
import cjay.ai.cjreminder.dto.SummaryResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReminderService {

    List<Reminder> findByListId(Long listId);

    Reminder findById(Long id);

    Reminder create(Long listId, String title);

    Reminder update(Long id, String title, String notes, LocalDate dueDate, Priority priority, boolean flagged, Long listId);

    void delete(Long id);

    Reminder toggleComplete(Long id);

    Reminder toggleFlag(Long id);

    List<Reminder> findToday();

    List<Reminder> findScheduled();

    List<Reminder> findAll();

    List<Reminder> findCompleted();

    List<Reminder> findFlagged();

    List<Reminder> search(String keyword);

    SummaryResponse summary();
}
