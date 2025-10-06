package belajar_springFramework.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import belajar_springFramework.core.Repository.CategoryRepository;
import lombok.Getter;

@Component
public class CategoryService {
    
    @Getter
    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository( CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
