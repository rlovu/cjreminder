package cjay.ai.cjreminder.dto;

import cjay.ai.cjreminder.domain.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {

    private String title;
    private String notes;
    private LocalDate dueDate;
    private Priority priority;
    private Boolean flagged;
    private Long listId;
}
