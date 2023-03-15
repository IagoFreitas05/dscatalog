package com.devfactor.dscatalog.repositories;

import com.devfactor.dscatalog.entities.Role;
import com.devfactor.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
