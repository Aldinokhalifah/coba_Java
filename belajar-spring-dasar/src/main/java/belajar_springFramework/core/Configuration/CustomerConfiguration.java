package belajar_springFramework.core.Configuration;

import org.springframework.context.annotation.Primary; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;

import belajar_springFramework.core.Repository.CustomerRepository;

@Configuration
public class CustomerConfiguration {
    
    @Primary
    @Bean
    public CustomerRepository normalCustomerRepository () {
        return new CustomerRepository();
    }

    @Bean
    public CustomerRepository premiumCustomerRepository () {
        return new CustomerRepository();
    }
}