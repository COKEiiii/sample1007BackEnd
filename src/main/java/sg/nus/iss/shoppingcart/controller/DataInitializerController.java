package sg.nus.iss.shoppingcart.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.model.Product;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.repository.CategoryRepository;
import sg.nus.iss.shoppingcart.repository.ProductRepository;
import sg.nus.iss.shoppingcart.repository.UserRepository;
import sg.nus.iss.shoppingcart.enums.Role;
import sg.nus.iss.shoppingcart.enums.ProductStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;

@Component
public class DataInitializerController implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializerController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始初始化基础数据...");

        try {
            initializeUsers();

            initializeCategories();

            initializeProducts();

            logger.info("基础数据初始化完成");
        } catch (Exception e) {
            logger.error("数据初始化过程中发生错误: {}", e.getMessage(), e);
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            try {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@shoppingcart.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPhone("+65-9000-0001");
                admin.setRole(Role.ADMIN);
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setAddress("NUS, Singapore");
                userRepository.save(admin);

                User customer = new User();
                customer.setUsername("customer1");
                customer.setEmail("customer@shoppingcart.com");
                customer.setPassword(passwordEncoder.encode("customer123"));
                customer.setPhone("+65-9000-0002");
                customer.setRole(Role.USER);
                customer.setFirstName("John");
                customer.setLastName("Doe");
                customer.setAddress("Singapore");
                userRepository.save(customer);

                User customer2 = new User();
                customer2.setUsername("testuser");
                customer2.setEmail("test@shoppingcart.com");
                customer2.setPassword(passwordEncoder.encode("test123"));
                customer2.setPhone("+65-9000-0003");
                customer2.setRole(Role.USER);
                customer2.setFirstName("Jane");
                customer2.setLastName("Smith");
                customer2.setAddress("123 Orchard Road, Singapore");
                userRepository.save(customer2);

                logger.info("成功初始化用户数据：创建了{}个用户", userRepository.count());
            } catch (DataIntegrityViolationException e) {
                logger.warn("用户数据可能已存在，跳过初始化");
            }
        } else {
            logger.info("用户数据已存在，跳过初始化。当前用户数：{}", userRepository.count());
        }
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            try {
                String[][] categories = {
                        { "Electronics", "电子产品和设备" },
                        { "Clothing", "服装和配饰" },
                        { "Books", "图书和教育材料" },
                        { "Home & Garden", "家居和园艺用品" },
                        { "Sports", "运动装备" },
                        { "Beauty", "美容和个人护理" },
                        { "Toys", "玩具和游戏" }
                };

                for (String[] categoryInfo : categories) {
                    Category category = new Category();
                    category.setName(categoryInfo[0]);
                    category.setDescription(categoryInfo[1]);
                    categoryRepository.save(category);
                }

                logger.info("成功初始化分类数据：创建了{}个分类", categoryRepository.count());
            } catch (Exception e) {
                logger.error("初始化分类数据时发生错误: {}", e.getMessage(), e);
            }
        } else {
            logger.info("分类数据已存在，跳过初始化。当前分类数：{}", categoryRepository.count());
        }
    }

    private void initializeProducts() {
        if (categoryRepository.count() > 0) {
            try {
                if (productRepository.count() > 0) {
                    productRepository.deleteAll();
                    logger.info("清空现有商品数据，准备重新初始化");
                }

                Category electronics = categoryRepository.findByName("Electronics")
                        .orElseThrow(() -> new RuntimeException("Electronics category not found"));

                Category books = categoryRepository.findByName("Books")
                        .orElseThrow(() -> new RuntimeException("Books category not found"));

                Category clothing = categoryRepository.findByName("Clothing")
                        .orElseThrow(() -> new RuntimeException("Clothing category not found"));

                initializeElectronicsProducts(electronics);
                initializeBookProducts(books);
                initializeClothingProducts(clothing);

                logger.info("成功重新初始化产品数据：创建了{}个产品，使用本地图片", productRepository.count());
            } catch (Exception e) {
                logger.error("初始化产品数据时发生错误: {}", e.getMessage(), e);
            }
        } else {
            logger.info("缺少分类，无法初始化产品数据。当前分类数：{}", categoryRepository.count());
        }
    }

    private void initializeElectronicsProducts(Category electronics) {
        Product smartphone = new Product();
        smartphone.setName("Smartphone X10");
        smartphone.setDescription("最新款智能手机，配备高清摄像头和长续航电池");
        smartphone.setPrice(899.99);
        smartphone.setStock(50);
        smartphone.setCategory(electronics);
        smartphone.setStatus(ProductStatus.AVAILABLE);
        smartphone.setImageUrl("/images/smartphone-x10.jpg");
        productRepository.save(smartphone);
        logger.info("创建商品: {} - 图片路径: {}", smartphone.getName(), smartphone.getImageUrl());

        Product laptop = new Product();
        laptop.setName("Gaming Laptop Pro");
        laptop.setDescription("高性能游戏笔记本电脑，适合专业游戏和设计工作");
        laptop.setPrice(1299.99);
        laptop.setStock(25);
        laptop.setCategory(electronics);
        laptop.setStatus(ProductStatus.AVAILABLE);
        laptop.setImageUrl("/images/gaming-laptop.jpg");
        productRepository.save(laptop);
        logger.info("创建商品: {} - 图片路径: {}", laptop.getName(), laptop.getImageUrl());
    }

    private void initializeBookProducts(Category books) {
        Product javaBook = new Product();
        javaBook.setName("Java Programming Guide");
        javaBook.setDescription("完整的Java编程学习指南，从基础到高级");
        javaBook.setPrice(49.99);
        javaBook.setStock(100);
        javaBook.setCategory(books);
        javaBook.setStatus(ProductStatus.AVAILABLE);
        javaBook.setImageUrl("/images/java-book.jpg");
        productRepository.save(javaBook);
        logger.info("创建商品: {} - 图片路径: {}", javaBook.getName(), javaBook.getImageUrl());
    }

    private void initializeClothingProducts(Category clothing) {
        Product tshirt = new Product();
        tshirt.setName("Cotton T-Shirt");
        tshirt.setDescription("100%纯棉舒适T恤，多种颜色可选");
        tshirt.setPrice(19.99);
        tshirt.setStock(200);
        tshirt.setCategory(clothing);
        tshirt.setStatus(ProductStatus.AVAILABLE);
        tshirt.setImageUrl("/images/cotton-tshirt.jpg");
        productRepository.save(tshirt);
        logger.info("创建商品: {} - 图片路径: {}", tshirt.getName(), tshirt.getImageUrl());

        checkImageFileExists("cotton-tshirt.jpg");
    }

    private void checkImageFileExists(String filename) {
        java.io.File imageFile = new java.io.File("uploads/images/" + filename);
        if (imageFile.exists()) {
            logger.info("图片文件存在: {} - 大小: {} bytes", filename, imageFile.length());
        } else {
            logger.warn("图片文件不存在: {}", filename);
        }
    }
}