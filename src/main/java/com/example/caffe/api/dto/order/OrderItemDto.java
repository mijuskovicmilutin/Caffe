package com.example.caffe.api.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class OrderItemDto {

    @NotNull
    private String barcode;
    @NotNull
    private Integer quantity;
}
