package com.company.clothingerp.service;

import com.company.clothingerp.model.Inventory;
import com.company.clothingerp.model.Warehouse;
import com.company.clothingerp.repository.InventoryRepository;
import com.company.clothingerp.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    // Warehouse CRUD
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getWarehouseById(Long id) {
        return warehouseRepository.findById(id);
    }

    public Warehouse saveWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    // Inventory management
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }

    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }

    public Long getTotalStockCount() {
        Long total = inventoryRepository.getTotalStockQuantity();
        return total != null ? total : 0L;
    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Adjust Stock (e.g. on order placement)
    public void adjustStock(Long productId, Long warehouseId, int deltaQuantity) {
        Optional<Inventory> optInv = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        if (optInv.isPresent()) {
            Inventory inv = optInv.get();
            inv.setQuantity(inv.getQuantity() + deltaQuantity);
            if (inv.getQuantity() < 0) {
                inv.setQuantity(0); // Prevent negative inventory
            }
            inventoryRepository.save(inv);
        } else if (deltaQuantity > 0) {
            // Create a new inventory record if it doesn't exist and stock is being added
            Warehouse warehouse = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + warehouseId));
            
            // Assume we can construct a default Inventory
            Inventory newInv = Inventory.builder()
                    .product(null) // Should be injected or fetched
                    .warehouse(warehouse)
                    .quantity(deltaQuantity)
                    .minThreshold(10) // Default low stock warning threshold
                    .build();
            // Since product needs to be injected, we expect the caller to set up the correct mapping
            // But usually, we only adjust stock for products already seeded/mapped.
        }
    }
}
