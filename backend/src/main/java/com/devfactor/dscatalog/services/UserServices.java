package com.devfactor.dscatalog.services;

import com.devfactor.dscatalog.dto.*;
import com.devfactor.dscatalog.entities.Category;
import com.devfactor.dscatalog.entities.Role;
import com.devfactor.dscatalog.entities.User;
import com.devfactor.dscatalog.repositories.CategoryRepository;
import com.devfactor.dscatalog.repositories.RoleRepository;
import com.devfactor.dscatalog.repositories.UserRepository;
import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserServices implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(UserServices.class);
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> user = userRepository.findAll(pageable);
        return user.map(userElement -> new UserDTO(userElement));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        User user = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO save(UserInsertDTO userDTO) {
        User user = new User();
        copyDTOtoEntity(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User loadedUser = userRepository.save(user);
        return new UserDTO(loadedUser);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userDTO) {
        try {
            User user = userRepository.getOne(id);
            copyDTOtoEntity(userDTO, user);
            user = userRepository.save(user);
            return new UserDTO(user);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("ID not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("ID not found " + id);
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceDatabaseIntegrityException("Integrity violation");
        }
    }

    public void copyDTOtoEntity(UserDTO userDTO, User user){
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(userDTO.getEmail());
        user.getRoles().clear();
        for(RoleDTO roleDTO : userDTO.getRoles()){
            Role role = roleRepository.getOne(roleDTO.getId());
            user.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            logger.error("User not found " + username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("User found " + username);
        return user;
    }
}
