package com.example.caffe.api.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateProductDto {

    private String name;
    private String type;
    private String barcode;
    private Integer price;
    private String oldBarcode;
}
