package com.example.bookexchange.data.controller;

import com.example.bookexchange.data.dto.system.RoleDto;
import com.example.bookexchange.data.dto.system.UserDto;
import com.example.bookexchange.data.exception.RecordNotFoundException;
import com.example.bookexchange.data.model.system.Role;
import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.repository.system.UserRepository;
import com.example.bookexchange.data.security.jwt.JwtTokenProvider;
import com.example.bookexchange.data.service.reference.ReferenceService;
import com.example.bookexchange.data.service.system.EmailSenderService;
import com.example.bookexchange.data.service.system.RoleService;
import com.example.bookexchange.data.service.system.UserService;
import com.example.bookexchange.data.utility.Utils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/v.1/register")
@Configuration
public class RegistrationController {
    @Value("${spring.mail.username}")
    String email;
    @Value("${smsApi}")
    String smsApi;
    @Value("${smsAuth}")
    String auth;
    @Value("${botName}")
    String botName;
    @Value("${botToken}")
    String botToken;
    @Value("${channelId}")
    String channelId;
    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReferenceService referenceService;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationController(BCryptPasswordEncoder passwordEncoder,
                                  UserService userService,
                                  RoleService roleService,
                                  UserRepository userRepository,
                                  JwtTokenProvider jwtTokenProvider,
                                  ReferenceService referenceService,
                                  EmailSenderService emailSenderService) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.referenceService = referenceService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailSenderService = emailSenderService;
    }

    @Transactional
    @RequestMapping(value = "/send-message-phone", method = RequestMethod.POST)
    public ResponseEntity<String> sendMessagePhone(@RequestBody UserDto userDto) {
        if (userDto.getPhone() == null || userDto.getPhone().equals(""))
            return new ResponseEntity<>("Not found Phone number", HttpStatus.NOT_FOUND);
        if (userDto.getPhone().length() != 12 || !userDto.getPhone().startsWith("998"))
            return new ResponseEntity<>("Invalid Phone", BAD_REQUEST);
        String newToken = shortUUID().toString();
        User userDB = userRepository.findByUsername(userDto.getUsername());
        if (userDB != null) {
            userDB.setConfirmationToken(newToken);
            userDB.setPhone(userDto.getPhone());
            userDB.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
            userService.save(userDB);
        } else {
            User user = new User();
            user.setConfirmationToken(newToken);
            user.setPhone(userDto.getPhone());
//            user.setUsername(userDto.getUsername());
//            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
            user.setStatus(referenceService.findByCode("INACTIVE"));
            Role role = roleService.findByCode("ROLE_USER");
            if (role == null) throw new RecordNotFoundException("Role Not Found");
            user.setRole(role);
            userService.save(user);
        }
        JSONObject actual = new JSONObject();
        actual.put("mobile_phone", userDto.getPhone());
        actual.put("message", newToken);
        OkHttpClient client = Utils.getUnsafeOkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON, actual.toString());
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + auth)
                .url(smsApi)
                .post(body)
                .build();
        JSONObject json = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                json = new JSONObject(response.body().string());
            } else throw new RecordNotFoundException("Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (json == null) return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/send-message-phone/by-telegram", method = RequestMethod.POST)
    public ResponseEntity<String> sendMessagePhoneByTelegram(@RequestBody UserDto userDto) throws IOException {
        if (userDto.getPhone() == null || userDto.getPhone().equals(""))
            return new ResponseEntity<>("Not found Phone number", HttpStatus.NOT_FOUND);
        if (userDto.getPhone().length() != 12 || !userDto.getPhone().startsWith("998"))
            return new ResponseEntity<>("Invalid Phone", BAD_REQUEST);
        String newToken = shortUUID().toString();
        User userDB = userRepository.findByUsername(userDto.getUsername());
        if (userDB != null) {
            userDB.setConfirmationToken(newToken);
            userDB.setPhone(userDto.getPhone());
            userDB.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
            userService.save(userDB);
        } else {
            User user = new User();
            user.setConfirmationToken(newToken);
            user.setPhone(userDto.getPhone());
//            user.setUsername(userDto.getUsername());
//            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
            user.setStatus(referenceService.findByCode("INACTIVE"));
            Role role = roleService.findByCode("ROLE_USER");
            if (role == null) throw new RecordNotFoundException("Role Not Found");
            user.setRole(role);
            userService.save(user);
        }

        assert userDto != null;
        String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + channelId +
                "&text=" + userDto.getPhone() + " " + newToken;

        urlString = String.format(urlString, botToken, channelId, userDto.getPhone() + " " + newToken);

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        String response = sb.toString();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm-phone", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Map<String, Object>> validateResetPhone(@Valid @RequestParam(name = "phone") String phone, @RequestParam(name = "code") String confirmationCode) {
        if (phone == null || phone.equals(""))
            return new ResponseEntity("Not found Phone number", HttpStatus.NOT_FOUND);
        if (phone.length() != 12 || !phone.startsWith("998"))
            return new ResponseEntity("Invalid Phone", BAD_REQUEST);
        User userDB = userService.findByPhone(phone);
        if (userDB.getExpireDate().before(new Date()))
            return new ResponseEntity("Date has been Expired...", BAD_REQUEST);
        if (!userDB.getConfirmationToken().equals(confirmationCode))
            return new ResponseEntity("Invalid CODE...!", BAD_REQUEST);

        userDB.setStatus(referenceService.findByCode("ACTIVE"));
        userService.saveUser(userDB);

        String token = jwtTokenProvider.createToken(userDB.getPhone(), userDB.getRole());
        Role role = userDB.getRole();
        List<String> permissionsDto = new ArrayList<>();
        if (role.getPermissions() != null && role.getPermissions().size() > 0)
            role.getPermissions().forEach(permission -> permissionsDto.add(permission.getNameUz()));
        Map<String, Object> response = new HashMap<>();
        response.put("id", userDB.getId());
        response.put("token", token);
        response.put("phone", userDB.getPhone());
        response.put("role", new RoleDto(role));
        response.put("permissions", permissionsDto);
        return ResponseEntity.ok(response);
    }

    public static Integer shortUUID() {
        Random uuid = new Random();
        return uuid.nextInt(9000) + 1000;
    }

    @RequestMapping(value = "/validate-phone", method = RequestMethod.POST)
    public ResponseEntity<String> nextPage(@RequestBody UserDto userDto) {
        User user = userService.findByPhone(userDto.getPhone());
        if (user != null) {
            if (user.getPhone() != null && user.getStatus() == referenceService.findByCode("ACTIVE"))
                return new ResponseEntity<>("This phone already exists...", BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/send-message-email", method = RequestMethod.POST)
    public ResponseEntity<String> sendMessage(@RequestBody UserDto userDto) {
        String userEmail = userDto.getEmail();
        if (userEmail == null || userEmail.equals(""))
            return new ResponseEntity<>("Not found email", HttpStatus.NOT_FOUND);
        String newToken = shortUUID().toString();
        User userDB = userRepository.findByUsername(userDto.getUsername());
        if (userDB != null) {
            userDB.setConfirmationToken(newToken);
            userDB.setPhone(userDto.getEmail());
            userDB.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
            userService.save(userDB);
        } else {
            User user = new User();
            user.setConfirmationToken(newToken);
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getFirstName());
            user.setUsername(userDto.getLastName());
            user.setExpireDate(new Date((new Date()).getTime() + (1000 * 60 * 2)));
            user.setStatus(referenceService.findByCode("INACTIVE"));
            Role role = roleService.findByCode("ROLE_USER");
            if (role == null) throw new RecordNotFoundException("Role Not Found");
            user.setRole(role);
            userService.save(user);
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Complete Password Rest!");
        mailMessage.setFrom(email);
        mailMessage.setText("To complete the confirmation code here: " + newToken);
        emailSenderService.sendEmail(mailMessage);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm-email", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Map<String, Object>> validateResetEmail(@Valid @RequestParam(name = "email") String mail, @RequestParam(name = "code") String confirmationCode) {
        if (mail == null || mail.equals(""))
            throw new RecordNotFoundException("Not found Email");
        User userDB = userService.findByEmail(mail);
        if (userDB.getExpireDate().before(new Date()))
            return new ResponseEntity("Date has been Expired...", BAD_REQUEST);
        if (!userDB.getConfirmationToken().equals(confirmationCode))
            return new ResponseEntity("Invalid CODE...!", BAD_REQUEST);
        userDB.setStatus(referenceService.findByCode("ACTIVE"));
        userService.saveUser(userDB);

        String token = jwtTokenProvider.createToken(userDB.getPhone(), userDB.getRole());
        Role role = userDB.getRole();
        List<String> permissionsDto = new ArrayList<>();
        if (role.getPermissions() != null && role.getPermissions().size() > 0)
            role.getPermissions().forEach(permission -> permissionsDto.add(permission.getNameUz()));
        Map<String, Object> response = new HashMap<>();
        response.put("id", userDB.getId());
        response.put("token", token);
        response.put("phone", userDB.getPhone());
        response.put("role", new RoleDto(role));
        response.put("permissions", permissionsDto);
        response.put("message", "This user successfully registered");
        return ResponseEntity.ok(response);
    }
}
