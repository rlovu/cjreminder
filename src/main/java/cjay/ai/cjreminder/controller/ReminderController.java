package cjay.ai.cjreminder.controller;

import cjay.ai.cjreminder.dto.ReminderRequest;
import cjay.ai.cjreminder.dto.ReminderResponse;
import cjay.ai.cjreminder.dto.SummaryResponse;
import cjay.ai.cjreminder.service.ports.inp.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping("/api/lists/{listId}/reminders")
    public List<ReminderResponse> findByListId(@PathVariable Long listId) {
        return reminderService.findByListId(listId).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @PostMapping("/api/lists/{listId}/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@PathVariable Long listId, @RequestBody ReminderRequest request) {
        return ReminderResponse.from(
                reminderService.create(listId, request.getTitle())
        );
    }

    @PutMapping("/api/reminders/{id}")
    public ReminderResponse update(@PathVariable Long id, @RequestBody ReminderRequest request) {
        return ReminderResponse.from(
                reminderService.update(id, request.getTitle(), request.getNotes(),
                        request.getDueDate(), request.getPriority(),
                        Boolean.TRUE.equals(request.getFlagged()), request.getListId())
        );
    }

    @DeleteMapping("/api/reminders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderService.delete(id);
    }

    @PatchMapping("/api/reminders/{id}/toggle")
    public ReminderResponse toggleComplete(@PathVariable Long id) {
        return ReminderResponse.from(reminderService.toggleComplete(id));
    }

    @PatchMapping("/api/reminders/{id}/flag")
    public ReminderResponse toggleFlag(@PathVariable Long id) {
        return ReminderResponse.from(reminderService.toggleFlag(id));
    }

    @GetMapping("/api/reminders/today")
    public List<ReminderResponse> findToday() {
        return reminderService.findToday().stream().map(ReminderResponse::from).toList();
    }

    @GetMapping("/api/reminders/scheduled")
    public List<ReminderResponse> findScheduled() {
        return reminderService.findScheduled().stream().map(ReminderResponse::from).toList();
    }

    @GetMapping("/api/reminders/all")
    public List<ReminderResponse> findAll() {
        return reminderService.findAll().stream().map(ReminderResponse::from).toList();
    }

    @GetMapping("/api/reminders/completed")
    public List<ReminderResponse> findCompleted() {
        return reminderService.findCompleted().stream().map(ReminderResponse::from).toList();
    }

    @GetMapping("/api/reminders/flagged")
    public List<ReminderResponse> findFlagged() {
        return reminderService.findFlagged().stream().map(ReminderResponse::from).toList();
    }

    @GetMapping("/api/reminders/search")
    public List<ReminderResponse> search(@RequestParam String q) {
        return reminderService.search(q).stream().map(ReminderResponse::from).toList();
    }

    @GetMapping("/api/reminders/summary")
    public SummaryResponse summary() {
        return reminderService.summary();
    }
}
