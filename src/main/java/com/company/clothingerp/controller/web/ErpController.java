package com.company.clothingerp.controller.web;

import com.company.clothingerp.model.*;
import com.company.clothingerp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/erp")
public class ErpController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String showErp(Model model) {
        List<Order> sales = orderService.getOrdersByType("SALE");
        List<Order> purchases = orderService.getOrdersByType("PURCHASE");
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Employee> employees = employeeService.getAllEmployees();
        List<Customer> customers = customerService.getAllCustomers();

        // Financial summary
        double revenue = orderService.getTotalRevenue();
        double expenses = orderService.getTotalExpenses();
        double netProfit = revenue - expenses;
        double profitMargin = revenue > 0 ? (netProfit / revenue) * 100 : 0.0;

        model.addAttribute("sales", sales);
        model.addAttribute("purchases", purchases);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("employees", employees);
        model.addAttribute("customers", customers);
        model.addAttribute("products", productService.getAllProducts());

        model.addAttribute("revenue", revenue);
        model.addAttribute("expenses", expenses);
        model.addAttribute("netProfit", netProfit);
        model.addAttribute("profitMargin", profitMargin);

        model.addAttribute("newSupplier", new Supplier());

        return "erp";
    }

    @PostMapping("/supplier")
    public String addSupplier(@ModelAttribute Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return "redirect:/erp";
    }

    @PostMapping("/order")
    public String createOrder(@RequestParam String type,
                              @RequestParam(required = false) Long customerId,
                              @RequestParam(required = false) Long supplierId,
                              @RequestParam String orderNumber,
                              @RequestParam Long productId,
                              @RequestParam int quantity,
                              @RequestParam double price) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setType(type);
        order.setStatus("PENDING");

        if (type.equalsIgnoreCase("SALE") && customerId != null) {
            Customer cust = customerService.getCustomerById(customerId).orElse(null);
            order.setCustomer(cust);
        } else if (type.equalsIgnoreCase("PURCHASE") && supplierId != null) {
            Supplier supp = supplierService.getSupplierById(supplierId).orElse(null);
            order.setSupplier(supp);
        }

        Product prod = productService.getProductById(productId).orElse(null);
        if (prod != null) {
            OrderItem item = OrderItem.builder()
                    .product(prod)
                    .quantity(quantity)
                    .price(price)
                    .order(order)
                    .build();
            order.setOrderItems(new ArrayList<>(List.of(item)));
            orderService.createOrder(order);
        }

        return "redirect:/erp";
    }
}
