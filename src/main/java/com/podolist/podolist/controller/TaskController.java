package com.podolist.podolist.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.podolist.podolist.Entity.Task;
import com.podolist.podolist.service.TaskService;

import lombok.RequiredArgsConstructor;

/**
 * HomeController
 */

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    public String post(@RequestBody Task task){
        service.add(task);
        return "완료";
    }

    @PutMapping
    public String put(@RequestBody Task task){
        service.update(task);
        return "완료";
    }

    @GetMapping
    public List<Task> getAllAtDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){

        List<Task> tasks = service.getAllTasksAtDate(date);
        return tasks;
    }

    @DeleteMapping
    public int delete(@RequestParam(name = "ids") List<Long> ids){
        int result = service.deleteAllTasksByIds(ids);
        return result;
    }
}