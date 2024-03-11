package com.heima.service.impl;

import com.heima.mapper.CategoryMapper;
import com.heima.pojo.Category;
import com.heima.service.CategoryService;
import com.heima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        category.setCreateUser(id);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        Map<String, Object> o = ThreadLocalUtil.get();
        Integer id = (Integer) o.get("id");
        return categoryMapper.list(id);
    }

    @Override
    public Category findById(Integer id) {
        Category c = categoryMapper.findById(id);
        return c;
    }
}
