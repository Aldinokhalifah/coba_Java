package belajar_springFramework.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import belajar_springFramework.core.Repository.ProductRepository;
import lombok.Getter;

@Component
public class ProductService {
    
    @Getter
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductService(ProductRepository productRepository, String name) {
        this.productRepository = productRepository;
    }
}
