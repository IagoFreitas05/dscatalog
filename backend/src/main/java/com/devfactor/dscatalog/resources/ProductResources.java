package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.ProductDTO;
import com.devfactor.dscatalog.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/product")
public class ProductResources {

    @Autowired
    ProductServices productServices;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy

    ) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ProductDTO> list = productServices.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO productDTO = productServices.findById(id);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO) {
        productDTO = productServices.save(productDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO, @PathVariable Long id) {
        productDTO = productServices.update(id, productDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long id) {
        productServices.delete(id);
        return ResponseEntity.noContent().build();
    }


}
