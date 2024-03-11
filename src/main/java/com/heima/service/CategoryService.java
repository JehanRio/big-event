package com.heima.service;

import com.heima.pojo.Category;

import java.util.List;

public interface CategoryService {
    void add(Category category);

    List<Category> list();

    Category findById(Integer id);
}
