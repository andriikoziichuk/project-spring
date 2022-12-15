package com.eproject.library.service;

import com.eproject.library.dto.CategoryDTO;
import com.eproject.library.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findById(Long id);
    Category update(Category category);
    void deleteById(Long id);
    void enableById(Long id);
    List<Category> findAllByActivated();

    // Customer
    List<CategoryDTO> getCategoryAndProduct();

}
