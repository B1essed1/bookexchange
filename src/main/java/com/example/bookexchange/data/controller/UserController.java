package com.example.bookexchange.data.controller;

import com.example.bookexchange.data.config.SpringSecurityAuditorAware;
import com.example.bookexchange.data.dto.system.UserDto;
import com.example.bookexchange.data.model.system.Pagination;
import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.security.jwt.JwtUser;
import com.example.bookexchange.data.service.reference.ReferenceService;
import com.example.bookexchange.data.service.system.RoleService;
import com.example.bookexchange.data.service.system.UserService;
import com.example.bookexchange.data.utility.UtilComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST controller user connected requestst.
 */

@RestController
@RequestMapping(value = "/api/v.1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final ReferenceService referenceService;
    private final UtilComponent utilComponent;
    private final BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('List Users')")
    public ResponseEntity<Map<String, Object>> listUsers(@RequestBody Pagination pagination) {
        Page<User> userPage = userService.findAll(pagination);
        Map<String, Object> map = new HashMap<>();
        if (userPage != null) {
            List<User> users = userPage.getContent();
            List<UserDto> userDto = new ArrayList<>(users.size());
            users.forEach(user -> userDto.add(new UserDto(user)));
            map.put("list", userDto);
            map.put("total", userPage.getTotalElements());
        } else {
            map.put("list", new ArrayList<>());
            map.put("total", 0);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-by-id", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('Get User')")
    public ResponseEntity<UserDto> userById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        if (user == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userDto.convertToUser();
        if (userDto.getRoleId() != null) user.setRole(roleService.getRoleById(userDto.getRoleId()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userService.saveUser(user);
        if (result == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST, HttpStatus.valueOf("Such username: " + userDto.getUsername() + " exist..."));
        return ResponseEntity.ok(new UserDto(user));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('Update User')")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userService.findById(userDto.getId());
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        userDto.convertToUser(user);
        if (userDto.getRoleId() != null) {
            user.setRole(roleService.getRoleById(userDto.getRoleId()));
        }
        userService.updateUser(user);
        return ResponseEntity.ok(new UserDto(user));
    }

    @RequestMapping(value = "/update-password", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('Update User')")
    public ResponseEntity<String> updateUserPass(@RequestBody Map<String, String> map) {
        String currPass = map.get("currentPassword");
        String newPass = map.get("newPassword");
        Optional<JwtUser> jwtUser = new SpringSecurityAuditorAware().getCurrentAuditor();
        if (jwtUser.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = userService.findById(jwtUser.get().getId());
        if (passwordEncoder.matches(currPass, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPass));
            userService.updateUser(user);
        } else return new ResponseEntity<>("Invalid Current Password...", HttpStatus.CONFLICT);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('Delete User')")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        user.setStatus(referenceService.findByCode("DELETED"));
        userService.updateUser(user);
        return ResponseEntity.ok("SUCCESSFUL Deleted...");
    }
}
