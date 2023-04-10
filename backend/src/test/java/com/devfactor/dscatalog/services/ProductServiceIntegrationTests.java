package com.devfactor.dscatalog.services;

import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.repositories.ProductRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTests {
    @Autowired
    private ProductServices productServices;
    @Autowired
    private ProductRepository productRepository;
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setup(){
        existingId = 1L;
        nonExistingId = 0L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productServices.delete(existingId);
        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productServices.delete(nonExistingId));
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPageZeroSizeTeen() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDTO> result = productServices.findAllPaged(pageRequest, "", 0L);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }
    @Test
    public void findAllPageShouldReturnEmptyPageWhenPageDoesNotExists(){
        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductDTO> result = productServices.findAllPaged(pageRequest, "", 0L);
        Assertions.assertTrue(result.isEmpty());
    }
    @Test
    public void findAllPageShouldReturnOrderedPageWhenSortByName(){
        PageRequest pageRequest  = PageRequest.of(0,10, Sort.by("name"));
        Page<ProductDTO> result = productServices.findAllPaged(pageRequest, "", 0L);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}
