package com.podolist.podolist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.podolist.podolist.Entity.Category;
import com.podolist.podolist.service.CategoryService;

import lombok.RequiredArgsConstructor;

/**
 * HomeController
 */

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping("parent")
    public List<Category> getParentCategories(){
        Long creatorId = 1L;
        return service.getParentCategories(creatorId);
    }
    @GetMapping("child")
    public List<Category> getChildCategory(Long parentId){
        return service.getChildCategories(parentId);
    }
}