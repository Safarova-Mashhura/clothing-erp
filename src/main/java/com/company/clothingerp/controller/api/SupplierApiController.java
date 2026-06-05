package com.company.clothingerp.controller.api;

import com.company.clothingerp.model.Supplier;
import com.company.clothingerp.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/erp/suppliers")
@Tag(name = "Procurement & Suppliers (ERP)", description = "Endpoints for managing wholesale clothing vendors")
public class SupplierApiController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    @Operation(summary = "Get list of all suppliers")
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @PostMapping
    @Operation(summary = "Create or update a supplier profile")
    public Supplier saveSupplier(@RequestBody Supplier supplier) {
        return supplierService.saveSupplier(supplier);
    }
}
