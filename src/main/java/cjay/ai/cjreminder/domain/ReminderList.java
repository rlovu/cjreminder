package cjay.ai.cjreminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reminder_list")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ReminderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reminder> reminders = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReminderList create(String name, String color) {
        LocalDateTime now = LocalDateTime.now();
        return ReminderList.builder()
                .name(name)
                .color(color)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
        this.updatedAt = LocalDateTime.now();
    }
}
