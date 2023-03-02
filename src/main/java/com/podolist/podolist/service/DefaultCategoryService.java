package com.podolist.podolist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.podolist.podolist.Entity.Category;
import com.podolist.podolist.repository.JPACategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DefaultCategoryService implements CategoryService {

    private final JPACategoryRepository repo;

    @Override
    public List<Category> getParentCategories(Long creatorId) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(creatorId);
        return repo.findByCreatorIdInAndParentIdIsNull(list);
    }

    @Override
    public List<Category> getChildCategories(Long parentId) {
        return repo.findByParentId(parentId);
    }
}
