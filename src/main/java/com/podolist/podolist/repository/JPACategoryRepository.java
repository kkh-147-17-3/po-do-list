package com.podolist.podolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.podolist.podolist.Entity.Category;

public interface JPACategoryRepository extends JpaRepository<Category, Long>{

    public List<Category> findByParentId(Long parentId);
    public List<Category> findByCreatorIdInAndParentIdIsNull(List<Long> ids);
}
