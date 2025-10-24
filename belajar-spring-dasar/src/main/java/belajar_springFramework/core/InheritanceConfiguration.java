package belajar_springFramework.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import belajar_springFramework.core.Service.MerchantServiceImpl;

@Configuration
@Import(MerchantServiceImpl.class)
public class InheritanceConfiguration {
    
}
