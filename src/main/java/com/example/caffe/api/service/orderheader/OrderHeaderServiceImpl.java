package com.example.caffe.api.service.orderheader;

import com.example.caffe.api.dto.order.OrderDto;
import com.example.caffe.api.dao.orderheader.OrderHeader;
import com.example.caffe.api.dao.orderitem.OrderItem;
import com.example.caffe.api.dao.user.User;
import com.example.caffe.api.dto.order.OrderItemDto;
import com.example.caffe.api.exception.ExceptionBadRequest;
import com.example.caffe.api.repository.orderheader.OrderHeaderRepo;
import com.example.caffe.api.repository.product.ProductRepo;
import com.example.caffe.api.repository.user.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderHeaderServiceImpl implements OrderHeaderService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderHeaderRepo orderHeaderRepo;

    public OrderHeaderServiceImpl(UserRepo userRepo, ProductRepo productRepo, OrderHeaderRepo orderHeaderRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderHeaderRepo = orderHeaderRepo;
    }

    private class ProductsWhereQuantityIsNotOk {
        private String barcode;
        private int quantity;

        public ProductsWhereQuantityIsNotOk(String barcode, int quantity) {
            this.barcode = barcode;
            this.quantity = quantity;
        }
    }

    @Override
    public void save(OrderDto orderDto) {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByUsername(username);

        if (check(orderDto.getOrderItemDtoList())) {

            OrderHeader orderHeader = OrderHeader.builder()
                    .user(user)
                    .table(orderDto.getTable())
                    .time(LocalDateTime.now())
                    .paymentStatus(orderDto.getPaymentStatus())
                    .build();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderHeader(orderHeader);

            //--------------------------------------------------------------------------------------------------------------
            // prvi nacin automatskog popunjavanja child tabele kroz parent - pomocu java.stream
            List<OrderItem> list = orderDto.getOrderItemDtoList().stream().map(l -> OrderItem.builder()
                    .quantity(l.getQuantity())
                    .product(productRepo.findByBarcode(l.getBarcode()))
                    .orderHeader(orderHeader)
                    .build()).collect(Collectors.toList());

            int totalPrice = orderDto.getOrderItemDtoList().stream()
                    .map(l -> productRepo.findByBarcode(l.getBarcode()).getPrice() * l.getQuantity()).mapToInt(Integer::intValue).sum();

            orderHeader.setOrders(list);
            orderHeader.setTotalPrice(totalPrice);
            orderHeaderRepo.save(orderHeader);

            for (int i = 0; i < orderDto.getOrderItemDtoList().size(); i++) {

                String barcode = orderDto.getOrderItemDtoList().get(i).getBarcode();
                int quantity = orderDto.getOrderItemDtoList().get(i).getQuantity();

                productRepo.updateQuantityInStock(productRepo.findByBarcode(barcode).getQuantityInStock() - quantity, barcode);
            }
        } else {
            throw new ExceptionBadRequest("Uneli ste veci broj artikala u Order nego sto je na stanju.");
        }
        //--------------------------------------------------------------------------------------------------------------
        // drugi nacin automatskog popunjavanja child tabele kroz parent - pomocu for petlje

        /*List<OrderItem> lista = new ArrayList<>();
        int price;
        int totalPrice = 0;

        for (int i = 0; i < orderDto.getOrderItemDtoList().size(); i++) {

            orderItem = OrderItem.builder()
                            .quantity(orderDto.getOrderItemDtoList().get(i).getQuantity())
                            .product(productRepo.findByBarcode(orderDto.getOrderItemDtoList().get(i).getBarcode()))
                            .orderHeader(orderHeader)
                            .build();

            lista.add(i,orderItem);

            price = productRepo.findByBarcode(orderDto.getOrderItemDtoList().get(i).getBarcode()).getPrice() *
                    orderDto.getOrderItemDtoList().get(i).getQuantity();
            totalPrice1 = totalPrice1 + price;
        }
        orderHeader.setOrders(lista);
        orderHeader.setTotalPrice(totalPrice1);
        orderHeaderRepo.save(orderHeader);*/
    }

    public boolean check(List<OrderItemDto> items) {


        return items.stream().anyMatch(item -> {
            String barcode = item.getBarcode();
            int quantity = item.getQuantity();
            return !(productRepo.findByBarcode(barcode).getQuantityInStock() - quantity < 0);
        });
    }
}