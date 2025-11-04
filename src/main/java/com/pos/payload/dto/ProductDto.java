package com.pos.payload.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private String sku;

    private Long storeId;

    private Long categoryId;

    private String description;

    private Double mrp;

    private Double sellingPrice;

    private String brand;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
