package com.devfactor.dscatalog.repositories;

import com.devfactor.dscatalog.entities.Product;
import com.devfactor.dscatalog.tests.Factory;
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
    private Long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 0L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);
        product = productRepository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(product.getId(), 26);
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
        Optional<Product> product = productRepository.findById(nonExistingId);
        Assertions.assertTrue(product.isEmpty());
    }

    @Test
    public void findByIdShouldReturnOptionalProductWhenIdExists() {
        Optional<Product> product = productRepository.findById(existingId);
        Assertions.assertTrue(product.isPresent());
    }
}
