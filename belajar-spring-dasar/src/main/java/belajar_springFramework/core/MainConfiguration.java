package belajar_springFramework.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import belajar_springFramework.core.Configuration.BarConfiguration;
import belajar_springFramework.core.Configuration.FooConfiguration;

@Configuration
@Import({
    FooConfiguration.class,
    BarConfiguration.class
})
public class MainConfiguration {
    
}
