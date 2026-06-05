package com.company.clothingerp.controller.web;

import com.company.clothingerp.model.*;
import com.company.clothingerp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wms")
public class WmsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String showWms(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("inventories", inventoryService.getAllInventory());
        model.addAttribute("warehouses", inventoryService.getAllWarehouses());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("lowStockItems", inventoryService.getLowStockItems());
        model.addAttribute("newProduct", new Product());
        model.addAttribute("newCategory", new Category());
        model.addAttribute("newWarehouse", new Warehouse());
        return "wms";
    }

    @PostMapping("/product")
    public String addProduct(@ModelAttribute Product product, @RequestParam Long categoryId) {
        Category cat = productService.getCategoryById(categoryId).orElse(null);
        product.setCategory(cat);
        productService.saveProduct(product);
        return "redirect:/wms";
    }

    @PostMapping("/category")
    public String addCategory(@ModelAttribute Category category) {
        productService.saveCategory(category);
        return "redirect:/wms";
    }

    @PostMapping("/warehouse")
    public String addWarehouse(@ModelAttribute Warehouse warehouse) {
        inventoryService.saveWarehouse(warehouse);
        return "redirect:/wms";
    }

    @PostMapping("/stock/adjust")
    public String adjustStock(@RequestParam Long productId, @RequestParam Long warehouseId, @RequestParam int delta) {
        inventoryService.adjustStock(productId, warehouseId, delta);
        return "redirect:/wms";
    }
}
