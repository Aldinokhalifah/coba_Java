package belajar_springFramework.core.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FooBar {
    
    private Foo foo;
    private Bar bar;
}
