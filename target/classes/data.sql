INSERT INTO `category` (`name`, `description`) VALUES
('电子产品', '手机、电脑、平板等电子设备'),
('图书', '各种书籍和教材'),
('生活用品', '日常家居用品'),
('服装', '衣服、鞋帽等');

INSERT INTO `products` (`name`, `description`, `price`, `stock`, `status`, `image_url`, `category_id`) VALUES
('iPhone 15 Pro', '最新款苹果手机，A17芯片', 999.00, 50, 'AVAILABLE', '/images/iphone15.jpg', 1),
('MacBook Pro 14"', 'M3芯片专业笔记本电脑', 1999.00, 20, 'AVAILABLE', '/images/macbook.jpg', 1),
('Spring Boot实战', 'Spring框架学习指南', 45.00, 100, 'AVAILABLE', '/images/spring-book.jpg', 2),
('陶瓷咖啡杯', '优质陶瓷保温杯', 25.00, 200, 'AVAILABLE', '/images/cup.jpg', 3),
('无线蓝牙耳机', '高音质无线耳机', 199.00, 75, 'AVAILABLE', '/images/earphones.jpg', 1);


INSERT INTO `cart` (`cart_id`) VALUES (1), (2), (3);

INSERT INTO `user` (`username`, `password`, `email`, `phone`, `address`, `role`, `last_name`, `first_name`, `cart_id`) VALUES
('john_doe', '$2a$10$v02OYmTrTiSate2DTdrjlu0ctCWuIwFq0cyXZ29l9nc1y17Bc4Xp', 'john@example.com', '13800138001', '北京市朝阳区', 'USER', 'Doe', 'John', 1),
('jane_smith', '$2a$10$examplehash2', 'jane@example.com', '13800138002', '上海市浦东新区', 'USER', 'Smith', 'Jane', 2),
('admin', '$2a$10$examplehash3', 'admin@example.com', '13800138003', '广州市天河区', 'ADMIN', 'Admin', 'System', 3);

INSERT INTO `cart_item` (`cart_id`, `product_id`, `quantity`, `sub_total_price`) VALUES
(1, 1, 1, 999.00),
(1, 3, 2, 90.00),
(2, 2, 1, 1999.00),
(3, 4, 3, 75.00);

INSERT INTO `payment` (`amount`, `payment_method`, `status`) VALUES
(1089.00, 'CREDIT_CARD', 'PAID'),
(1999.00, 'PAYPAL', 'PAID'),
(75.00, 'ALIPAY', 'UNPAID');

INSERT INTO `customer_order` (`user_id`, `payment_id`, `status`, `total_price`, `created_at`) VALUES
(1, 1, 'DELIVERED', 1089.00, '2024-01-15 10:30:00'),
(2, 2, 'SHIPPED', 1999.00, '2024-01-16 14:20:00'),
(3, 3, 'PENDING', 75.00, '2024-01-17 09:15:00');

INSERT INTO `order_item` (`order_id`, `product_id`, `item_name`, `quantity`, `subtotal`) VALUES
(1, 1, 'iPhone 15 Pro', 1, 999.00),
(1, 3, 'Spring Boot实战', 2, 90.00),
(2, 2, 'MacBook Pro 14"', 1, 1999.00),
(3, 4, '陶瓷咖啡杯', 3, 75.00);

INSERT INTO `review` (`user_id`, `product_id`, `rating`, `content`) VALUES
(1, 1, 5, '手机非常好用，运行流畅！'),
(2, 2, 4, '电脑性能强大，就是价格有点贵'),
(1, 3, 5, '这本书对学习Spring Boot很有帮助');