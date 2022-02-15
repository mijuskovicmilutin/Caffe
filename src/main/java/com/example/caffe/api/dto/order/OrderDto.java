package com.example.caffe.api.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDto {

    // OrderHeader
    @NotNull
    private Integer table;
    @NotNull
    private Boolean paymentStatus;

    //OrderItem
    @NotNull
    private List<OrderItemDto> orderItemDtoList;

}
