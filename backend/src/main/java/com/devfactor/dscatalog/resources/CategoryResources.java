package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.CategoryDTO;
import com.devfactor.dscatalog.entities.Category;
import com.devfactor.dscatalog.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResources {

    @Autowired
    CategoryServices categoryServices;
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){
        return ResponseEntity.ok().body(categoryServices.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO categoryDTO = categoryServices.findById(id);
        return ResponseEntity.ok().body(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){
        categoryDTO = categoryServices.save(categoryDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(categoryDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO categoryDTO, @PathVariable Long id){
        categoryDTO = categoryServices.update(id ,categoryDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(categoryDTO);
    }

}
