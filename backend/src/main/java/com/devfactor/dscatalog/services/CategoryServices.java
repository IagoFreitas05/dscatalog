package com.devfactor.dscatalog.services;

import com.devfactor.dscatalog.dto.CategoryDTO;
import com.devfactor.dscatalog.entities.Category;
import com.devfactor.dscatalog.repositories.CategoryRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServices {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> category = categoryRepository.findAll();
        return  category
                .stream()
                .map(categoryElement -> new CategoryDTO(categoryElement))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> optional = categoryRepository.findById(id);
        Category category = optional.orElseThrow(() ->new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category loadedCategory = categoryRepository.save(category);
        return new CategoryDTO(loadedCategory);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepository.getOne(id);
            category.setName(categoryDTO.getName());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        }
        catch (EntityNotFoundException exception){
            throw new ResourceNotFoundException("ID not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            throw new ResourceNotFoundException("ID not found "+ id);
        }catch (DataIntegrityViolationException exception){
            throw new ResourceDatabaseIntegrityException("Integrity violation");
        }
    }
}
