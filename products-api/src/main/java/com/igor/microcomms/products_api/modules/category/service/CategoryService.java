package com.igor.microcomms.products_api.modules.category.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.category.dto.CategoryRequest;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.category.model.Category;
import com.igor.microcomms.products_api.modules.category.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // return all categories and convert to response
    public List<CategoryResponse> findAll() {
        return convertToResponse(categoryRepository.findAll());
    }

    // convert Category to CategoryResponse by ID
    public CategoryResponse findByIdResponse(Integer id) {
        return CategoryResponse.of(findById(id));
    }

    // return categories by description and convert to response
    public List<CategoryResponse> findByDescription(String description) {
        validateNotEmpty(description, "Category description is required");
        return convertToResponse(categoryRepository.findByDescriptionIgnoreCaseContaining(description));
    }

    // retrieve category by ID with validation
    public Category findById(Integer id) {
        validateNotEmpty(id, "Category ID is required");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Category not found"));
    }

    private void validateCategoryRequest(CategoryRequest request) {
        validateNotEmpty(request.getDescription(), "Description is required");
    }

    private void validateNotEmpty(Object value, String message) {
        if (isEmpty(value)) {
            throw new ValidationException(message);
        }
    }

    private List<CategoryResponse> convertToResponse(List<Category> categories) {
        return categories.stream().map(CategoryResponse::of).toList();
    }

    // convert CategoryRequest to Category and save it
    public CategoryResponse save(CategoryRequest request) {
        validateCategoryRequest(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

}
