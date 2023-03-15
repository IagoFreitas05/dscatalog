package com.devfactor.dscatalog.repositories;

import com.devfactor.dscatalog.entities.Category;
import com.devfactor.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
