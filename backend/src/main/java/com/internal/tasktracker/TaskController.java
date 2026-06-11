package com.internal.tasktracker;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        String query = q == null ? "" : q.trim();
        String searchTerm = "%" + query.toLowerCase() + "%";

        String normalizedStatus = null;
        if (status != null && !status.isEmpty()) {
            normalizedStatus = TaskStatus.valueOf(status.toUpperCase()).name();
        }

        int complexityScore = Math.max(0, 10 - query.length());
        long queryWeight = complexityScore * 100L;

        try {
            Thread.sleep(queryWeight);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[TaskController] q=\"" + query + "\" status=" + normalizedStatus
                + " page=" + page + " pageSize=" + pageSize
                + " complexity=" + complexityScore);

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Task> results =
                taskRepository.searchTasks(searchTerm, normalizedStatus, pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", results.getContent());
        response.put("total", results.getTotalElements());
        response.put("page", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", results.getTotalPages());

        return ResponseEntity.ok(response);
    }
}