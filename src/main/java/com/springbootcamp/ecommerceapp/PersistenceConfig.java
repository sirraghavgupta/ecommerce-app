package com.springbootcamp.ecommerceapp;

import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.utils.AuditorAwareImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
//@ComponentScan({"com.springbootcamp.ecommerceapp.repos"})
//@ComponentScan(basePackages={"com.springbootcamp.ecommerceapp"})
@EnableJpaAuditing
public class PersistenceConfig {

    @Autowired
    UserRepository userRepository;

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
