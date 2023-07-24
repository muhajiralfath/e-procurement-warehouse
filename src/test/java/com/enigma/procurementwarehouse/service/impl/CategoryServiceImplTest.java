package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Category;
import com.enigma.procurementwarehouse.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id("1")
                .name("TestCategory")
                .build();
    }

    @Test
    void create_Success() {
        when(categoryRepository.findByName(category.getName())).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);

        Category createdCategory = categoryService.create(category);

        assertNotNull(createdCategory);
        assertEquals(category.getName(), createdCategory.getName());
        assertEquals(category.getName(), createdCategory.getName());

        verify(categoryRepository, times(1)).findByName(category.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void deleteCategory_Success() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        categoryService.deleteCategory(category.getId());

        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }


    @Test
    void findById_Success() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.findById(category.getId());

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(category.getName(), foundCategory.getName());
        assertEquals(category.getName(), foundCategory.getName());

        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void findById_CategoryNotFound_ThrowsResponseStatusException() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> categoryService.findById(category.getId()));

        verify(categoryRepository, times(1)).findById(category.getId());
    }
}
