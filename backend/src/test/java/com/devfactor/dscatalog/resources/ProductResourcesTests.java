package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.services.ProductServices;
import com.devfactor.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResources.class)
public class ProductResourcesTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductServices service;
    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    @BeforeEach
    void setup() throws Exception{
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
    }
    @Test
    public void findAllShoulReturnPage() throws Exception{
        mockMvc.perform(get("/product")).andExpect(status().isOk());
    }
}
