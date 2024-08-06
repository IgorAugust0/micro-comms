package com.igor.microcomms.products_api.modules.product.model;

import com.igor.microcomms.products_api.modules.category.model.Category;
import com.igor.microcomms.products_api.modules.supplier.model.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // use IDENTITY if you don't want to manually increment the ID
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
//  @JoinColumn(name = "fk_supplier", referencedColumnName = "id", nullable = false)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_supplier"))
    private Supplier supplier;

    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;

    @ManyToOne
//  @JoinColumn(name = "fk_category", referencedColumnName = "id", nullable = false)
    @JoinColumn(name = "category_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_category"))
    private Category category;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;
}
