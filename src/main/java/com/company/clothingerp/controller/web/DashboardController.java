package com.company.clothingerp.controller.web;

import com.company.clothingerp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : "Guest";
        model.addAttribute("username", username);

        // Core KPIs
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("totalExpenses", orderService.getTotalExpenses());
        model.addAttribute("salesCount", orderService.getSalesCount());
        model.addAttribute("totalStock", inventoryService.getTotalStockCount());

        // Dynamic statistics
        model.addAttribute("productsCount", productService.getAllProducts().size());
        model.addAttribute("customersCount", customerService.getAllCustomers().size());
        model.addAttribute("suppliersCount", supplierService.getAllSuppliers().size());

        // Low stock items and warnings
        var lowStock = inventoryService.getLowStockItems();
        model.addAttribute("lowStockCount", lowStock.size());
        model.addAttribute("lowStockItems", lowStock);

        // Recent orders (latest transactions)
        var allOrders = orderService.getAllOrders();
        model.addAttribute("recentOrders", allOrders.subList(0, Math.min(5, allOrders.size())));

        return "dashboard";
    }
}
