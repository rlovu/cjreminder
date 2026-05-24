package cjay.ai.cjreminder.dto;

import cjay.ai.cjreminder.domain.Priority;
import cjay.ai.cjreminder.domain.Reminder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReminderResponse {

    private Long id;
    private String title;
    private String notes;
    private LocalDate dueDate;
    private Priority priority;
    private boolean flagged;
    private boolean completed;
    private LocalDateTime completedAt;
    private Long listId;
    private String listName;
    private String listColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderResponse from(Reminder reminder) {
        return new ReminderResponse(
                reminder.getId(),
                reminder.getTitle(),
                reminder.getNotes(),
                reminder.getDueDate(),
                reminder.getPriority(),
                reminder.isFlagged(),
                reminder.isCompleted(),
                reminder.getCompletedAt(),
                reminder.getList().getId(),
                reminder.getList().getName(),
                reminder.getList().getColor(),
                reminder.getCreatedAt(),
                reminder.getUpdatedAt()
        );
    }
}
