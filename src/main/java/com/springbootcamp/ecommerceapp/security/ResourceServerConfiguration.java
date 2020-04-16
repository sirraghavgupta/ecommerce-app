package com.springbootcamp.ecommerceapp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    AppUserDetailsService userDetailsService;

    public ResourceServerConfiguration() {
        super();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/home").hasAnyRole("ADMIN")
                .antMatchers("/seller/home").hasAnyRole("SELLER","ADMIN")
                .antMatchers("/customer/home").hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/activate/customer").anonymous()
                .antMatchers("/activate/{id}").hasAnyRole("ADMIN")
                .antMatchers("/deactivate/{id}").hasAnyRole("ADMIN")
                .antMatchers("/register/*").anonymous()
                .antMatchers("/resend-activation-link/customer").anonymous()
                .antMatchers("/customers").hasAnyRole("ADMIN")
                .antMatchers("/sellers").hasAnyRole("ADMIN")
                .antMatchers("/forgot-password", "/reset-password", "/change-password").hasAnyRole("CUSTOMER", "SELLER","ADMIN")
                .antMatchers("/customer/profile").hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/seller/profile").hasAnyRole("SELLER","ADMIN")
                .antMatchers("/customer/addresses/*").hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/seller/addresses/*").hasAnyRole("SELLER","ADMIN")
                .antMatchers("/doLogout").hasAnyRole("CUSTOMER","SELLER","ADMIN")
                .antMatchers("/metadata-fields").hasAnyRole("ADMIN")
                .antMatchers("categories", "category/{id}").hasAnyRole("ADMIN")
                .antMatchers("/categories/customer").hasAnyRole("CUSTOMER","ADMIN")
                .antMatchers("/categories/seller").hasAnyRole("SELLER","ADMIN")
                .antMatchers("/metadata-field-values").hasAnyRole("ADMIN")
                .antMatchers("/seller/products").hasAnyRole("SELLER","ADMIN")
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/v2/api-docs").anonymous()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}