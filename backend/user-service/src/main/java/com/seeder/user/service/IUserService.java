package com.seeder.user.service;

import com.seeder.user.dto.UserDTO;
import com.seeder.user.request.UpdateUserRequest;
import com.seeder.user.request.UserRequest;

import java.util.UUID;

public interface IUserService {

    UserDTO createUser(UserRequest userRequest);
    UserDTO getByEmail(String email);
    UserDTO updateUser(UUID id, UpdateUserRequest updateUserRequest);
}
