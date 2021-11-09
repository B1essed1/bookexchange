package com.example.bookexchange.data.service.system;

import com.example.bookexchange.data.exception.RecordNotFoundException;
import com.example.bookexchange.data.model.system.Pagination;
import com.example.bookexchange.data.model.system.Permission;
import com.example.bookexchange.data.repository.system.PermissionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Page<Permission> findAll(Pagination pagination) {
        Pageable paging;
        if (pagination.getOrder() == null || pagination.getOrder().equals(""))
            paging = PageRequest.of(pagination.getPage(), pagination.getLimit(), Sort.by("id").descending());
        else {
            if (pagination.getType().toUpperCase().equals("ASC"))
                paging = PageRequest.of(pagination.getPage(), pagination.getLimit(), Sort.by(pagination.getOrder()).ascending());
            else
                paging = PageRequest.of(pagination.getPage(), pagination.getLimit(), Sort.by(pagination.getOrder()).descending());
        }
        Page<Permission> pagedResult;
        if (pagination.getSearch() == null)
            pagedResult = permissionRepository.findAll(paging);
        else {
            Permission permission = new ObjectMapper().convertValue(pagination.getSearch(), Permission.class);
            ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
            Example<Permission> employeeExample = Example.of(permission, matcher);
            pagedResult = permissionRepository.findAll(employeeExample, paging);
        }
        return pagedResult;
    }

    public List<Permission> findAll() {
        List<Permission> permissionList = (List<Permission>) permissionRepository.findAll();
        if (permissionList.size() > 0) {
            log.info("IN findAll - {} permissions found", permissionList);
            return permissionList;
        } else return null;
    }

    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
        log.info("IN save - permission: {} successfully saved", permission);
    }

    public Permission getByPermissionId(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission == null) {
            log.warn("IN findById - no permission found by id: {}", id);
            return null;
        }

        log.info("IN findById - permission: {} found by id: {}", permission, id);
        return permission;
    }

    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
        log.info("IN delete - permission with id: {} successfully deleted", id);
    }

    public Permission getPermissionByName(String name) {
        return permissionRepository.findByNameLt(name);
    }

    public List<Permission> findNotInRole(Long roleId) {
        return permissionRepository.findNotInRole(roleId);
    }

    public List<Permission> findAllByRoleId(Long roleId) {
        return permissionRepository.findAllByRolesId(roleId);
    }

    public List<Permission> getByPermissionIds(List<Long> permissionIds) {
        return permissionRepository.findAllByPermissionIds(permissionIds);
    }

    public List<String> getType() {
        return permissionRepository.findType();
    }

    public List<Permission> getPermission(String type) {
        return permissionRepository.findPermissionByType(type);
    }

    public Map<String, List> listByRoleId(Long roleId) {
        Map<String, List> map = new HashMap();
        List<String> types = permissionRepository.listTypes();
        List<Map> permissions = permissionRepository.listByRoleId(roleId);
        for (String type : types) {
            List list = new ArrayList();
            for (Map permission : permissions) {
                String permissionType = permission.get("type").toString();
                if (type.equalsIgnoreCase(permissionType)) {
                    list.add(permission);
                }
            }
            map.put(type, list);
        }
        return map;
    }
}
