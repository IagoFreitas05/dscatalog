package com.devfactor.dscatalog.services;

import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.entities.Product;
import com.devfactor.dscatalog.repositories.ProductRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductServices {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> product = productRepository.findAll(pageRequest);
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
        //product.setName(productDTO.getName());
        Product loadedProduct = productRepository.save(product);
        return new ProductDTO(loadedProduct);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getOne(id);
            //product.setName(productDTO.getName());
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
}
