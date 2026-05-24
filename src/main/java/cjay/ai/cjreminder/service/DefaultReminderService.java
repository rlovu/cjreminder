package cjay.ai.cjreminder.service;

import cjay.ai.cjreminder.domain.Priority;
import cjay.ai.cjreminder.domain.Reminder;
import cjay.ai.cjreminder.domain.ReminderList;
import cjay.ai.cjreminder.dto.SummaryResponse;
import cjay.ai.cjreminder.repository.ReminderRepository;
import cjay.ai.cjreminder.service.ports.inp.ReminderListService;
import cjay.ai.cjreminder.service.ports.inp.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderService implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderListService reminderListService;

    @Override
    public List<Reminder> findByListId(Long listId) {
        reminderListService.findById(listId);
        return reminderRepository.findByListIdAndCompletedFalse(listId);
    }

    @Override
    public Reminder findById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reminder not found: " + id));
    }

    @Override
    @Transactional
    public Reminder create(Long listId, String title) {
        ReminderList list = reminderListService.findById(listId);
        Reminder reminder = Reminder.create(title, list);
        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public Reminder update(Long id, String title, String notes, LocalDate dueDate, Priority priority, boolean flagged, Long listId) {
        Reminder reminder = findById(id);
        reminder.update(title, notes, dueDate, priority, flagged);
        if (listId != null && !listId.equals(reminder.getList().getId())) {
            ReminderList newList = reminderListService.findById(listId);
            reminder.setList(newList);
        }
        return reminder;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = findById(id);
        reminderRepository.delete(reminder);
    }

    @Override
    @Transactional
    public Reminder toggleComplete(Long id) {
        Reminder reminder = findById(id);
        reminder.toggleComplete();
        return reminder;
    }

    @Override
    @Transactional
    public Reminder toggleFlag(Long id) {
        Reminder reminder = findById(id);
        reminder.toggleFlag();
        return reminder;
    }

    @Override
    public List<Reminder> findToday() {
        return reminderRepository.findByDueDateAndCompletedFalse(LocalDate.now());
    }

    @Override
    public List<Reminder> findScheduled() {
        return reminderRepository.findByDueDateNotNullAndCompletedFalseOrderByDueDate();
    }

    @Override
    public List<Reminder> findAll() {
        return reminderRepository.findByCompletedFalse();
    }

    @Override
    public List<Reminder> findCompleted() {
        return reminderRepository.findByCompletedTrue();
    }

    @Override
    public List<Reminder> findFlagged() {
        return reminderRepository.findByFlaggedTrueAndCompletedFalse();
    }

    @Override
    public List<Reminder> search(String keyword) {
        return reminderRepository.findByTitleContainingIgnoreCaseOrNotesContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public SummaryResponse summary() {
        return new SummaryResponse(
                reminderRepository.countByDueDateAndCompletedFalse(LocalDate.now()),
                reminderRepository.countByDueDateNotNullAndCompletedFalse(),
                reminderRepository.countByCompletedFalse(),
                reminderRepository.countByCompletedTrue(),
                reminderRepository.countByFlaggedTrueAndCompletedFalse()
        );
    }
}
