package cjay.ai.cjreminder.repository;

import cjay.ai.cjreminder.domain.ReminderList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {
}
