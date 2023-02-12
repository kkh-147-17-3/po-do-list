package com.podolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.podolist.repository.HomeRepository;

@Service
public class DefaultHomeService implements HomeService {
    
    HomeRepository repository;

    public DefaultHomeService(@Autowired HomeRepository repository){
        this.repository = repository;
    }
}
