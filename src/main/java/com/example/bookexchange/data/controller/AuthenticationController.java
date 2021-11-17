package com.example.bookexchange.data.controller;

import com.example.bookexchange.data.dto.AuthenticationRequestDto;
import com.example.bookexchange.data.dto.system.RoleDto;
import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.repository.system.UserRepository;
import com.example.bookexchange.data.security.jwt.JwtTokenProvider;
import com.example.bookexchange.data.service.system.EmailSenderService;
import com.example.bookexchange.data.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.example.bookexchange.data.controller.RegistrationController.shortUUID;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/v.1/login")
public class AuthenticationController {

    @Value("${spring.mail.username}")
    String email;

    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, EmailSenderService emailSenderService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.emailSenderService = emailSenderService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
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

    @PostMapping("/auth-payload/by-email")
    public ResponseEntity<String> loginByEmail(@RequestBody AuthenticationRequestDto requestDto) {
        User user = userService.findByEmail(requestDto.getEmail());
        if (user == null) return new ResponseEntity("This email address is not registered!", HttpStatus.BAD_REQUEST);

        String newToken = shortUUID().toString();
        user.setConfirmationToken(newToken);
        user.setEmail(requestDto.getEmail());
        user.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
        userService.save(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(requestDto.getEmail());
        mailMessage.setSubject("Complete Password Rest!");
        mailMessage.setFrom(email);
        mailMessage.setText("To complete the confirmation code here: " + newToken);
        emailSenderService.sendEmail(mailMessage);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
