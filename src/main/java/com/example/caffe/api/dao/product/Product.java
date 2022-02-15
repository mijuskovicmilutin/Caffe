package com.example.caffe.api.dao.product;

import com.example.caffe.api.dao.orderitem.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "products", schema = "usersdb")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @JsonIgnore
    private Long productId;

    @Column(nullable = false)
    @NotEmpty
    private String name;

    @Column(nullable = false)
    @NotEmpty
    private String type;

    @Column(nullable = false)
    @NotEmpty
    private String barcode;

    @Column(nullable = false)
    @Min(1)
    private Integer price;

    /*@OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "product"
    )
    @JsonIgnore
    private OrderItem orderItem;*/

    @OneToMany (
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "product"
    )
    @JsonIgnore
    private List<ProductImport> productImports;

    @Column (name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Column (name = "minimum_quantity_in_stock", nullable = false)
    private Integer minimumQuantityInStock;
}
