package belajar_springFramework.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "belajar_SpringFramework.core.Service"
}) 
public class ComponentConfiguration {
    
}
