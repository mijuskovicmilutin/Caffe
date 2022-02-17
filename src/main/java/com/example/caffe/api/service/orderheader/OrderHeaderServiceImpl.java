package com.example.caffe.api.service.orderheader;

import com.example.caffe.api.dto.order.OrderDto;
import com.example.caffe.api.dao.orderheader.OrderHeader;
import com.example.caffe.api.dao.orderitem.OrderItem;
import com.example.caffe.api.dao.user.User;
import com.example.caffe.api.dto.order.OrderItemDto;
import com.example.caffe.api.repository.orderheader.OrderHeaderRepo;
import com.example.caffe.api.repository.product.ProductRepo;
import com.example.caffe.api.repository.user.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
        try {
            User user = userRepo.findByUsername(username);
            if (checkQuantity(orderDto.getOrderItemDtoList())) {

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


                orderHeader.setOrders(list);
                orderHeader.setTotalPrice(totalPrice(orderDto));
                orderHeaderRepo.save(orderHeader);

                updateQuantityInStock(orderDto.getOrderItemDtoList());

            } else {
                log.error("Uneli ste veci broj artikala u Order nego sto je na stanju.");
                throw new IndexOutOfBoundsException("Uneli ste veci broj artikala u Order nego sto je na stanju.");
            }
        }catch (NoSuchElementException e){
            log.error("Korisnik sa unetim Username ne postoji");
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

    public Boolean checkQuantity (List<OrderItemDto> items) {

        try {
            return items.stream().anyMatch(item -> {
                String barcode = item.getBarcode();
                int quantity = item.getQuantity();
                return !(productRepo.findByBarcode(barcode).getQuantityInStock() - quantity < 0);
            });
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    public int totalPrice (OrderDto orderDto){
        int totalPrice = orderDto.getOrderItemDtoList().stream()
                .map(l -> productRepo.findByBarcode(l.getBarcode()).getPrice() * l.getQuantity()).mapToInt(Integer::intValue).sum();
        return totalPrice;
    }

    public void updateQuantityInStock (List<OrderItemDto> items) {
        items.stream().forEach(item -> {
            String barcode = item.getBarcode();
            int quantity = item.getQuantity();
            productRepo.updateQuantityInStock(productRepo.findByBarcode(barcode).getQuantityInStock() - quantity, barcode);
        });
    }
}