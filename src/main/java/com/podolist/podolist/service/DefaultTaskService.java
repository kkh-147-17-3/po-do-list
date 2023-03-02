package com.podolist.podolist.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.podolist.podolist.Entity.Task;
import com.podolist.podolist.repository.JPATaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultTaskService implements TaskService {
    
    public final JPATaskRepository repository;

    @Override
    public void add(Task task) {
        repository.save(task);
    }

    @Override
    public void update(Task task) {
        repository.save(task);
    }

    @Override
    public List<Task> getAllTasksAtDate(LocalDateTime date) {
        return repository.getAllTasksAtDate(date);
    }

    @Override
    public LocalDateTime updateCompletedAtById(Long id, boolean isCompleted) {
        Optional<Task> option = repository.findById(id);
        if(option.isEmpty()){
            throw new RuntimeException("존재하지 않는 id 입니다.");
        }
        Task task = option.get();
        if(isCompleted){
            var now = LocalDateTime.now();
            task.setCompletedAt(now);
        }
        else{
            task.setCompletedAt(null);
        }
        repository.save(task);
        return task.getCompletedAt();
    }

    @Override
    public int deleteAllTasksByIds(List<Long> ids) {
        
        List<Task> tasks = repository.findAllById(ids);
        tasks.stream().forEach(task->{
            task.setDeleted(true);
            task.setDeletedAt(LocalDateTime.now());
        });

        repository.saveAll(tasks);

        return tasks.size();
    }
}
