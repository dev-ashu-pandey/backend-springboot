package com.seeder.user.utils;

public class Constant {
    public static final String BASE_URL = "/api/v1/users";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_CREATION_INFO = "Creating user with email: {}";
    public static final String USER_CREATED_SUCCESSFULLY_INFO = "User created successfully with email: {}";
    public static final String ERROR_CREATING_USER = "Error creating user: {}";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email ";
    public static final String USER_RETRIEVED_BY_EMAIL_INFO = "Retrieved user by email: {}";
    public static final String ERROR_RETRIEVING_USER_BY_EMAIL = "Error retrieving user by email: {}";
    public static final String USER_NOT_FOUND_BY_ID = "User not found with id ";
    public static final String PASSWORD_MATCHES_PREVIOUS_ERROR = "Please enter a password which does not match the previous password";
    public static final String CASH_KICK_BALANCE_UPDATED = "Updated cash kick balance for user with id {}: {}";
    public static final String USER_UPDATED_SUCCESSFULLY_INFO = "User updated successfully with id: {}";
    public static final String ERROR_UPDATING_USER = "Error updating user with id {}: {}";
    public static final String LOGIN = "/login";
    public static final String ID = "/{id}";
    public static final String VALID_EMAIL = "Enter a valid Email";
    public static final String EMAIL_NOT_EMPTY = "Email should not be empty";
    public static final String PASSWORD_NOT_EMPTY = "Password should not be Empty";
    public static final String CASHKICK_NOT_EMPTY = "Cashkick should not be Empty";
    public static final String NAME_NOT_EMPTY = "Name should not be Empty";
    public static final String NAME_NOT_NUL = "Name is a required field";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$";
    public static final String PASSWORD_MESSAGE = "Password must contain at least one capital letter, one digit, one special character and minimum 8 characters";
    public static final String CREATE_USER_REQUEST_RECEIVED = "Received request to create user with email: {}";
    public static final String GET_USER_BY_EMAIL_REQUEST_RECEIVED = "Received request to get user by email: {}";
    public static final String LOGIN_REQUEST_RECEIVED = "Received request to login user with email: {}";
    public static final String UPDATE_USER_REQUEST_RECEIVED = "Received request to update user with id: {}";
}
