package com.igor.microcomms.products_api.modules.category.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.category.dto.CategoryRequest;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.category.model.Category;
import com.igor.microcomms.products_api.modules.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
