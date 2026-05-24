package cjay.ai.cjreminder.dto;

import cjay.ai.cjreminder.domain.ReminderList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReminderListResponse {

    private Long id;
    private String name;
    private String color;
    private int reminderCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderListResponse from(ReminderList list) {
        int count = (int) list.getReminders().stream()
                .filter(r -> !r.isCompleted())
                .count();
        return new ReminderListResponse(
                list.getId(),
                list.getName(),
                list.getColor(),
                count,
                list.getCreatedAt(),
                list.getUpdatedAt()
        );
    }
}
