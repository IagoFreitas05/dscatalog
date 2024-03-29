package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.tests.Factory;
import com.devfactor.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTests {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private MockMvc mockMvc;
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 0L;
        countTotalProducts = 25L;
        mapper = new ObjectMapper();
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        mockMvc
                .perform(get("/product?page=0&size=12&sort=name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(countTotalProducts))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    }
}
