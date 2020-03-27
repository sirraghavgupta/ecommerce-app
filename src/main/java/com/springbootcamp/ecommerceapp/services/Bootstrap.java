package com.springbootcamp.ecommerceapp.services;


import com.springbootcamp.ecommerceapp.entities.*;
import com.springbootcamp.ecommerceapp.repos.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    UserDao userDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.count()<1){
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


            User user1 = new User("admin.admin@tothenew.com", "admin", "", "admin");
            user1.setPassword(passwordEncoder.encode("pass"));
            user1.addRole(admin);
            user1.addRole(seller);
            user1.addAddress(new Address("B-90", "homely", "haryana", "778884", "india", "home"));
            user1.addAddress(new Address("75/60, ballabgarh", "faridabad", "haryana", "778654", "india", "home"));


            Customer user2 = new Customer("customer.customer@tothenew.com", "customer", "", "customer", "9873556644");
            user2.setPassword(passwordEncoder.encode("pass"));
            user2.addAddress(new Address("B-70", "palwal", "haryana", "778884", "india", "home"));
            user2.addAddress(new Address("B-100", "london", "haryana", "778884", "india", "home"));



            Seller seller1 = new Seller("seller.seller@tothenew.com", "seller", "", "seller","bh7ht754r5", "amalgam pvt. lmt.", "9999988817");
            seller1.setPassword(passwordEncoder.encode("pass"));
            seller1.addAddress(new Address("B-890", "rewari", "haryana", "778884", "india", "home"));



            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(seller1);

            System.out.println("Total users saved::"+userRepository.count());


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
            System.out.println("total categories saved - "+ categoryRepository.count());


            ProductVariation mSize = new ProductVariation(5, 1500d);
            mSize.setMetadata("M - size");
            ProductVariation lSize = new ProductVariation(3, 1600d);
            lSize.setMetadata("L - size");

            product1.setCategory(men);
            product1.addVariation(mSize);
            product1.addVariation(lSize);
            seller1.addProduct(product1);


            ProductReview review1 = new ProductReview("awesome", 4.3d);
            review1.setAuthor(user2);
            ProductReview review2 = new ProductReview("confortable", 4.8d);
            review2.setAuthor(user2);

            product1.addReview(review1);
            product1.addReview(review2);

            productRepository.save(product1);
        }
    }
}
