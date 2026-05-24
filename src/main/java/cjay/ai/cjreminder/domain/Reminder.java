package cjay.ai.cjreminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String notes;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Priority priority = Priority.NONE;

    @Builder.Default
    private boolean flagged = false;

    @Builder.Default
    private boolean completed = false;

    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private ReminderList list;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Reminder create(String title, ReminderList list) {
        LocalDateTime now = LocalDateTime.now();
        return Reminder.builder()
                .title(title)
                .list(list)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void update(String title, String notes, LocalDate dueDate, Priority priority, boolean flagged) {
        this.title = title;
        this.notes = notes;
        this.dueDate = dueDate;
        this.priority = priority;
        this.flagged = flagged;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleComplete() {
        this.completed = !this.completed;
        this.completedAt = this.completed ? LocalDateTime.now() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleFlag() {
        this.flagged = !this.flagged;
        this.updatedAt = LocalDateTime.now();
    }
}
