package com.example.bookexchange.data.security.jwt;

import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.model.system.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of Factory Method for class {@link JwtUser}.
 */
public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()),
                user.getStatus().getCode().equals("ACTIVE"),
                user.getUpdated()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role userRole) {
        List<GrantedAuthority> collect = userRole.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getNameUz())).collect(Collectors.toList());
        collect.add(new SimpleGrantedAuthority(userRole.getCode()));
        return collect;
    }
}
