package com.example.caffe.api.repository.product;

public interface ProductCustomRepo {

    void updateProduct (String name, String type, String barcode, Integer price, String oldBarcode);
}
