package com.igor.microcomms.products_api.modules.category.service;

import com.igor.microcomms.products_api.config.exception.SuccessResponse;
import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.category.dto.CategoryRequest;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.category.model.Category;
import com.igor.microcomms.products_api.modules.category.repository.CategoryRepository;
import com.igor.microcomms.products_api.modules.product.service.ProductService;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static com.igor.microcomms.products_api.config.util.ResponseUtil.convertToResponse;
import static com.igor.microcomms.products_api.config.util.ResponseUtil.validateNotEmpty;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Lazy
    private final ProductService productService;

    // return all categories and convert to response
    public List<CategoryResponse> findAll() {
        return convertToResponse(categoryRepository.findAll(), CategoryResponse::of);
    }

    // retrieve category by ID with validation
    public Category findById(Integer id) {
        validateNotEmpty(id, "Category ID is required");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Category not found"));
    }

    // convert Category to CategoryResponse by ID
    public CategoryResponse findByIdResponse(Integer id) {
        return CategoryResponse.of(findById(id));
    }

    // return categories by description and convert to response
    public List<CategoryResponse> findByDescription(String description) {
        validateNotEmpty(description, "Category description is required");
        return convertToResponse(categoryRepository.findByDescriptionIgnoreCaseContaining(description),
                CategoryResponse::of);
    }

    // validate CategoryRequest description is not empty
    private void validateCategoryRequest(CategoryRequest request) {
        validateNotEmpty(request.getDescription(), "Description is required");
    }

    // convert CategoryRequest to Category and save it
    public CategoryResponse save(CategoryRequest request) {
        validateCategoryRequest(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse update(Integer id, CategoryRequest request) {
        validateNotEmpty(id, "Category ID is required");
        validateCategoryRequest(request);
        var category = Category.of(request);
        // var category = findById(id);
        // category.setDescription(request.getDescription());
        category.setId(id);
        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    // delete category by ID
    public SuccessResponse delete(Integer id) {
        validateNotEmpty(id, "Category ID is required");
        if (productService.existsByCategoryId(id)) {
            throw new ValidationException("Category is in use");
        }
        categoryRepository.deleteById(id);
        return SuccessResponse.create("Category deleted successfully");
    }

}
