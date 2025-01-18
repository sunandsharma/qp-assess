package com.grocerybooking.service.impl;

import com.grocerybooking.dto.GroceryItemDTO;
import com.grocerybooking.dto.OrderDTO;
import com.grocerybooking.dto.OrderItemDTO;
import com.grocerybooking.entity.GroceryItem;
import com.grocerybooking.entity.Order;
import com.grocerybooking.entity.OrderItem;
import com.grocerybooking.entity.User;
import com.grocerybooking.exception.ResourceNotFoundException;
import com.grocerybooking.repository.GroceryItemRepository;
import com.grocerybooking.repository.OrderRepository;
import com.grocerybooking.repository.UserRepository;
import com.grocerybooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {
        // Retrieve the user
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));

        // Process each order item
        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(itemDTO -> {
                    GroceryItem groceryItem = groceryItemRepository.findById(itemDTO.getGroceryItemId())
                            .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + itemDTO.getGroceryItemId()));

                    // Check stock availability
                    if (groceryItem.getStock() < itemDTO.getQuantity()) {
                        throw new IllegalArgumentException("Insufficient stock for item: " + groceryItem.getName());
                    }

                    // Deduct stock
                    groceryItem.setStock(groceryItem.getStock() - itemDTO.getQuantity());
                    groceryItemRepository.save(groceryItem);

                    // Create OrderItem
                    OrderItem orderItem = new OrderItem();
                    orderItem.setGroceryItem(groceryItem);
                    orderItem.setQuantity(itemDTO.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());

        // Calculate total price
        double totalPrice = orderItems.stream()
                .mapToDouble(item -> item.getGroceryItem().getPrice() * item.getQuantity())
                .sum();

        // Create and save the order
        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        orderItems.forEach(item -> item.setOrder(order)); // Link each order item to the order
        Order savedOrder = orderRepository.save(order);

        // Map to OrderDTO
        return mapToDTO(savedOrder);
    }

    private OrderDTO mapToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setItems(order.getItems().stream()
                .map(orderItem -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setGroceryItemId(orderItem.getGroceryItem().getId());
                    itemDTO.setQuantity(orderItem.getQuantity());
                    return itemDTO;
                })
                .collect(Collectors.toList()));
        return orderDTO;
    }

    @Override
    public List<GroceryItemDTO> getAvailableGroceryItems() {
        return groceryItemRepository.findAll().stream()
                .filter(item -> item.getStock() > 0) // Filter items with stock > 0
                .map(this::mapToDTO) // Map entities to DTOs
                .collect(Collectors.toList());
    }

    private GroceryItemDTO mapToDTO(GroceryItem entity) {
        GroceryItemDTO dto = new GroceryItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        return dto;
    }
}
