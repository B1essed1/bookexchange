package com.example.bookexchange.data.utility;

import com.example.bookexchange.data.model.reference.Reference;
import com.example.bookexchange.data.model.system.Permission;
import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.service.reference.ReferenceService;
import com.example.bookexchange.data.service.system.PermissionService;
import com.example.bookexchange.data.service.system.RoleService;
import com.example.bookexchange.data.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Set;


@Slf4j
@Configuration
public class AnnotationInitialScan {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
//    @Autowired
//    private ReferenceService referenceService;

    @PostConstruct
    public void postConstruct() {
        this.startPerm();
        this.startRole();
        this.startUser();
        this.initializeReferences();
    }

    public void startPerm() {
        log.info("---------------------------------------");
        log.info("----Scan for Permission annotations----");
        log.info("---------------------------------------");
        Reflections reflections = new Reflections("com.example.shop.controller", new MethodAnnotationsScanner());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);
        methods.forEach(method -> {
            PreAuthorize perm = method.getAnnotation(PreAuthorize.class);
            String name = perm.value();
            if (name.contains("hasAuthority")) {
                String code = name.replace("hasAuthority('", "").replace("')", "");
                try {
                    String methodType = method.getDeclaringClass().getSimpleName();
                    String type = null;
                    if (methodType.contains("Controller"))
                        type = methodType.replace("Controller", "");
                    if (type != null) {
                        Permission permissionOpt = permissionService.getPermissionByName(code);
                        if (permissionOpt == null) {
                            Permission permission = new Permission();
                            if (code.equals("View Reference")) {
                                permission.setNameLt(code);
                                permission.setType("References");
                            } else {
                                permission.setNameLt(code);
                                permission.setType(type);
                            }
                            permissionService.savePermission(permission);
                            log.info("Found a permission '" + code + "'");
                        }
                    }
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public void startRole() {
        Role role = roleService.findByCode("ROLE_ADMIN");
        if (role == null) {
            role = new Role();
            role.setName("ADMIN");
            role.setCode("ROLE_ADMIN");
            roleService.createRole(role);
        }
        role.getPermissions().addAll(permissionService.findNotInRole(role.getId()));
        roleService.updateRole(role);

        Role roleU = roleService.findByCode("ROLE_USER");
        if (roleU == null) {
            roleU = new Role();
            roleU.setName("USER");
            roleU.setCode("ROLE_USER");
            roleService.createRole(roleU);
        }
    }

    public void startUser() {
        User user = userService.findByUsername("admin");
        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setStatus(referenceService.findByCode("ACTIVE"));
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(roleService.findByCode("ROLE_ADMIN"));
            userService.saveUser(user);
        }
        User userU = userService.findByUsername("user");
        if (userU == null) {
            userU = new User();
            userU.setUsername("user");
            userU.setStatus(referenceService.findByCode("ACTIVE"));
            userU.setPassword(passwordEncoder.encode("user"));
            userU.setRole(roleService.findByCode("ROLE_USER"));
            userService.saveUser(userU);
        }
    }

    private void initializeReferences() {
        try {
            for (Constants.ReferenceType type : Constants.ReferenceType.values()) {
                Reference parent = referenceService.parentByCode(type.name());
                if (parent == null) {
                    parent = new Reference();
                    parent.setCode(type.name());
                    parent.setNameUz(type.name());
                    parent.setNameLt(type.name());
                    parent.setNameRu(type.name());
                    parent.setNameEn(type.name());
                    referenceService.saveReference(parent);
                }
                if (type.names == null) continue;
                for (String name : type.names) {
                    Reference reference = referenceService.findByCode(name);
                    if (reference != null) continue;
                    reference = new Reference();
                    reference.setCode(name);
                    reference.setNameUz(name);
                    reference.setNameLt(name);
                    reference.setNameRu(name);
                    reference.setNameEn(name);
                    reference.setParent(parent);
                    referenceService.saveReference(reference);
                }
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }


}
