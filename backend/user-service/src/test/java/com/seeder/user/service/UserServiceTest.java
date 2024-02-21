package com.seeder.user.service;

import com.seeder.user.dto.UserDTO;
import com.seeder.user.entity.User;
import com.seeder.user.exception.EntityAlreadyExistException;
import com.seeder.user.exception.EntityException;
import com.seeder.user.exception.EntityNotFoundException;
import com.seeder.user.repository.IUserRepository;
import com.seeder.user.request.UpdateUserRequest;
import com.seeder.user.request.UserRequest;
import com.seeder.user.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User user;
    private UpdateUserRequest updateUserRequest;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("hashedPassword@123");
        userRequest.setCashKickBalance("$100,00");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(user.getPassword());
        user.setCashKickBalance(userRequest.getCashKickBalance());


        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setPassword("newPassword@123");
        updateUserRequest.setCashKickBalance(user.getCashKickBalance());

        userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCashKickBalance(user.getCashKickBalance());


    }

    @Test
    void createUser_ValidUser_ReturnsUserDTO() {

        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(user);

        UserDTO createdUser = userService.createUser(userRequest);
        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getCashKickBalance(), createdUser.getCashKickBalance());

    }

    @Test
    void createUser_UserAlreadyExists_ThrowsEntityAlreadyExistException() {

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void getByEmail_ValidEmail_ReturnsUserDTO() {
        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.getByEmail(userRequest.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void createUser_EntityExceptionThrown() {
        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenThrow(new EntityException("Could not save user"));

        assertThrows(EntityException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void getByEmail_UserNotFound_ThrowsEntityNotFoundException() {
        String email = userRequest.getEmail();
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    void getByEmail_ThrowsEntityException() {

        String email = userRequest.getEmail();
        when(userRepository.findByEmail(userRequest.getEmail())).thenThrow(new EntityException("any exception"));

        assertThrows(EntityException.class, () -> userService.getByEmail(email));
    }
    @Test
    void updateUser_PasswordProvided_PasswordUpdated() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setPassword("newPassword@123");

        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPassword("oldPassword");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updateUserRequest.getPassword())).thenReturn("hashedNewPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO updatedUser = userService.updateUser(userId, updateUserRequest);

        assertNotNull(updatedUser);
        assertEquals("hashedNewPassword", user.getPassword());
    }

    @Test
    void updateUser_PasswordNotProvided_CashKickBalanceUpdated() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setCashKickBalance("$200.00");
        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setCashKickBalance(updateUserRequest.getCashKickBalance());

        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setId(newUser.getId());
        newUserDTO.setCashKickBalance(updateUserRequest.getCashKickBalance());

        when(modelMapper.map(userRequest, User.class)).thenReturn(newUser);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(newUserDTO);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(newUser));

        UserDTO updatedUser = userService.updateUser(newUser.getId(), updateUserRequest);

        assertNotNull(updatedUser);
        assertEquals(updateUserRequest.getCashKickBalance(), updatedUser.getCashKickBalance());
    }

    @Test
    void updateUser_PasswordDoesNotMatchPreviousPassword_PasswordUpdated() {

        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(updateUserRequest.getPassword(), user.getPassword())).thenReturn(false);

        UserDTO updatedUser = userService.updateUser(user.getId(), updateUserRequest);

        assertNotNull(updatedUser);
        assertEquals(updateUserRequest.getCashKickBalance(), updatedUser.getCashKickBalance());
    }
    @Test
    void updateUser_UserNotFound_ThrowsEntityNotFoundException() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setPassword("newPassword@123");

        UUID invalidUserId = UUID.randomUUID();

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(invalidUserId, updateUserRequest));
    }

    @Test
    void updateUser_PasswordMatchesPreviousPassword_ThrowsEntityException() {
        UUID id = user.getId();

        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        assertThrows(EntityException.class, () -> userService.updateUser(id, updateUserRequest));
    }
}
