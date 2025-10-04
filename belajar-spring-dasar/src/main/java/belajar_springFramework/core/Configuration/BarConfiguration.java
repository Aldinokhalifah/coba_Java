package belajar_springFramework.core.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import belajar_springFramework.core.data.Bar;

@Configuration
public class BarConfiguration {
    
    @Bean
    public Bar bar() {
        return new Bar();
    }
}
