package com.devfactor.dscatalog.repositories;

import com.devfactor.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    private Long existingId;
    private Long nonExistingId;
    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 0L;
    }
    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
           productRepository.deleteById(nonExistingId);
        });
    }
}
