package com.example.caffe.api.service.product;

import com.example.caffe.api.dto.product.UpdateProductDto;
import com.example.caffe.api.dao.product.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    void save (Product product);
    void importProduct (String barcode, LocalDateTime dateTime, Integer quantity);
    void importProductManually (String barcode, Integer quantity);
    List<Product> getProductList ();
    List<Product> getProductListSorted (Boolean sortByName, Boolean sortByType, Boolean sortByBarcode, Boolean sortByPrice);
    List<Product> searchProductByName (String name);
    List<Product> findByType (String type);
    Product findByBarcode (String barcode);
    List<Product> findByPrice (Integer price1, Integer price2);
    List<Product> findAllProductsWhereQuantityIsLessThanMinimumQuantity();
    void updateProduct (UpdateProductDto productDto);
    void updateMinimumQuantityInStock (Integer quantity, String barcode);
    void deleteProduct (String barcode);
}
