package com.example.caffe.api.service.product;

import com.example.caffe.api.dto.product.UpdateProductDto;
import com.example.caffe.api.exception.ExceptionBadRequest;
import com.example.caffe.api.exception.ExceptionOk;
import com.example.caffe.api.dao.product.Product;
import com.example.caffe.api.dao.product.ProductImport;
import com.example.caffe.api.repository.product.ProductCustomRepo;
import com.example.caffe.api.repository.product.ProductImportRepo;
import com.example.caffe.api.repository.product.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductCustomRepo findAndUpdateProductRepo;
    private final ProductImportRepo productImportRepo;

    public ProductServiceImpl(ProductRepo productRepo, ProductCustomRepo findAndUpdateProductRepo, ProductImportRepo productImportRepo) {
        this.productRepo = productRepo;
        this.findAndUpdateProductRepo = findAndUpdateProductRepo;
        this.productImportRepo = productImportRepo;
    }

    @Override
    public void save (Product product) {
        Integer countByName = productRepo.countByName(product.getName());
        Integer countByBarcode = productRepo.countByBarcode(product.getBarcode());

        if (countByName == 0 && countByBarcode == 0){
            try {
                productRepo.save(product);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else {
            throw new ExceptionBadRequest("Order sa ovim nazivom ili barkodom vec postoji u bazi.");
        }
    }

    @Override
    public void importProduct (String barcode, LocalDateTime dateTime, Integer quantity) {
        productImportRepo.save(ProductImport.builder()
                .product(productRepo.findByBarcode(barcode))
                .dateTime(dateTime)
                .quantity(quantity)
                .build());
        int newQuantity = productRepo.findByBarcode(barcode).getQuantityInStock() + quantity;
        productRepo.updateQuantityInStock(newQuantity, barcode);
    }

    @Override
    public void importProductManually(String barcode, Integer quantity) {
        productRepo.updateQuantityInStock(quantity, barcode);
    }

    @Override
    public List<Product> getProductList() {
        // nesortirana lista
        return productRepo.findAll();
    }

    @Override
    public List<Product> getProductListSorted (Boolean sortByName, Boolean sortByType, Boolean sortByBarcode, Boolean sortByPrice) {
        // nesortirana lista
        List<Product> list = productRepo.findAll();
        // sortirana lista produkata po nazivu
        List<Product> listByName = list.stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
        if (sortByName){
            return listByName;
        }else if (sortByType){ // sortirana lista produkata po tipu
            return listByName.stream().sorted(Comparator.comparing(Product::getType)).collect(Collectors.toList());
        }else if (sortByBarcode){ // sortirana lista produkata po barkodu
            return listByName.stream().sorted(Comparator.comparing(Product::getBarcode)).collect(Collectors.toList());
        }else if (sortByPrice){ // sortirana lista produkata po ceni
            return listByName.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        }else {
            throw new ExceptionOk("Greska u servisu");
        }
    }

    @Override
    public List<Product> searchProductByName (String name){
        return productRepo.searchByName(name).stream()
                .sorted(Comparator.comparing(Product::getType).thenComparing(Product::getName)).collect(Collectors.toList());
    }

    @Override
    public List<Product> findByType (String type){
        return productRepo.findAllByType(type).stream()
                .sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
    }

    @Override
    public Product findByBarcode (String barcode){
        return productRepo.findByBarcode(barcode);
    }

    @Override
    public List<Product> findByPrice (Integer price1, Integer price2){
        return productRepo.findByPriceBetween(price1, price2);
    }

    @Override
    public List<Product> findAllProductsWhereQuantityIsLessThanMinimumQuantity() {
        return productRepo.findAllProductsWhereQuantityIsLessThanMinimumQuantity();
    }

    @Override
    public void updateProduct(UpdateProductDto productDto) {
        try {
            findAndUpdateProductRepo.updateProduct(productDto.getName(),
                    productDto.getType(),
                    productDto.getBarcode(),
                    productDto.getPrice(),
                    productDto.getOldBarcode());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateMinimumQuantityInStock(Integer quantity, String barcode) {
        productRepo.updateMinimumQuantityInStock(quantity, barcode);
    }

    @Override
    public void deleteProduct(String barcode) {
        Integer countByBarcode = productRepo.countByBarcode(barcode);

        if (countByBarcode >= 1) {
            try {
                productRepo.deleteProductByBarcode(barcode);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new ExceptionBadRequest("Order sa ovim barkodom ne postoji u bazi.");
        }
    }

}
