package com.example.caffe.api.controller;

import com.example.caffe.api.dto.product.ImportProductDto;
import com.example.caffe.api.dto.product.UpdateProductDto;
import com.example.caffe.api.dao.product.Product;
import com.example.caffe.api.service.product.ProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/save")
    public void saveProduct (@Valid @RequestBody Product product){
        productService.save(product);
    }

    @PostMapping("/admin/save/product")
    public void importProduct (@Valid @RequestBody ImportProductDto importProductDto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(importProductDto.getDate(), formatter);
        productService.importProduct(importProductDto.getBarcode(), localDate.atTime(9,00), importProductDto.getQuantity());
    }

    @PostMapping("/admin/save/product/manually")
    public void importProductManually (@Valid @RequestBody ImportProductDto importProductDto){
        productService.importProductManually(importProductDto.getBarcode(), importProductDto.getQuantity());
    }

    @GetMapping("/get")
    public List<Product> getProductList (){
        return productService.getProductList();
    }

    @GetMapping("/get/sorted")
    public List<Product> getProductListSorted (@RequestParam Boolean sortByName,
                                               @RequestParam Boolean sortByType,
                                               @RequestParam Boolean sortByBarcode,
                                               @RequestParam Boolean sortByPrice){
        return productService.getProductListSorted(sortByName, sortByType, sortByBarcode, sortByPrice);
    }

    @GetMapping("/get/name")
    public List<Product> searchProductByName (@RequestParam("name") String name){
        return productService.searchProductByName(name);
    }

    @GetMapping("/get/type")
    public List<Product> findByType (@RequestParam("type") String type){
        return productService.findByType(type);
    }

    @GetMapping("/get/barcode")
    public Product findByBarcode (@RequestParam("barcode") String barcode){
        return productService.findByBarcode(barcode);
    }

    @GetMapping("/get/price")
    public List<Product> findByPrice (@RequestParam("price1") Integer price1, @RequestParam("price2") Integer price2){
        return productService.findByPrice(price1, price2);
    }

    @GetMapping("/get/producturgent")
    public List<Product> findAllProductsWhereQuantityIsLessThanMinimumQuantity(){
        return productService.findAllProductsWhereQuantityIsLessThanMinimumQuantity();
    }

    @PutMapping("/admin/update")
    public void updateProduct (@Valid @RequestBody UpdateProductDto productDto){
        productService.updateProduct(productDto);
    }

    @PutMapping("/admin/update/minimumquantity")
    public void updateMinimumQuantityInStock (@Valid @RequestBody ImportProductDto importProductDto){
        productService.updateMinimumQuantityInStock(importProductDto.getQuantity(), importProductDto.getBarcode());
    }

    @DeleteMapping("/admin/delete")
    public void deleteProduct (@Valid @RequestParam String barcode){
        productService.deleteProduct(barcode);
    }
}
