package com.springbootcamp.ecommerceapp.bootloader;


import com.springbootcamp.ecommerceapp.entities.*;
import com.springbootcamp.ecommerceapp.repos.*;
import com.springbootcamp.ecommerceapp.security.UserDao;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    @Autowired
    CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    CartRepository cartRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
/*
        if (userRepository.count() < 1) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


//            new Address("75/60, ballabgarh", "faridabad", "haryana", "778654", "india", "home");
//            new Address("B-60", "faridabad", "haryana", "778884", "india", "home");
//            new Address("B-70", "palwal", "haryana", "778884", "india", "home");
//            new Address("B-80", "rewari", "haryana", "778884", "india", "home");
//            new Address("B-90", "homely", "haryana", "778884", "india", "home");


            Role admin = new Role(1, "ROLE_ADMIN");
            Role seller = new Role(2, "ROLE_SELLER");
            Role customer = new Role(3, "ROLE_CUSTOMER");

            roleRepository.save(admin);
            roleRepository.save(customer);
            roleRepository.save(seller);


            Admin admin1 = new Admin("draghavgupta.96@gmail.com", "admin", "", "admin");
            admin1.setPassword(passwordEncoder.encode("pass"));
            admin1.addRole(admin);
            admin1.addAddress(new Address("B-90", "homely", "haryana", "778884", "india", "home"));
            admin1.addAddress(new Address("75/60, ballabgarh", "faridabad", "haryana", "778654", "india", "home"));
            admin1.setActive(true);


            Customer customer1 = new Customer("customer@customer.com", "customer", "", "customer", "9873556644");
            customer1.setPassword(passwordEncoder.encode("pass"));
            customer1.addAddress(new Address("B-70", "palwal", "haryana", "778884", "india", "home"));
            customer1.addAddress(new Address("B-100", "london", "haryana", "778884", "india", "home"));
            customer1.setActive(true);

            Seller seller1 = new Seller("seller.seller@tothenew.com", "seller", "", "seller", "bh7ht754r5", "amalgam pvt. lmt.", "9999988817");
            seller1.setPassword(passwordEncoder.encode("pass"));
            seller1.addAddress(new Address("B-890", "rewari", "haryana", "778884", "india", "home"));
            seller1.setActive(true);


            userRepository.save(admin1);
            userRepository.save(customer1);
            userRepository.save(seller1);

            System.out.println("Total users saved::" + userRepository.count());


            Product product1 = new Product("UCB T-Shirt", "very attractive and comfortable", "UCB");
//            Product product2 = new Product("RedTape Jeans", "slim fit", "RedTape");
//            Product product3 = new Product("Nike shoes", "light weight", "Nike");

            product1.setId(100L);

            Category fashion = new Category("fashion");
            Category clothing = new Category("clothing");
            fashion.addSubCategory(clothing);
            Category men = new Category("men");
            Category women = new Category("women");
            clothing.addSubCategory(men);
            clothing.addSubCategory(women);

            categoryRepository.save(fashion);

            System.out.println("total categories saved - " + categoryRepository.count());


            ProductVariation mSize = new ProductVariation(5, 1500d);
            Map<String, String> attributes1 = new HashMap<>();
            attributes1.put("size", "M-Size");
            attributes1.put("gender", "female");
            mSize.setProductAttributes(attributes1);

            ProductVariation lSize = new ProductVariation(3, 1600d);
            Map<String, String> attributes2 = new HashMap<>();
            attributes2.put("size", "L-Size");
            attributes2.put("gender", "male");
            lSize.setProductAttributes(attributes2);


            product1.setCategory(men);
            product1.addVariation(mSize);
            product1.addVariation(lSize);

            seller1.addProduct(product1);

            productRepository.save(product1);


//  =============================================================================
            ProductReview review1 = new ProductReview("awesome", 4.3d);
            ProductReview review2 = new ProductReview("comfortable", 4.8d);

            Customer cust1 = customerRepository.findByEmail("customer@customer.com");
            Product prod1 = productRepository.findById(13L).get();
            cust1.addReview(review1);
            prod1.addReview(review1);
            System.out.println(review1.getProductReviewId());

            productReviewRepository.save(review1);


//            ProductVariation var = productVariationRepository.findById(17L).get();
//            Map<String, Object> attributes = var.getProductAttributes();
//            System.out.println(attributes.keySet());


//  ============================================================================

            String sizeValues = "XS,S,M,L,XL,XXL";
            CategoryMetadataFieldValues fieldValues = new CategoryMetadataFieldValues(sizeValues);
//        String colorValues = "red, green, yellow";
//        CategoryMetadataFieldValues fieldValues1 = new CategoryMetadataFieldValues(colorValues);
//        String ramValues = "6gb, 8gb, 16gb";
//        CategoryMetadataFieldValues fieldValues2 = new CategoryMetadataFieldValues(ramValues);

            CategoryMetadataField sizeField = new CategoryMetadataField("size");
//        CategoryMetadataField colorField = new CategoryMetadataField("color");
//        CategoryMetadataField ramField = new CategoryMetadataField("ram");

            System.out.println(sizeField.getName() + "############################");

            Category kids = new Category("kids");

            Category clothing1 = categoryRepository.findByName("clothing");
            clothing1.addSubCategory(kids);
            categoryRepository.save(clothing1);

            kids = categoryRepository.findByName("kids");
//        kids.addFieldValues(fieldValues);
//        kids.addFieldValues(fieldValues1);
//        kids.addFieldValues(fieldValues2);
//        sizeField.addFieldValues(fieldValues);
            fieldRepository.save(sizeField);

//        colorField.addFieldValues(fieldValues1);
//        ramField.addFieldValues(fieldValues2);

            fieldValues.setCategoryMetadataField(sizeField);
            fieldValues.setCategory(kids);
            categoryMetadataFieldValuesRepository.save(fieldValues);

//
//        categoryMetadataFieldValuesRepository.save(fieldValues1);
//        categoryMetadataFieldValuesRepository.save(fieldValues2);


//    =======================================================================
//    ================= FOR ORDER DOMAIN NOW ================================


            Orders order1 = new Orders();
            order1.setDateCreated(new Date());
            order1.setPaymentMethod("Cash on Delivery");
            order1.setId(2009992L);
            Customer grahak = customerRepository.findByEmail("customer@customer.com");
            order1.setCustomer(grahak);
            order1.setDeliveryAddress(new OrderAddress(new Address("B-70", "palwal", "haryana", "778884", "india", "home")));

            ProductVariation buy1 = productVariationRepository.findById(14L).get();
            ProductVariation buy2 = productVariationRepository.findById(15L).get();

            OrderProduct orderProduct1 = new OrderProduct();
            orderProduct1.setProductVariation(buy1);

            OrderProduct orderProduct2 = new OrderProduct();
            orderProduct2.setProductVariation(buy2);

            order1.addOrderProduct(orderProduct1);
            order1.addOrderProduct(orderProduct2);
            order1.calculateBillAmount();

            Cart cart1 = new Cart();
            cart1.setOwner(customer1);
            cart1.setItem(buy1);
            cart1.setQuantity(1);
            cart1.setWishlisted(true);


            ordersRepository.save(order1);
            cartRepository.save(cart1);

//  ===============================================================================

            Category mobiles = new Category("mobiles");
            categoryRepository.save(mobiles);


//        }

        }


 */
        System.out.println("application went up !!==============================");

    }
}

