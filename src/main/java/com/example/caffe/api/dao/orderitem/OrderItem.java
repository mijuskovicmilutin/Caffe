package com.example.caffe.api.dao.orderitem;

import com.example.caffe.api.dao.product.Product;
import com.example.caffe.api.dao.orderheader.OrderHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "order_item", schema = "usersdb")
public class OrderItem  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    @JsonIgnore
    private Long orderId;

    @Column(nullable = false)
    private Integer quantity;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "product_id",
            nullable = false
    )
    private Product product;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private OrderHeader orderHeader;

}
