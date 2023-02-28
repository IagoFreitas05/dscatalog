package com.devfactor.dscatalog.services;


import com.devfactor.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductServices productServices;

    @Mock
    private ProductRepository productRepository;

}
