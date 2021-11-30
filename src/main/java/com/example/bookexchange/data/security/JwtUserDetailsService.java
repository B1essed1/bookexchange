package com.example.bookexchange.data.security;

import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.security.jwt.JwtUser;
import com.example.bookexchange.data.security.jwt.JwtUserFactory;
import com.example.bookexchange.data.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Implementation of {@link UserDetailsService} interface for {@link JwtUser}.
 */

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Username not found in this holly shit code ");
        log.error("user-------------->", user);
        log.info("user-------------->", user);
        user.setLastUse(new Date(System.currentTimeMillis()));
        userService.updateUser(user);
        if (user == null) throw new UsernameNotFoundException("User with username: " + username + " not found");
        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
        return jwtUser;
    }
}
