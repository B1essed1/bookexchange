package com.example.bookexchange.data.repository.system;

import com.example.bookexchange.data.model.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Role}.
 */

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>, PagingAndSortingRepository<Role, Long>, QueryByExampleExecutor<Role> {
    Role findByName(String name);

    Role findByCode(String code);
}
