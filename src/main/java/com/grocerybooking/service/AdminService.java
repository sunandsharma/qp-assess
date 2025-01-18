package com.grocerybooking.service;

import com.grocerybooking.dto.GroceryItemDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AdminService {
    GroceryItemDTO addGroceryItem(GroceryItemDTO groceryItemDTO);
    List<GroceryItemDTO> getAllGroceryItems();
    GroceryItemDTO updateGroceryItem(Long id, GroceryItemDTO groceryItemDTO);
    void deleteGroceryItem(Long id);
    GroceryItemDTO updateInventory(Long id, Integer stock);
}
