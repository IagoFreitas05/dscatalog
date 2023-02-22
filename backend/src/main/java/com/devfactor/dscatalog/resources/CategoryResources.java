package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.CategoryDTO;
import com.devfactor.dscatalog.entities.Category;
import com.devfactor.dscatalog.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
