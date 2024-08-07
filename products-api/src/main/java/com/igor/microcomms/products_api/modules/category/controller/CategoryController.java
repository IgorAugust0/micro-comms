package com.igor.microcomms.products_api.modules.category.controller;

import com.igor.microcomms.products_api.modules.category.dto.CategoryRequest;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.category.service.CategoryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }
}
