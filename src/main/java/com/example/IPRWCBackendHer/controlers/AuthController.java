package com.example.IPRWCBackendHer.controlers;

import com.example.IPRWCBackendHer.DAO.UserDao;
import com.example.IPRWCBackendHer.models.ApiResponse;
import com.example.IPRWCBackendHer.models.LoginCredentials;
import com.example.IPRWCBackendHer.models.User;
import com.example.IPRWCBackendHer.security.JWTUtil;
import com.example.IPRWCBackendHer.services.InvalidMailService;
import com.example.IPRWCBackendHer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    private final InvalidMailService invalidMailService;

    public AuthController(UserDao userDao, JWTUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder passwordEncoder, InvalidMailService invalidMailService) {
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.invalidMailService = invalidMailService;
    }

    @PostMapping("/register")
    public Object registerHandler(@RequestBody User user) {
        try {
            if (invalidMailService.patternMatches(user.getEmail())) {
                user.setRole(false);
                String encodedPass = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPass);
                userDao.saveToDatabase(user);
                return new ApiResponse(HttpStatus.ACCEPTED, jwtUtil.generateToken(user.getEmail()));
            } else {
                return new ApiResponse(HttpStatus.BAD_REQUEST, "Invalid email");
            }
        } catch (Exception e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Email already in use");
        }
    }

    @PostMapping("/login")
    public Object loginHandler(@RequestBody LoginCredentials body) {
        try {
            String email = body.getEmail();

            if (invalidMailService.patternMatches(email)) {
                UsernamePasswordAuthenticationToken authInputToken =
                        new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
                authManager.authenticate(authInputToken);

                Optional<User> user = Optional.ofNullable(userService.findUserByEmail(email));

                return new ApiResponse(HttpStatus.ACCEPTED, jwtUtil.generateToken(body.getEmail()));
            } else {
                return new ApiResponse(HttpStatus.BAD_REQUEST, "Invalid email");
            }
        } catch (AuthenticationException authExc) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED, "Invalid email/password");
        }
    }

    @GetMapping("/info")
    public ApiResponse getUserDetails() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ApiResponse(HttpStatus.ACCEPTED, userDao.findByEmail(email).get());
    }

    @GetMapping("/get")
    public ApiResponse getAllUsers() {
       return new ApiResponse(HttpStatus.ACCEPTED, userDao.getAllUsers());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse deleteUser(@PathVariable UUID id) {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userDao.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (this.userDao.isUserAdmin(user)) {
                userDao.deleteUserFromDatabase(id);
            } else {
                return new ApiResponse(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to delete products");
            }
        } else {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "User not found");
        }
        return new ApiResponse(HttpStatus.ACCEPTED, "You deleted some data!");
    }
}