package com.springbootcamp.ecommerceapp.services;


import com.springbootcamp.ecommerceapp.entities.Address;
import com.springbootcamp.ecommerceapp.entities.Role;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.repos.RoleRepository;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

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


            User user2 = new User("customer.customer@tothenew.com", "customer", "", "customer");
            user2.setPassword(passwordEncoder.encode("pass"));
            user2.addRole(customer);
            user2.addAddress(new Address("B-70", "palwal", "haryana", "778884", "india", "home"));
            user2.addAddress(new Address("B-100", "london", "haryana", "778884", "india", "home"));



            User user3 = new User("seller.seller@tothenew.com", "seller", "", "seller");
            user3.setPassword(passwordEncoder.encode("pass"));
            user3.addRole(seller);
            user3.addAddress(new Address("B-890", "rewari", "haryana", "778884", "india", "home"));



            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            System.out.println("Total users saved::"+userRepository.count());

        }
    }
}
