package com.example.bookexchange.data.service.system;

import com.example.bookexchange.data.exception.RecordNotFoundException;
import com.example.bookexchange.data.model.system.Pagination;
import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.repository.system.RoleRepository;
import com.example.bookexchange.data.repository.system.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserService} interface.
 * Wrapper for {@link UserRepository} + business logic.
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Page<User> findAll(Pagination pagination) {
        Pageable paging;
        if (pagination.getOrder() == null || pagination.getOrder().equals(""))
            paging = PageRequest.of(pagination.getPage(), pagination.getLimit(), Sort.by("id").descending());
        else {
            if (pagination.getType().toUpperCase().equals("ASC"))
                paging = PageRequest.of(pagination.getPage(), pagination.getLimit(), Sort.by(pagination.getOrder()).ascending());
            else
                paging = PageRequest.of(pagination.getPage(), pagination.getLimit(), Sort.by(pagination.getOrder()).descending());
        }
        Page<User> pagedResult;
        if (pagination.getSearch() == null)
            pagedResult = userRepository.findAll(paging);
        else {
            User user = new ObjectMapper().convertValue(pagination.getSearch(), User.class);
            ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
            Example<User> example = Example.of(user, matcher);
            pagedResult = userRepository.findAll(example, paging);
        }
        if (pagedResult.hasContent()) {
            log.info("IN findAll - {} roles found", pagedResult.getTotalElements());
            return pagedResult;
        } else return null;
    }

    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }
        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
    }

    public User saveUser(User user) {
        User result = findByUsername(user.getUsername());
        if (result == null) {
            userRepository.save(user);
            return user;
        } else return result;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void saveConfirmation(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        User result = userRepository.findByEmail(email);
        log.info("IN findByUsername - user: {} found by username: {}", result, email);
        return result;
    }

    public User findByPhone(String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }
}
