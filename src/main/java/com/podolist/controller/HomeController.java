package com.podolist.controller;

/**
 * HomeController
 */

@RestController
public class HomeController {

    DefaultHomeService service;

    public HomeController(@Autowired DefaultHomeService service){
        this.service = service;
    }
}