package com.example.caffe.api.dao.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "product_urgent", schema = "usersdb")
public class ProductUrgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private Integer price;

    @Column (name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Column (name = "minimum_quantity_in_stock", nullable = false)
    private Integer minimumQuantityInStock;

    @Column(nullable = false)
    private LocalDate date;
}
