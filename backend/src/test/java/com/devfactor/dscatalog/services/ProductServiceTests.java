package com.devfactor.dscatalog.services;


import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.entities.Product;
import com.devfactor.dscatalog.repositories.ProductRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devfactor.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductServices productServices;
    @Mock
    private ProductRepository productRepository;
    private Long existingId;
    private Long nonExistingId;
    private Long depentedId;
    private PageImpl<Product> page;
    private Product product;
    @BeforeEach
    public void setUp(){
        existingId = 1L;
        nonExistingId = 0L;
        depentedId = 4L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
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
    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = productServices.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }
    @Test
    public void findByIdshouldReturnProductDtoWhenIdExists(){
        ProductDTO productDTO = productServices.findById(existingId);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            productServices.findById(nonExistingId);
        });
    }
}
