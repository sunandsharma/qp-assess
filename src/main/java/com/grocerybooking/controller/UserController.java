package com.grocerybooking.controller;

import com.grocerybooking.dto.GroceryItemDTO;
import com.grocerybooking.dto.OrderDTO;
import com.grocerybooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/orders")
    public OrderDTO placeOrder(@RequestBody OrderDTO orderDTO) {
        return userService.placeOrder(orderDTO);
    }

    @GetMapping("/items")
    public List<GroceryItemDTO> getAvailableItems() {
        return userService.getAvailableGroceryItems();
    }
}
