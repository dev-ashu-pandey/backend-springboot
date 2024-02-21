package com.seeder.user.controller;

import com.seeder.user.config.JwtService;
import com.seeder.user.dto.UserDTO;
import com.seeder.user.request.AuthRequest;
import com.seeder.user.request.UpdateUserRequest;
import com.seeder.user.request.UserRequest;
import com.seeder.user.service.IUserService;
import com.seeder.user.utils.Constant;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(Constant.BASE_URL)
@RestController
@Slf4j
public class UserController {

    private final IUserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(IUserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserRequest userRequest) {
        log.info(Constant.CREATE_USER_REQUEST_RECEIVED, userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        log.info(Constant.GET_USER_BY_EMAIL_REQUEST_RECEIVED, email);
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @PostMapping(Constant.LOGIN)
    public String loginAccount(@RequestBody AuthRequest auth) {
        log.info(Constant.LOGIN_REQUEST_RECEIVED, auth.getEmail());
        return jwtService.generateToken(auth.getEmail(), auth.getPassword());
    }

    @PatchMapping(Constant.ID)
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        log.info(Constant.UPDATE_USER_REQUEST_RECEIVED, id.toString());
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }
}
