package com.seeder.user.controller;

import com.seeder.user.config.JwtService;
import com.seeder.user.dto.UserDTO;
import com.seeder.user.request.AuthRequest;
import com.seeder.user.request.UpdateUserRequest;
import com.seeder.user.request.UserRequest;
import com.seeder.user.service.IUserService;
import com.seeder.user.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createUser_ValidUser_ReturnsCreatedUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setName("test");
        userRequest.setPassword("password");
        userRequest.setCashKickBalance("$1,000");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setName(userRequest.getName());
        userDTO.setEmail(userRequest.getEmail());
        userDTO.setName(userRequest.getName());

        when(userService.createUser(userRequest)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO.getName(), response.getBody().getName());
        assertEquals(userDTO.getId(), response.getBody().getId());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void getUserByEmail_ValidEmail_ReturnsUser() {
        String email = "test@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail(email);

        when(userService.getByEmail(email)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserByEmail(email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void loginAccount_ValidCredentials_ReturnsToken() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        String token = "generatedToken";
        when(jwtService.generateToken(authRequest.getEmail(), authRequest.getPassword())).thenReturn(token);

        String response = userController.loginAccount(authRequest);
        assertEquals(token, response);
    }

    @Test
    void updateUser_ValidIdAndRequest_ReturnsUpdatedUser() {
        UUID id = UUID.randomUUID();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setPassword("newPassword");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(id);
        updatedUserDTO.setEmail("test@example.com");

        when(userService.updateUser(id, updateUserRequest)).thenReturn(updatedUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(id, updateUserRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUserDTO, response.getBody());
    }
}
