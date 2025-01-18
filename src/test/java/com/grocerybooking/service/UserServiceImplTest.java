package com.grocerybooking.service;

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
import com.grocerybooking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAvailableGroceryItems_ShouldReturnItemsInStock() {
        List<GroceryItem> groceryItems = Arrays.asList(
                new GroceryItem(1L, "Apple", 100.0, 50),
                new GroceryItem(2L, "Orange", 80.0, 0) // Out of stock
        );

        when(groceryItemRepository.findAll()).thenReturn(groceryItems);

        List<GroceryItemDTO> availableItems = userService.getAvailableGroceryItems();

        assertEquals(1, availableItems.size());
        assertEquals("Apple", availableItems.get(0).getName());
    }

    @Test
    void placeOrder_ShouldPlaceOrderSuccessfully() {
        User user = new User(1L, "John", "john@example.com", "USER");
        GroceryItem groceryItem = new GroceryItem(1L, "Apple", 100.0, 50);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setItems(Arrays.asList(new OrderItemDTO(1L, 2))); // 2 apples

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(groceryItem));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        OrderDTO placedOrder = userService.placeOrder(orderDTO);

        assertNotNull(placedOrder);
        verify(groceryItemRepository, times(1)).save(any(GroceryItem.class)); // Stock updated
        verify(orderRepository, times(1)).save(any(Order.class)); // Order saved
    }

    @Test
    void placeOrder_ShouldThrowException_WhenGroceryItemNotFound() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setItems(Arrays.asList(new OrderItemDTO(1L, 2))); // 2 apples

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(groceryItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.placeOrder(orderDTO));
    }

    @Test
    void placeOrder_ShouldThrowException_WhenInsufficientStock() {
        User user = new User(1L, "John", "john@example.com", "USER");
        GroceryItem groceryItem = new GroceryItem(1L, "Apple", 100.0, 1); // Insufficient stock

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setItems(Arrays.asList(new OrderItemDTO(1L, 2))); // 2 apples

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(groceryItem));

        assertThrows(IllegalArgumentException.class, () -> userService.placeOrder(orderDTO));
    }
}
