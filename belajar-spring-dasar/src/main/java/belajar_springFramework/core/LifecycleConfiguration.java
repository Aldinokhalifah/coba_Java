package belajar_springFramework.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import belajar_springFramework.core.data.Connection;
import belajar_springFramework.core.data.Server;

@Configuration
public class LifecycleConfiguration {
    
    @Bean
    public Connection connection() {
        return new Connection();
    }

    // @Bean(initMethod= "start", destroyMethod= "stop")
    @Bean
    public Server server() {
        return new Server();
    }
}
