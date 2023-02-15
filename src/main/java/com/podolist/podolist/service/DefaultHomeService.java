package com.podolist.podolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.podolist.podolist.Entity.Task;
import com.podolist.podolist.repository.JPATaskRepository;

@Service
public class DefaultHomeService implements HomeService {
    
    JPATaskRepository repository;

    public DefaultHomeService(@Autowired JPATaskRepository repository){
        this.repository = repository;
    }

    public void insertData(String string) {
        Task task = Task.builder().details(string).build();
        repository.save(task);
    }
}
