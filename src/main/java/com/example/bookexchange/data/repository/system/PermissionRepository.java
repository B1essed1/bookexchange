package com.example.bookexchange.data.repository.system;

import com.example.bookexchange.data.model.system.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long>, PagingAndSortingRepository<Permission, Long>, QueryByExampleExecutor<Permission> {
    Permission findByNameLt(String name);

    @Query(value = "SELECT p FROM Permission p WHERE p.id NOT IN (SELECT p.id FROM Permission p LEFT JOIN p.roles r WHERE r.id = ?1)")
    List<Permission> findNotInRole(Long roleId);

    List<Permission> findAllByRolesId(Long roleId);

    @Query(value = "SELECT p FROM Permission p WHERE p.id IN (?1)")
    List<Permission> findAllByPermissionIds(List<Long> permissionIds);

    @Query("SELECT p.type FROM Permission p GROUP BY p.type")
    List<String> findType();

    @Query("SELECT p FROM Permission p WHERE p.type=?1")
    List<Permission> findPermissionByType(String type);

    @Query(value = "select p.*, " +
            " (case " +
            " when p.id in (select rp.permission_id from role_permissions rp where rp.role_id = 1) then true " +
            " else false " +
            " end) as checked " +
            " from permissions p " +
            " order by checked ", nativeQuery = true)
    List<Map> listByRoleId(Long roleId);

    @Query("select p.type from Permission p group by p.type")
    List<String> listTypes();

}

