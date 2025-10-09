SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `customer_order`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `cart_item`;
DROP TABLE IF EXISTS `shopping_cart`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) UNIQUE NOT NULL,
    `phone` VARCHAR(50) NOT NULL,
    `address` TEXT,
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER',
    `last_name` VARCHAR(100),
    `first_name` VARCHAR(100),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `category` (
    `category_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT
);

CREATE TABLE `products` (
    `product_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `price` DECIMAL(10,2) NOT NULL,
    `stock` INT NOT NULL DEFAULT 0,
    `status` VARCHAR(50) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `image_url` VARCHAR(500),
    `category_id` BIGINT NOT NULL,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`category_id`)
);

CREATE TABLE `cart` (
    `cart_id` INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE `cart_item` (
    `cart_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `cart_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1 CHECK (quantity >= 1),
    `sub_total_price` DECIMAL(10,2) NOT NULL CHECK (sub_total_price >= 1),
    FOREIGN KEY (`cart_id`) REFERENCES `cart`(`cart_id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`)
);

CREATE TABLE `payment` (
    `payment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `payment_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `amount` DECIMAL(10,2) NOT NULL CHECK (amount >= 1),
    `payment_method` VARCHAR(50) NOT NULL DEFAULT 'CREDIT_CARD',
    `status` VARCHAR(50) NOT NULL DEFAULT 'UNPAID'
);

CREATE TABLE `customer_order` (
    `order_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `payment_id` INT NOT NULL UNIQUE,
    `status` VARCHAR(50) NOT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`payment_id`) REFERENCES `payment`(`payment_id`)
);

CREATE TABLE `order_item` (
    `order_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `item_name` VARCHAR(255),
    `quantity` INT NOT NULL CHECK (quantity >= 1),
    `subtotal` DECIMAL(10,2) NOT NULL CHECK (subtotal >= 1),
    FOREIGN KEY (`order_id`) REFERENCES `customer_order`(`order_id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`)
);

CREATE TABLE `review` (
    `review_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `product_id` INT NOT NULL,
    `rating` INT CHECK (rating >= 1 AND rating <= 5),
    `content` TEXT,
    `image_path` VARCHAR(500),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`)
);

ALTER TABLE `user` ADD COLUMN `cart_id` INT UNIQUE;
ALTER TABLE `user` ADD FOREIGN KEY (`cart_id`) REFERENCES `cart`(`cart_id`);