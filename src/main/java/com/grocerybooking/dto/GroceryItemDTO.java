package com.grocerybooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryItemDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
}
