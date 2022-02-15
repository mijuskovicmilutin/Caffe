package com.example.caffe.api.dao.orderheader;

import com.example.caffe.api.dao.orderitem.OrderItem;
import com.example.caffe.api.dao.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table (name = "order_header", schema = "usersdb")
public class OrderHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_header_id")
    @JsonIgnore
    private Long orderHeaderId;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false
    )
    private User user;

    private Integer table;

    private LocalDateTime time;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "payment_status",nullable = false)
    private Boolean paymentStatus;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "orderHeader"
    )
    private List<OrderItem> orders;
}
