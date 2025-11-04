package com.pos.service.impl;

import com.pos.configuration.JwtProvider;
import com.pos.domain.UserRole;
import com.pos.exception.UserException;
import com.pos.modal.User;
import com.pos.payload.dto.AuthRequest;
import com.pos.payload.dto.UserDto;
import com.pos.payload.mapper.UserMapper;
import com.pos.payload.response.AuthResponse;
import com.pos.repository.UserRepository;
import com.pos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserImplementation customUserImplementation;
    private final UserMapper userMapper;

    @Override
    public AuthResponse signup(AuthRequest request) throws UserException {
        User user = userRepository.findByEmail(request.getEmail());
        if(user != null) {
            throw new UserException("Email already registered!");
        }

        if(request.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new UserException("Role admin is not allowed!");
        }

        User newUser = new User();

        userMapper.updateFromAuthRequest(request, newUser);

        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        if(newUser.getRole() == null) {
            newUser.setRole(UserRole.ROLE_USER);
        }

        newUser.setCreatedAt(LocalDate.now());
        newUser.setUpdatedAt(LocalDate.now());
        newUser.setLastLoginAt(LocalDate.now());

        userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        return AuthResponse.builder().message("Registered Successfully").jwt(jwt).user(userMapper.toDto(newUser)).build();
    }

    @Override
    public AuthResponse login(AuthRequest request) throws UserException {

        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String role = "";
        if (authorities != null && !authorities.isEmpty()) {
            role = authorities.iterator().next().getAuthority();
        }

        String jwt = jwtProvider.generateToken(authentication);

        User user = userRepository.findByEmail(email);
        user.setLastLoginAt(LocalDate.from(LocalDateTime.now()));
        userRepository.save(user);

        return AuthResponse.builder().jwt(jwt).message("Login Successfully").user(userMapper.toDto(user)).build();
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserImplementation.loadUserByUsername(email);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

}
