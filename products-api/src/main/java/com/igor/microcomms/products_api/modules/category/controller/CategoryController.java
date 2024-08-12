package com.igor.microcomms.products_api.modules.category.controller;

import com.igor.microcomms.products_api.config.exception.SuccessResponse;
import com.igor.microcomms.products_api.modules.category.dto.CategoryRequest;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.category.service.CategoryService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable(value = "id") Integer id) { // use value if ur in VS Code (?)
        return categoryService.findByIdResponse(id);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable(value = "description") String description) {
        return categoryService.findByDescription(description);
    }

    @PostMapping("/")
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable(value = "id") Integer id) {
        return categoryService.delete(id);
    }
}
