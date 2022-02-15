package com.example.caffe.api.repository.product;

import com.example.caffe.api.dao.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("select count(o) from Product o where o.name = ?1")
    Integer countByName (String name);

    @Query("select count(p) from Product p where p.barcode = ?1")
    Integer countByBarcode (String barcode);

    void deleteProductByBarcode (String barcode);

    Product findByBarcode (String barcode);

    @Query("select p from Product p where p.name like CONCAT('%', ?1, '%')")
    List<Product> searchByName (String name);

    @Query("select p from Product p where p.type = ?1")
    List<Product> findAllByType (String type);

    @Query("select p from Product p where p.price between ?1 and ?2")
    List<Product> findByPriceBetween (Integer price1, Integer price2);

    @Modifying
    @Query (value = "UPDATE Product p SET p.quantityInStock = ?1 WHERE p.barcode = ?2")
    void updateQuantityInStock (Integer quantity, String barcode);

    @Modifying
    @Query (value = "UPDATE Product p SET p.minimumQuantityInStock = ?1 WHERE p.barcode = ?2")
    void updateMinimumQuantityInStock (Integer quantity, String barcode);

    @Query (value = "SELECT p FROM Product p WHERE p.quantityInStock < p.minimumQuantityInStock")
    List<Product> findAllProductsWhereQuantityIsLessThanMinimumQuantity();
}
