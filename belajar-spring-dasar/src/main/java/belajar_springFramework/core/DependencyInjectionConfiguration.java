package belajar_springFramework.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import belajar_springFramework.core.data.Bar;
import belajar_springFramework.core.data.Foo;
import belajar_springFramework.core.data.FooBar;

@Configuration
public class DependencyInjectionConfiguration {
    
    @Bean
    public Foo foo() {
        return new Foo();
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }

    @Bean
    public FooBar fooBar(Foo foo, Bar bar) {
        return new FooBar(foo, bar);
    }
}
