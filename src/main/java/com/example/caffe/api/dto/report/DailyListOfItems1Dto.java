package com.example.caffe.api.dto.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DailyListOfItems1Dto {

    private String productName;
    private Integer quantitybyProduct;
    private Integer priceByProduct;
}
