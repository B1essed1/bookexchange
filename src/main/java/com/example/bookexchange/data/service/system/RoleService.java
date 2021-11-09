package com.example.bookexchange.data.service.system;

import com.example.bookexchange.data.model.system.Pagination;
import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.repository.system.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByCode(String code) {
        Role role = roleRepository.findByCode(code);
        if (role == null) {
            log.warn("IN findByCode - no role found by code: {}", code);
            return null;
        }
        log.info("IN findByCode - role: {} found by code: {}", role, code);
        return role;
    }

    public Role getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElse(null);

        if (role == null) {
            log.warn("IN findById - no role found by id: {}", id);
            return null;
        }
        log.info("IN findById - role: {} found by id: {}", role, id);
        return role;
    }

    public Role createRole(Role role) {
        Role roleDB = findByCode(role.getCode());
        if (roleDB == null) {
            roleRepository.save(role);
            return role;
        } else {
            log.info("IN create - user with username: {} exists", role.getCode());
            return null;
        }
    }

    public void updateRole(Role role) {
        roleRepository.save(role);
    }
}
