package com.example.caffe.api.controller;

import com.example.caffe.api.dto.order.OrderDto;
import com.example.caffe.api.service.orderheader.OrderHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/order")
public class OrderController {

    OrderHeaderService orderHeaderService;

    public OrderController(OrderHeaderService orderHeaderService) {
        this.orderHeaderService = orderHeaderService;
    }

    @PostMapping("/save")
    public void saveOrder (@Valid @RequestBody OrderDto orderDto){
        orderHeaderService.save(orderDto);
    }
}
