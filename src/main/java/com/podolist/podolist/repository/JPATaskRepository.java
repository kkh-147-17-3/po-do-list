package com.podolist.podolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.podolist.podolist.Entity.Task;

public interface JPATaskRepository extends JpaRepository<Task, Integer> {
    
}
