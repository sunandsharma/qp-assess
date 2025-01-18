package com.grocerybooking.service;

import com.grocerybooking.dto.GroceryItemDTO;
import com.grocerybooking.entity.GroceryItem;
import com.grocerybooking.exception.ResourceNotFoundException;
import com.grocerybooking.repository.GroceryItemRepository;
import com.grocerybooking.service.impl.AdminServiceImpl;
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

class AdminServiceImplTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addGroceryItem_ShouldSaveItem() {
        GroceryItem groceryItem = new GroceryItem(1L, "Apple", 100.0, 50);
        GroceryItemDTO groceryItemDTO = new GroceryItemDTO(1L, "Apple", 100.0, 50);

        when(groceryItemRepository.save(any(GroceryItem.class))).thenReturn(groceryItem);

        GroceryItemDTO savedItem = adminService.addGroceryItem(groceryItemDTO);

        assertNotNull(savedItem);
        assertEquals("Apple", savedItem.getName());
        assertEquals(100.0, savedItem.getPrice());
    }

    @Test
    void getAllGroceryItems_ShouldReturnAllItems() {
        List<GroceryItem> groceryItems = Arrays.asList(
                new GroceryItem(1L, "Apple", 100.0, 50),
                new GroceryItem(2L, "Orange", 80.0, 30)
        );

        when(groceryItemRepository.findAll()).thenReturn(groceryItems);

        List<GroceryItemDTO> items = adminService.getAllGroceryItems();

        assertEquals(2, items.size());
        assertEquals("Apple", items.get(0).getName());
        assertEquals("Orange", items.get(1).getName());
    }

    @Test
    void updateGroceryItem_ShouldUpdateItem() {
        GroceryItem groceryItem = new GroceryItem(1L, "Apple", 100.0, 50);
        GroceryItemDTO updatedDTO = new GroceryItemDTO(1L, "Apple Updated", 120.0, 60);

        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(groceryItem));
        when(groceryItemRepository.save(any(GroceryItem.class))).thenReturn(groceryItem);

        GroceryItemDTO updatedItem = adminService.updateGroceryItem(1L, updatedDTO);

        assertNotNull(updatedItem);
        assertEquals("Apple Updated", updatedItem.getName());
        assertEquals(120.0, updatedItem.getPrice());
    }

    @Test
    void deleteGroceryItem_ShouldDeleteItem() {
        GroceryItem groceryItem = new GroceryItem(1L, "Apple", 100.0, 50);

        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(groceryItem));
        doNothing().when(groceryItemRepository).delete(groceryItem);

        assertDoesNotThrow(() -> adminService.deleteGroceryItem(1L));
        verify(groceryItemRepository, times(1)).delete(groceryItem);
    }

    @Test
    void deleteGroceryItem_ShouldThrowException_WhenItemNotFound() {
        when(groceryItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteGroceryItem(1L));
    }

    @Test
    public void testUpdateInventory_Success() {
        GroceryItem groceryItem = new GroceryItem(1L, "Apple", 100.0, 50);

        when(groceryItemRepository.findById(1L)).thenReturn(Optional.of(groceryItem));
        when(groceryItemRepository.save(any(GroceryItem.class))).thenReturn(groceryItem);


        GroceryItemDTO result = adminService.updateInventory(1L, 20);


        assertNotNull(result);
        assertEquals(20, result.getStock());  // Make sure the stock is updated correctly
        assertEquals("Apple", result.getName()); // Verify that the name remains unchanged


        verify(groceryItemRepository, times(1)).findById(1L);
        verify(groceryItemRepository, times(1)).save(groceryItem);
    }
}
