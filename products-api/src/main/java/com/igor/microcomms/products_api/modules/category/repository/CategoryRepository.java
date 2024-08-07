package com.igor.microcomms.products_api.modules.category.repository;

import com.igor.microcomms.products_api.modules.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // SELECT * FROM category WHERE description = ?
    List<Category> findByDescriptionIgnoreCaseContaining(String description);
}
