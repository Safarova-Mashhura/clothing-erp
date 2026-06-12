package com.company.clothingerp.config;

import com.company.clothingerp.model.*;
import com.company.clothingerp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() > 0) {
            return; // DB already seeded
        }

        System.out.println("--- Seeding Database with Corporate Datasets ---");

        // 1. Seed Roles
        Role adminRole = roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
        Role managerRole = roleRepository.save(Role.builder().name(RoleName.ROLE_MANAGER).build());
        Role employeeRole = roleRepository.save(Role.builder().name(RoleName.ROLE_EMPLOYEE).build());

        // 2. Seed Users
        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@company.com")
                .active(true)
                .roles(new HashSet<>(Arrays.asList(adminRole)))
                .build();
        userRepository.save(adminUser);

        User managerUser = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("manager123"))
                .email("manager@company.com")
                .active(true)
                .roles(new HashSet<>(Arrays.asList(managerRole)))
                .build();
        userRepository.save(managerUser);

        User employeeUser = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("employee123"))
                .email("employee@company.com")
                .active(true)
                .roles(new HashSet<>(Arrays.asList(employeeRole)))
                .build();
        userRepository.save(employeeUser);

        // 3. Seed Employees
        employeeRepository.save(Employee.builder()
                .firstName("Alisher")
                .lastName("Ubaydullaev")
                .email("admin@company.com")
                .phone("+998 90 123 45 67")
                .role("Administrator / Architect")
                .salary(5000.0)
                .user(adminUser)
                .build());

        employeeRepository.save(Employee.builder()
                .firstName("Dilshod")
                .lastName("Rustamov")
                .email("manager@company.com")
                .phone("+998 93 321 65 43")
                .role("Sales & Procurement Manager")
                .salary(3000.0)
                .user(managerUser)
                .build());

        employeeRepository.save(Employee.builder()
                .firstName("Zilola")
                .lastName("Karimova")
                .email("employee@company.com")
                .phone("+998 99 987 65 43")
                .role("Warehouse Coordinator")
                .salary(1500.0)
                .user(employeeUser)
                .build());

        // 4. Seed Categories
        Category menswear = categoryRepository.save(Category.builder().name("Menswear").description("Men's fashion items, trousers, shirts").build());
        Category womenswear = categoryRepository.save(Category.builder().name("Womenswear").description("Women's dresses, blouses, skirts").build());
        Category outerwear = categoryRepository.save(Category.builder().name("Outerwear").description("Winter jackets, coats, trench coats").build());
        Category accessories = categoryRepository.save(Category.builder().name("Accessories").description("Belts, ties, socks, bags").build());

        // 5. Seed Products
        Product product1 = productRepository.save(Product.builder()
                .sku("OUT-DEN-01")
                .name("Turkish Denim Jacket")
                .description("High-quality Turkish cotton denim jacket with copper buttons")
                .price(60.0)
                .cost(25.0)
                .category(outerwear)
                .size("M")
                .color("Blue")
                .build());

        Product product2 = productRepository.save(Product.builder()
                .sku("MEN-JEAN-02")
                .name("Slim Fit Black Jeans")
                .description("Comfortable stretch denim slim-fit black jeans")
                .price(35.0)
                .cost(15.0)
                .category(menswear)
                .size("L")
                .color("Black")
                .build());

        Product product3 = productRepository.save(Product.builder()
                .sku("WOM-DRES-03")
                .name("Floral Summer Dress")
                .description("Chiffon floral summer dress with lightweight lining")
                .price(45.0)
                .cost(18.0)
                .category(womenswear)
                .size("S")
                .color("Red/Floral")
                .build());

        Product product4 = productRepository.save(Product.builder()
                .sku("MEN-POLO-04")
                .name("Classic Pique Polo")
                .description("100% combed cotton pique polo shirt")
                .price(22.0)
                .cost(8.0)
                .category(menswear)
                .size("XL")
                .color("White")
                .build());

        Product product5 = productRepository.save(Product.builder()
                .sku("ACC-BELT-05")
                .name("Genuine Leather Belt")
                .description("Full-grain leather wholesale belt with silver buckle")
                .price(15.0)
                .cost(5.0)
                .category(accessories)
                .size("36")
                .color("Brown")
                .build());

        // 6. Seed Warehouses
        Warehouse warehouse1 = warehouseRepository.save(Warehouse.builder()
                .name("Central Tashkent Terminal (W1)")
                .location("Yunusabad District, Tashkent")
                .capacity(10000)
                .build());

        Warehouse warehouse2 = warehouseRepository.save(Warehouse.builder()
                .name("Jizzax Logistics Hub (W2)")
                .location("Jizzax Ring Road")
                .capacity(5000)
                .build());

        // 7. Seed Inventory (Stock Levels & Alerts)
        inventoryRepository.save(Inventory.builder().product(product1).warehouse(warehouse1).quantity(450).minThreshold(30).build());
        inventoryRepository.save(Inventory.builder().product(product1).warehouse(warehouse2).quantity(120).minThreshold(20).build());

        // Seed product 2 with low stock in both warehouses
        inventoryRepository.save(Inventory.builder().product(product2).warehouse(warehouse1).quantity(8).minThreshold(15).build()); // Low stock!
        inventoryRepository.save(Inventory.builder().product(product2).warehouse(warehouse2).quantity(5).minThreshold(10).build());  // Low stock!

        // Seed product 3
        inventoryRepository.save(Inventory.builder().product(product3).warehouse(warehouse1).quantity(200).minThreshold(25).build());
        inventoryRepository.save(Inventory.builder().product(product3).warehouse(warehouse2).quantity(6).minThreshold(15).build());  // Low stock in rkand!

        // Seed product 4
        inventoryRepository.save(Inventory.builder().product(product4).warehouse(warehouse1).quantity(800).minThreshold(50).build());

        // Seed product 5
        inventoryRepository.save(Inventory.builder().product(product5).warehouse(warehouse1).quantity(3).minThreshold(10).build());  // Low stock!

        // 8. Seed CRM Customers
        Customer customer1 = customerRepository.save(Customer.builder()
                .companyName("Nike Wholesale Uzbekistan")
                .contactName("Sherzod Alimov")
                .email("sherzod@zarawholesale.uz")
                .phone("+998 90 900 11 22")
                .address("A.Temur Ave 102")
                .city("Tashkent")
                .country("Uzbekistan")
                .status("ACTIVE")
                .build());

        Customer customer2 = customerRepository.save(Customer.builder()
                .companyName("Xorazm Fashion LLC")
                .contactName("Madina Karimova")
                .email("madina@samfashion.uz")
                .phone("+998 66 233 44 55")
                .address("Registan Street 12")
                .city("rkand")
                .country("Uzbekistan")
                .status("ACTIVE")
                .build());

        Customer customer3 = customerRepository.save(Customer.builder()
                .companyName("Bukhara Bazaar Outlet")
                .contactName("Jasur Rahimov")
                .email("jasur@bukharaoutlet.com")
                .phone("+998 97 765 43 21")
                .address("B.Naqshband St 56")
                .city("Bukhara")
                .country("Uzbekistan")
                .status("ACTIVE")
                .build());

        // 9. Seed Suppliers
        Supplier supplier1 = supplierRepository.save(Supplier.builder()
                .companyName("Istanbul Textile Exporters")
                .contactName("Ahmet Yilmaz")
                .email("ahmet@istanbultextile.com")
                .phone("+90 212 555 1234")
                .address("Merter district, Istanbul")
                .build());

        Supplier supplier2 = supplierRepository.save(Supplier.builder()
                .companyName("Chilonzor Garments OJSC")
                .contactName("Rustam Jalilov")
                .email("info@chilonzor-garments.uz")
                .phone("+998 71 277 88 99")
                .address("Chilonzor 24, Tashkent")
                .build());

        // 10. Seed Orders (Sales and Procurement History)
        Order order1 = Order.builder()
                .orderNumber("ORD-2026-001")
                .customer(customer1)
                .orderDate(LocalDateTime.now().minusDays(10))
                .status("DELIVERED")
                .type("SALE")
                .processedBy(managerUser)
                .build();
        order1.setOrderItems(Arrays.asList(
                OrderItem.builder().order(order1).product(product1).quantity(100).price(60.0).build(),
                OrderItem.builder().order(order1).product(product2).quantity(50).price(35.0).build()
        ));
        order1.setTotalAmount(100 * 60.0 + 50 * 35.0); // 7750.0
        orderRepository.save(order1);

        Order order2 = Order.builder()
                .orderNumber("ORD-2026-002")
                .customer(customer2)
                .orderDate(LocalDateTime.now().minusDays(5))
                .status("SHIPPED")
                .type("SALE")
                .processedBy(managerUser)
                .build();
        order2.setOrderItems(Arrays.asList(
                OrderItem.builder().order(order2).product(product3).quantity(40).price(45.0).build(),
                OrderItem.builder().order(order2).product(product4).quantity(100).price(22.0).build()
        ));
        order2.setTotalAmount(40 * 45.0 + 100 * 22.0); // 4000.0
        orderRepository.save(order2);

        // Procurement order (Purchase)
        Order purchaseOrder = Order.builder()
                .orderNumber("PUR-2026-001")
                .supplier(supplier1)
                .orderDate(LocalDateTime.now().minusDays(15))
                .status("DELIVERED")
                .type("PURCHASE")
                .processedBy(adminUser)
                .build();
        purchaseOrder.setOrderItems(Arrays.asList(
                OrderItem.builder().order(purchaseOrder).product(product1).quantity(500).price(25.0).build(),
                OrderItem.builder().order(purchaseOrder).product(product2).quantity(200).price(15.0).build()
        ));
        purchaseOrder.setTotalAmount(500 * 25.0 + 200 * 15.0); // 15500.0
        orderRepository.save(purchaseOrder);

        // 11. Log Seeding
        systemLogRepository.save(SystemLog.builder()
                .action("DATABASE_INITIALIZED")
                .details("System schema initialized and corporate datasets seeded by DatabaseSeeder")
                .performedBy("SYSTEM")
                .timestamp(LocalDateTime.now())
                .build());

        System.out.println("--- Database Seeded Successfully ---");
    }
}
