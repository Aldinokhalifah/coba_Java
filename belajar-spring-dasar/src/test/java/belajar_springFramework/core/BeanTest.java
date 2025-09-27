package belajar_springFramework.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import belajar_springFramework.core.data.Foo;

public class BeanTest {
    
    @Test
    void testCreateBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);

        Assertions.assertNotNull(context);
    }

    @Test
    void testGetBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);

        Object foo1 = context.getBean(Foo.class);
        Object foo2 = context.getBean(Foo.class);
        
        Assertions.assertSame(foo1, foo2);
    }
}