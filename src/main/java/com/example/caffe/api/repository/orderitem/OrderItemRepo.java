package com.example.caffe.api.repository.orderitem;

import com.example.caffe.api.dao.orderitem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
}
