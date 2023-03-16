package com.devfactor.dscatalog.resources;

import com.devfactor.dscatalog.dto.UserDTO;
import com.devfactor.dscatalog.dto.UserInsertDTO;
import com.devfactor.dscatalog.dto.UserUpdateDTO;
import com.devfactor.dscatalog.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserResources {

    @Autowired
    UserServices userServices;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        Page<UserDTO> list = userServices.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO userDTO = userServices.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@Valid @RequestBody UserInsertDTO userDTO) {
        UserDTO userDTOPersisted = userServices.save(userDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTOPersisted.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTOPersisted);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserUpdateDTO userDTO, @PathVariable Long id) {
        UserDTO userDTOpersisted = userServices.update(id, userDTO);
        return ResponseEntity.ok().body(userDTOpersisted);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id) {
        userServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}
