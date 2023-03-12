package com.devfactor.dscatalog.services;


import com.devfactor.dscatalog.repositories.ProductRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductServices productServices;
    @Mock
    private ProductRepository productRepository;
    private Long existingId;
    private Long nonExistingId;
    private Long depentedId;
    @BeforeEach
    public void setUp(){
        existingId = 1L;
        nonExistingId = 0L;
        depentedId = 4L;

        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(depentedId);
    }
    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            productServices.delete(existingId);
        });

        Mockito.verify(productRepository).deleteById(existingId);
    }
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class ,() -> {
            productServices.delete(nonExistingId);
        });
    }
    @Test
    public void deleteShouldThrowResourceDatabaseIntegrityException(){
        Assertions.assertThrows(ResourceDatabaseIntegrityException.class, ()->{
            productServices.delete(depentedId);
        });
    }
}
