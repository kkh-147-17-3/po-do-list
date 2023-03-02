package com.podolist.podolist.service;

import java.util.List;

import com.podolist.podolist.Entity.Category;

public interface CategoryService {
    public List<Category> getParentCategories(Long creatorId);
    public List<Category> getChildCategories(Long parentId);
}
