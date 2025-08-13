package com.poly.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDTO {

    private int quantity;
    private BigDecimal price;
    private ProductDTO product;
}
