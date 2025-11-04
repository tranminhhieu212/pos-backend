package com.pos.service;

import com.pos.exception.UserException;
import com.pos.payload.dto.AuthRequest;
import com.pos.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse signup(AuthRequest request) throws Exception, UserException;
    AuthResponse login(AuthRequest request) throws UserException;
}
