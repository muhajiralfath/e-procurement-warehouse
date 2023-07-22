package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.Category;

public interface CategoryService {
  Category create(Category category);
  void deleteCategory(String id);
}
