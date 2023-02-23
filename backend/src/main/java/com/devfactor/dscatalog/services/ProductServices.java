package com.devfactor.dscatalog.services;

import com.devfactor.dscatalog.dto.CategoryDTO;
import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.entities.Category;
import com.devfactor.dscatalog.entities.Product;
import com.devfactor.dscatalog.repositories.CategoryRepository;
import com.devfactor.dscatalog.repositories.ProductRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductServices {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> product = productRepository.findAll(pageable);
        return product.map(productElement -> new ProductDTO(productElement));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product product = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product product = new Product();
        copyDTOtoEntity(productDTO, product);
        Product loadedProduct = productRepository.save(product);
        return new ProductDTO(loadedProduct, loadedProduct.getCategories());
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getOne(id);
            copyDTOtoEntity(productDTO, product);
            product = productRepository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("ID not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("ID not found " + id);
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceDatabaseIntegrityException("Integrity violation");
        }
    }

    public void copyDTOtoEntity(ProductDTO productDTO, Product product){
        product.setName(productDTO.getName());
        product.setDate(productDTO.getDate());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImgUrl(productDTO.getImgUrl());
        product.getCategories().clear();
        for(CategoryDTO categoryDTO : productDTO.getCategories()){
            Category category = categoryRepository.getOne(categoryDTO.getId());
            product.getCategories().add(category);
        }
    }
}
