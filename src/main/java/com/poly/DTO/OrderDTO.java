package com.poly.DTO;

import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

    private Integer orderID;
    private String status;
    private String shippingAddress;
    private String orderDateFormatted;
    private String orderDate;
    private UserDTO user;
    private List<OrderItemDTO> orderItems;

    // getters/setters
}
