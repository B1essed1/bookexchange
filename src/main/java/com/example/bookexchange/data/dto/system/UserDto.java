package com.example.bookexchange.data.dto.system;

import com.example.bookexchange.data.model.enums.Status;
import com.example.bookexchange.data.model.system.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO class for user requests by ROLE_USER
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Tashkent")
    private Date birthday;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Status status;
    private Long roleId;
    private String roleName;

    public UserDto(User user) {
        if (user.getId() != null)
            setId(user.getId());
        setBirthday(user.getBirthday());
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        setPhone(user.getPhone());

        if (user.getRole() != null) {
            setRoleId(user.getRole().getId());
            setRoleName(user.getRole().getName());
        }
    }

    public User convertToUser() {
        User user = new User();
        return convertToUser(user);
    }

    public User convertToUser(User user) {
        if (id != null)
            user.setId(id);
        user.setBirthday(birthday);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        return user;
    }
}
