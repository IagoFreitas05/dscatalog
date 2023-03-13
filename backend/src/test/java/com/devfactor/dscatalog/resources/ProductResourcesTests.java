package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.services.ProductServices;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devfactor.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResources.class)
public class ProductResourcesTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProductServices service;
    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private Long existingId;
    private Long nonExistingId;
    private long associatedId;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1l;
        nonExistingId = 0l;
        associatedId = 2l;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        Mockito.when(service.findAllPaged(any())).thenReturn(page);
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(ResourceDatabaseIntegrityException.class).when(service).delete(associatedId);
        Mockito.when(service.save(any())).thenReturn(productDTO);
    }

    @Test
    public void saveShouldReturnProductDto() throws  Exception {
        String json = mapper.writeValueAsString(productDTO);
        mockMvc
                .perform(post("/product")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws  Exception {
        mockMvc
                .perform(delete("/product/{id}", existingId)).andExpect(status().isNoContent());
    }
    @Test
    public void deleteShouldReturnNotFoundWhenIdNotExists() throws Exception {
        mockMvc
                .perform(delete("/product/{id}", nonExistingId)).andExpect(status().isNotFound());
    }
    @Test
    public void deleteshouldReturnBadRequestWhenIdIsAssociate() throws Exception {
        mockMvc.perform(delete("/product/{id}", associatedId)).andExpect(status().isBadRequest());
    }
    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);
        mockMvc
                .perform(put("/product/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateshouldReturnNotFoundWhenIdNotExists() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);
        ResultActions result = mockMvc
                .perform(put("/product/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void findAllShoulReturnPage() throws Exception {
        mockMvc.perform(get("/product")).andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/product/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findByIdShouldReturnResourceNotFoundWhenIdNotExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/product/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }
}