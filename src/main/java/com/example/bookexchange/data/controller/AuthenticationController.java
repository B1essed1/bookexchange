package com.example.bookexchange.data.controller;

import com.example.bookexchange.data.dto.AuthenticationRequestDto;
import com.example.bookexchange.data.dto.system.RoleDto;
import com.example.bookexchange.data.exception.RecordNotFoundException;
import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.security.jwt.JwtTokenProvider;
import com.example.bookexchange.data.service.system.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/v.1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/auth-payload")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationRequestDto requestDto) {
        User user = null;
        if (requestDto.getSignedData() == null || requestDto.getSignedData().equals("")) {
            try {
                String username = requestDto.getUsername();
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
                user = userService.findByUsername(username);
            } catch (AuthenticationException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        Role role = user.getRole();
        List<String> permissionsDto = new ArrayList<>();
        if (role.getPermissions() != null && role.getPermissions().size() > 0)
            role.getPermissions().forEach(permission -> permissionsDto.add(permission.getNameUz()));
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("role", new RoleDto(role));
        response.put("permissions", permissionsDto);
        return ResponseEntity.ok(response);
    }
}
