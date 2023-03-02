package com.podolist.podolist.service;

import java.time.LocalDateTime;
import java.util.List;

import com.podolist.podolist.Entity.Task;

public interface TaskService {

    public void add(Task task);

    public void update(Task task);

    public List<Task> getAllTasksAtDate(LocalDateTime date);

    public LocalDateTime updateCompletedAtById(Long id, boolean isCompleted);

    public int deleteAllTasksByIds(List<Long> ids);
}
