package com.example.caffe.api.repository.product;

import com.example.caffe.api.dao.product.ProductUrgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUrgentRepo extends JpaRepository<ProductUrgent, Long> {
}
