package com.example.caffe.api.repository.product;

import com.example.caffe.api.dao.product.ProductImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImportRepo extends JpaRepository<ProductImport, Long> {
}
