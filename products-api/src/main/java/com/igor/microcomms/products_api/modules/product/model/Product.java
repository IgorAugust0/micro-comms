package com.igor.microcomms.products_api.modules.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
//  @JoinColumn(name = "fk_supplier", referencedColumnName = "id", nullable = false)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_supplier"))
    private Supplier supplier;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @ManyToOne
//  @JoinColumn(name = "fk_category", referencedColumnName = "id", nullable = false)
    @JoinColumn(name = "category_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_category"))
    private Category category;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;
}
