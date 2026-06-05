package com.company.clothingerp.controller.api;

import com.company.clothingerp.model.Inventory;
import com.company.clothingerp.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wms/inventory")
@Tag(name = "Warehouse Management System (WMS)", description = "Endpoints for managing stock levels and warehouses")
public class InventoryApiController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Get all inventory item records across all warehouses")
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get list of items that are low in stock (quantity <= minimum threshold)")
    public List<Inventory> getLowStockItems() {
        return inventoryService.getLowStockItems();
    }

    @PostMapping("/adjust")
    @Operation(summary = "Manually adjust stock quantity of a product in a warehouse")
    public ResponseEntity<Void> adjustStock(@RequestParam Long productId,
                                            @RequestParam Long warehouseId,
                                            @RequestParam int deltaQuantity) {
        inventoryService.adjustStock(productId, warehouseId, deltaQuantity);
        return ResponseEntity.ok().build();
    }
}
