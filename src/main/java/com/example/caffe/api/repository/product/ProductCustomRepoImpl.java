package com.example.caffe.api.repository.product;

import com.example.caffe.api.dao.product.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;

@Repository
public class ProductCustomRepoImpl implements ProductCustomRepo {

    @PersistenceContext
    public EntityManager em;

    private final ProductRepo productRepo;

    public ProductCustomRepoImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public void updateProduct (String name, String type, String barcode, Integer price, String oldBarcode){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Product> cu = cb.createCriteriaUpdate(Product.class);
        Root<Product> product = cu.from(Product.class);
        if (name != null){
            cu.where(cb.equal(product.get("barcode"), oldBarcode));
            cu.set(product.get("name"), name);
        }else if (type != null){
            cu.where(cb.equal(product.get("barcode"), oldBarcode));
            cu.set(product.get("type"), type);
        }else if (barcode != null){
            cu.where(cb.equal(product.get("barcode"), oldBarcode));
            cu.set(product.get("barcode"), barcode);
        }else if (price != null){
            cu.where(cb.equal(product.get("barcode"), oldBarcode));
            cu.set(product.get("price"), price);
        }

        Query query = em.createQuery(cu);
        query.executeUpdate();
    }
}
