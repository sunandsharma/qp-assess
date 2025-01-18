package com.grocerybooking.service;

import com.grocerybooking.dto.GroceryItemDTO;
import com.grocerybooking.dto.OrderDTO;

import java.util.List;

public interface UserService {
    OrderDTO placeOrder(OrderDTO orderDTO);

    List<GroceryItemDTO> getAvailableGroceryItems();
}
