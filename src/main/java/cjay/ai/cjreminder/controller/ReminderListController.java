package cjay.ai.cjreminder.controller;

import cjay.ai.cjreminder.dto.ReminderListRequest;
import cjay.ai.cjreminder.dto.ReminderListResponse;
import cjay.ai.cjreminder.service.ports.inp.ReminderListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ReminderListController {

    private final ReminderListService reminderListService;

    @GetMapping
    public List<ReminderListResponse> findAll() {
        return reminderListService.findAll().stream()
                .map(ReminderListResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ReminderListResponse findById(@PathVariable Long id) {
        return ReminderListResponse.from(reminderListService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderListResponse create(@RequestBody ReminderListRequest request) {
        return ReminderListResponse.from(
                reminderListService.create(request.getName(), request.getColor())
        );
    }

    @PutMapping("/{id}")
    public ReminderListResponse update(@PathVariable Long id, @RequestBody ReminderListRequest request) {
        return ReminderListResponse.from(
                reminderListService.update(id, request.getName(), request.getColor())
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderListService.delete(id);
    }
}
