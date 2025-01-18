package com.grocerybooking.service.impl;

import com.grocerybooking.dto.GroceryItemDTO;
import com.grocerybooking.entity.GroceryItem;
import com.grocerybooking.exception.ResourceNotFoundException;
import com.grocerybooking.repository.GroceryItemRepository;
import com.grocerybooking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Override
    public GroceryItemDTO addGroceryItem(GroceryItemDTO groceryItemDTO) {
        GroceryItem groceryItem = mapToEntity(groceryItemDTO);
        GroceryItem savedItem = groceryItemRepository.save(groceryItem);
        return mapToDTO(savedItem);
    }

    @Override
    public List<GroceryItemDTO> getAllGroceryItems() {
        return groceryItemRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroceryItemDTO updateGroceryItem(Long id, GroceryItemDTO groceryItemDTO) {
        GroceryItem groceryItem = groceryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));

        groceryItem.setName(groceryItemDTO.getName());
        groceryItem.setPrice(groceryItemDTO.getPrice());
        groceryItem.setStock(groceryItemDTO.getStock());

        GroceryItem updatedItem = groceryItemRepository.save(groceryItem);
        return mapToDTO(updatedItem);
    }

    @Override
    public void deleteGroceryItem(Long id) {
        GroceryItem groceryItem = groceryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));
        groceryItemRepository.delete(groceryItem);
    }

    @Override
    public GroceryItemDTO updateInventory(Long id, Integer stock) {
        GroceryItem groceryItem = groceryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));


        groceryItem.setStock(stock);

        GroceryItem updatedItem = groceryItemRepository.save(groceryItem);
        return mapToDTO(updatedItem);
    }


    private GroceryItem mapToEntity(GroceryItemDTO dto) {
        GroceryItem entity = new GroceryItem();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        return entity;
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
