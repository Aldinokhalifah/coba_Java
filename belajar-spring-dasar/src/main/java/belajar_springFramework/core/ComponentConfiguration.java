package belajar_springFramework.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import belajar_springFramework.core.data.MultiFoo;

@Configuration
@ComponentScan(basePackages = {
    "belajar_SpringFramework.core.Service",
    "belajar_SpringFramework.core.Repository",
    "belajar_SpringFramework.core.Configuration",
}) 
@Import(MultiFoo.class)
public class ComponentConfiguration {
    
}
