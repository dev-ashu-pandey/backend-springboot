package com.seeder.user.exception;

public class InvalidUserCredentials extends EntityException {
    public InvalidUserCredentials(String message) {
        super(message);
    }
}
