package com.pos.service;

import com.pos.exception.UserException;
import com.pos.modal.User;

import java.util.List;

public interface UserService {

    User getUserFromJwtToken(String token) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(Long id) throws UserException;
    List<User> getAllUsers();
}
