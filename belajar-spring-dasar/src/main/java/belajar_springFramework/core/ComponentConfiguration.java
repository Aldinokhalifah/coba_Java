package belajar_springFramework.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "belajar_SpringFramework.core.Service",
    "belajar_SpringFramework.core.Repository",
    "belajar_SpringFramework.core.Configuration",
}) 
public class ComponentConfiguration {
    
}
