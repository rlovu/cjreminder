package cjay.ai.cjreminder.service;

import cjay.ai.cjreminder.domain.ReminderList;
import cjay.ai.cjreminder.service.ports.inp.ReminderListService;
import cjay.ai.cjreminder.repository.ReminderListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderListService implements ReminderListService {

    private final ReminderListRepository reminderListRepository;

    @Override
    public List<ReminderList> findAll() {
        return reminderListRepository.findAll();
    }

    @Override
    public ReminderList findById(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReminderList not found: " + id));
    }

    @Override
    @Transactional
    public ReminderList create(String name, String color) {
        ReminderList list = ReminderList.create(name, color);
        return reminderListRepository.save(list);
    }

    @Override
    @Transactional
    public ReminderList update(Long id, String name, String color) {
        ReminderList list = findById(id);
        list.update(name, color);
        return list;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReminderList list = findById(id);
        reminderListRepository.delete(list);
    }
}
