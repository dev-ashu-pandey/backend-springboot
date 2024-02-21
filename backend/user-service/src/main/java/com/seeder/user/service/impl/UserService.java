package com.seeder.user.service.impl;

import com.seeder.user.dto.UserDTO;
import com.seeder.user.entity.User;
import com.seeder.user.exception.EntityAlreadyExistException;
import com.seeder.user.exception.EntityException;
import com.seeder.user.exception.EntityNotFoundException;
import com.seeder.user.exception.InvalidParameterException;
import com.seeder.user.repository.IUserRepository;
import com.seeder.user.request.UpdateUserRequest;
import com.seeder.user.request.UserRequest;
import com.seeder.user.service.IUserService;
import com.seeder.user.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    @Value("${INITIAL_CASHKICK_BALANCES}")
    private String INITIAL_CASHKICK_AMOUNT;

    @Autowired
    public UserService(IUserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserRequest userRequest) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
            if (existingUser.isPresent()) {
                throw new EntityAlreadyExistException(Constant.USER_ALREADY_EXISTS);
            }
            log.info(Constant.USER_CREATION_INFO, userRequest.getEmail());
            BigDecimal cashKickBalance = new BigDecimal(userRequest.getCashKickBalance().replace("$", "").replace(",", ""));
            if (cashKickBalance.compareTo(BigDecimal.valueOf(Integer.parseInt(INITIAL_CASHKICK_AMOUNT))) < 0) {
                throw new InvalidParameterException("Cash kick balance must be at least $10,000.00");
            }
            String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
            userRequest.setPassword(encodedPassword);
            User savedUser = userRepository.save(modelMapper.map(userRequest, User.class));
            log.info(Constant.USER_CREATED_SUCCESSFULLY_INFO, savedUser.getEmail());
            return modelMapper.map(savedUser, UserDTO.class);
        } catch (EntityException e) {
            log.error(Constant.ERROR_CREATING_USER, e.getMessage());
            throw new EntityException(e.getMessage());
        }
    }

    @Override
    public UserDTO getByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(Constant.USER_NOT_FOUND_BY_EMAIL + email));
            log.info(Constant.USER_RETRIEVED_BY_EMAIL_INFO, email);
            return modelMapper.map(user, UserDTO.class);
        } catch (EntityException e) {
            log.error(Constant.ERROR_RETRIEVING_USER_BY_EMAIL, e.getMessage());
            throw new EntityException(e.getMessage());
        }
    }

    @Override
    public UserDTO updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Constant.USER_NOT_FOUND_BY_ID + id));
            if (updateUserRequest.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
                if (passwordEncoder.matches(encodedPassword, user.getPassword())) {
                    throw new EntityException(Constant.PASSWORD_MATCHES_PREVIOUS_ERROR);
                }
                user.setPassword(encodedPassword);
            }
            if (updateUserRequest.getCashKickBalance() != null) {
                BigDecimal cashKickBalance = new BigDecimal(updateUserRequest.getCashKickBalance().replace("$", "").replace(",", ""));
                if (cashKickBalance.compareTo(BigDecimal.valueOf(10000)) < 0) {
                    throw new InvalidParameterException("Cash kick balance must be at least $10,000.00");
                }
                user.setCashKickBalance(updateUserRequest.getCashKickBalance());
                log.info(Constant.CASH_KICK_BALANCE_UPDATED, id, updateUserRequest.getCashKickBalance());
            }
            User updateUser = userRepository.save(user);
            log.info(Constant.USER_UPDATED_SUCCESSFULLY_INFO, id);
            return modelMapper.map(updateUser, UserDTO.class);
        } catch (EntityException e) {
            log.error(Constant.ERROR_UPDATING_USER, id, e.getMessage());
            throw new EntityException(e.getMessage());
        }
    }
}
