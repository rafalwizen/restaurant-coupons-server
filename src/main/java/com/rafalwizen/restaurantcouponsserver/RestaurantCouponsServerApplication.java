package com.rafalwizen.restaurantcouponsserver;

import com.rafalwizen.restaurantcouponsserver.model.Admin;
import com.rafalwizen.restaurantcouponsserver.service.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantCouponsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantCouponsServerApplication.class, args);
    }

    /**
     * Initialize admin user on application startup if not exists
     */
    @Bean
    public CommandLineRunner initAdminUser(AdminService adminService) {
        return args -> {
            if (!adminService.existsByUsername("admin")) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // Will be encoded by the service
                admin.setEmail("admin@restaurant.com");
                admin.setRole("ADMIN");
                adminService.createAdmin(admin);
                System.out.println("Default admin user created");
            }
        };
    }
}
