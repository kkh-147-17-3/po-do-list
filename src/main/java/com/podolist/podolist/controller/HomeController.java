package com.podolist.podolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.podolist.podolist.service.DefaultHomeService;

import lombok.RequiredArgsConstructor;

/**
 * HomeController
 */

@Controller
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final DefaultHomeService service;

    @GetMapping("/hello")
    public String getHome(){

        service.insertData("hello");
        System.out.println("호추르");
        return "index";
    }
}