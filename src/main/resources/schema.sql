-- restaurant_coupon_db.sql

-- Create database (if not using Spring's auto-creation)
-- CREATE DATABASE IF NOT EXISTS restaurant_coupon_db;
-- USE restaurant_coupon_db;

-- Admins Table
CREATE TABLE IF NOT EXISTS admins (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Coupons Table
CREATE TABLE IF NOT EXISTS coupons (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    discount_value DECIMAL(10,2) NOT NULL,
    valid_from TIMESTAMP NOT NULL,
    valid_to TIMESTAMP NOT NULL,
    terms_and_conditions VARCHAR(1000),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Insert initial admin user (Password will be encoded by the application)
-- INSERT INTO admins (username, password, email, role)
-- VALUES ('admin', '$2a$10$lOuFkDHC9X2qxnVFxQjZzeKfMWyVnCEi69JuXzKwbWV6x52e8V/5O', 'admin@restaurant.com', 'ADMIN');

-- Insert sample coupons
INSERT INTO coupons (name, description, discount_value, valid_from, valid_to, terms_and_conditions, is_active)
VALUES
    ('Summer Special', '15% off on all summer dishes', 15.00, '2025-04-01 00:00:00', '2025-06-30 23:59:59', 'Not valid on weekends and holidays', TRUE),
    ('Welcome Coupon', 'Get 10% off on your first order', 10.00, '2025-01-01 00:00:00', '2025-12-31 23:59:59', 'Applicable only for new customers', TRUE),
    ('Happy Hour', '20% off on beverages between 5 PM and 7 PM', 20.00, '2025-04-01 00:00:00', '2025-05-31 23:59:59', 'Valid only for dine-in customers', TRUE),
    ('Lunch Combo', 'Get a free dessert with any lunch combo', 100.00, '2025-04-01 00:00:00', '2025-04-30 23:59:59', 'Valid Monday to Friday, 11 AM to 3 PM', TRUE);