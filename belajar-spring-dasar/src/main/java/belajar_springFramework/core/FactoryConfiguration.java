package belajar_springFramework.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import belajar_springFramework.core.factory.PaymentGatewayClientFactoryBean;

@Configuration
@Import({
    PaymentGatewayClientFactoryBean.class
})
public class FactoryConfiguration {
    
}
