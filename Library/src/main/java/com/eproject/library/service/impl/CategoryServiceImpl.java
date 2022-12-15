package com.eproject.library.service.impl;

import com.eproject.library.dto.CategoryDTO;
import com.eproject.library.model.Category;
import com.eproject.library.model.Product;
import com.eproject.library.repository.CategoryRepository;
import com.eproject.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return categoryRepository.save(categorySave);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = null;
        try {
            categoryUpdate= categoryRepository.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.set_activated(category.is_activated());
            categoryUpdate.set_deleted(category.is_deleted());
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.getReferenceById(id);
        category.set_deleted(true);
        category.set_activated(false);
        categoryRepository.save(category);
    }

    @Override
    public void enableById(Long id) {
        Category category = categoryRepository.getReferenceById(id);
        category.set_activated(true);
        category.set_deleted(false);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllByActivated() {
        return categoryRepository.findAllByActivated();
    }

    @Override
    public List<CategoryDTO> getCategoryAndProduct() {
        return categoryRepository.getCategoryAndProduct();
    }
}
