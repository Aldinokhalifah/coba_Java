package belajar_springFramework.core;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import belajar_springFramework.core.data.Bar;
import belajar_springFramework.core.data.Foo;
import belajar_springFramework.core.data.FooBar;

@Configuration
public class OptionalConfiguration {
    

    @Bean
    public Foo foo() {
        return new Foo();
    }

    @Bean
    public FooBar fooBar(Optional<Foo> foo, Optional<Bar> bar) {
        return new FooBar(foo.orElse(null), bar.orElse(null));
    }
}
