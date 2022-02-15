package com.example.caffe.api.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ImportProductDto {

    private String barcode;

    private String date;

    private Integer quantity;
}
