package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Category;
import com.enigma.procurementwarehouse.repository.CategoryRepository;
import com.enigma.procurementwarehouse.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category create(Category category) {
        try {
            categoryRepository.findByName(category.getName());
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "category already exist");
        }
    }

    @Override
    public void deleteCategory(String id) {
        try {
            categoryRepository.findById(id);
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id admin not found");
        }
    }
}
