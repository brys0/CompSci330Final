package org.uwgb.compsci330.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.server.Configuration;
import org.uwgb.compsci330.server.annotation.SensitiveApi;
import org.uwgb.compsci330.server.dto.request.LoginUserRequest;
import org.uwgb.compsci330.server.dto.request.UserDeleteRequest;
import org.uwgb.compsci330.server.dto.response.SafeUser;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.dto.request.RegisterUserRequest;
import org.uwgb.compsci330.server.entity.user.UserStatus;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.repository.UserRepository;
import org.uwgb.compsci330.server.security.JwtUtil;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public String register(RegisterUserRequest userRequest) {
        final String username = userRequest.getUsername();
        final int usernameLen = userRequest.getUsername().length();

        // Username is too short.
        if (usernameLen < Configuration.MIN_USERNAME_LENGTH) {
            throw new UsernameTooShortException(username);
        } else if (usernameLen > Configuration.MAX_USERNAME_LENGTH) {  // Username is too long.
            throw new UsernameTooLongException(username);
        }

        // Check if user with that username already exists, if it does escape early.
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserAlreadyExistsException(userRequest.getUsername());
        }
        // Next check password len
        if (userRequest.getPassword().length() < Configuration.MIN_PASSWORD_LENGTH) throw new PasswordTooShortException();

        User newUser = new User(
                username,
                encoder.encode(userRequest.getPassword())
        );

        userRepository.save(newUser);

        return JwtUtil.generateToken(newUser.getId());
    }

    public String login(LoginUserRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(UsernameOrPasswordIncorrectException::new);

        // Password was incorrect.
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) throw new UsernameOrPasswordIncorrectException();

        return JwtUtil.generateToken(user.getId());
    }


    public SafeUser getMe(String token) {
        try {
            String userId = JwtUtil.getUserIdFromToken(token);
            User user = userRepository.findUserById(userId)
                    .orElseThrow(UnauthorizedException::new);

            return new SafeUser(user);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    public void deleteUser(String token, UserDeleteRequest req) {
        try {
            String userId = JwtUtil.getUserIdFromToken(token);
            User user = userRepository.findUserById(userId)
                    .orElseThrow(UnauthorizedException::new);

            if (req.getPassword().isBlank()) throw new PasswordIncorrectForUserDeletionException();

            if (!BCrypt.checkpw(req.getPassword(), user.getPassword())) throw new PasswordIncorrectForUserDeletionException();
            
            userRepository.deleteById(user.getId());
        } catch (RuntimeException e) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    @SensitiveApi
    public void setUserStatus(String userId, UserStatus status) {
        userRepository.setUserStatus(userId, status);
    }
}
