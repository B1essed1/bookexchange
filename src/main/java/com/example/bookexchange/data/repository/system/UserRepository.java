package com.example.bookexchange.data.repository.system;

import com.example.bookexchange.data.model.main.Book;
import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.model.system.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long>, QueryByExampleExecutor<User> {
    User findByUsername(String name);

    @Query("SELECT u FROM User u ORDER BY u.username")
    Page<User> findAll(Pageable paging);

    @Query("select u from User u where u.phone = ?1")
    Optional<User> findByPhone(String phone);

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

}