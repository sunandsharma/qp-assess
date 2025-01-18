package com.grocerybooking.controller;

import com.grocerybooking.dto.GroceryItemDTO;
import com.grocerybooking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/items")
    public GroceryItemDTO addItem(@RequestBody GroceryItemDTO groceryItemDTO) {
        return adminService.addGroceryItem(groceryItemDTO);
    }

    @GetMapping("/items")
    public List<GroceryItemDTO> getAllItems() {
        return adminService.getAllGroceryItems();
    }

    @PutMapping("/items/{id}")
    public GroceryItemDTO updateItem(@PathVariable Long id, @RequestBody GroceryItemDTO groceryItemDTO) {
        return adminService.updateGroceryItem(id, groceryItemDTO);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id) {
        adminService.deleteGroceryItem(id);
    }

    @PutMapping("/items/{id}/inventory")
    public GroceryItemDTO updateInventory(@PathVariable Long id, @RequestParam Integer stock) {
        return adminService.updateInventory(id, stock);
    }
}
