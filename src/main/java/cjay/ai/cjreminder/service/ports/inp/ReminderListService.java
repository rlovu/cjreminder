package cjay.ai.cjreminder.service.ports.inp;

import cjay.ai.cjreminder.domain.ReminderList;

import java.util.List;

public interface ReminderListService {

    List<ReminderList> findAll();

    ReminderList findById(Long id);

    ReminderList create(String name, String color);

    ReminderList update(Long id, String name, String color);

    void delete(Long id);
}
