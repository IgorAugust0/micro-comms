package com.igor.microcomms.products_api.modules.category.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.category.dto.CategoryRequest;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.category.model.Category;
import com.igor.microcomms.products_api.modules.category.repository.CategoryRepository;

import org.springframework.stereotype.Service;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse findByIdResponse(Integer id) {
        return CategoryResponse.of(findById(id));
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream() // convert Category to CategoryResponse
                .map(CategoryResponse::of) // apply CategoryResponse.of to each Category
                .toList(); // convert Stream to List
    }

    public List<CategoryResponse> findByDescription(String description) {
        if (isEmpty(description)) {
            throw new ValidationException("Category description is required");
        }
        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .toList();
    }

    public Category findById(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("Category id is required");
        }
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Category not found"));
    }

    // convert CategoryRequest to Category and save it
    public CategoryResponse save(CategoryRequest request) {
        validateCategoryRequest(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    private void validateCategoryRequest(CategoryRequest request) {
        if (isEmpty(request.getDescription())) {
            throw new ValidationException("Description is required");
        }
    }
}
