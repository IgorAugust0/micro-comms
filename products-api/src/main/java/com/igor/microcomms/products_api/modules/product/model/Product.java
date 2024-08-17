package com.igor.microcomms.products_api.modules.product.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.igor.microcomms.products_api.modules.category.model.Category;
import com.igor.microcomms.products_api.modules.product.dto.ProductRequest;
import com.igor.microcomms.products_api.modules.supplier.model.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String name;

    // @JoinColumn(name = "fk_supplier", referencedColumnName = "id", nullable =
    // false)
    @ManyToOne
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_supplier"))
    private Supplier supplier;

    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;

    // @JoinColumn(name = "fk_category", referencedColumnName = "id", nullable =
    // false)
    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_category"))
    private Category category;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

    public static Product of(ProductRequest request, Supplier supplier, Category category) {
        return Product
                .builder()
                .name(request.getName())
                .quantityAvailable(request.getQuantityAvailable())
                .supplier(supplier)
                .supplierId(request.getSupplierId())
                .category(category)
                .categoryId(request.getCategoryId())
                .build();
    }

    public void updateStock(Integer quantity) {
        this.quantityAvailable -= quantity;
    }
}
