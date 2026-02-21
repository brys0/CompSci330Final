package org.uwgb.compsci330.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.server.Configuration;
import org.uwgb.compsci330.server.dto.request.LoginUserRequest;
import org.uwgb.compsci330.server.dto.request.UserDeleteRequest;
import org.uwgb.compsci330.server.dto.response.SafeUser;
import org.uwgb.compsci330.server.entity.User;
import org.uwgb.compsci330.server.dto.request.RegisterUserRequest;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.repository.UserRepository;
import org.uwgb.compsci330.server.security.JwtUtil;

import java.util.List;
import java.util.UUID;

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
                UUID.randomUUID().toString(),
                username,
                encoder.encode(userRequest.getPassword())
        );

        userRepository.save(newUser);

        return JwtUtil.generateToken(newUser.getId(), newUser.getUsername());
    }

    @Transactional
    public String login(LoginUserRequest loginRequest) {
        List<User> users = userRepository.findByUsername(loginRequest.getUsername());
        // Username wasn't found.
        if (users.isEmpty()) throw new UsernameOrPasswordIncorrectException();

        User user = users.getFirst();

        // Password was incorrect.
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) throw new UsernameOrPasswordIncorrectException();

        return JwtUtil.generateToken(user.getId(), user.getUsername());
    }


    @Transactional
    public SafeUser getMe(String token) {
        try {
            String userId = JwtUtil.getUserIdFromToken(token);
            List<User> user = userRepository.findUserById(userId);

            if (user.isEmpty()) throw new UnauthorizedException();

            return new SafeUser(user.getFirst());
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    @Transactional
    public void deleteUser(String token, UserDeleteRequest req) {
        try {
            String userId = JwtUtil.getUserIdFromToken(token);
            List<User> user = userRepository.findUserById(userId);

            if (user.isEmpty()) throw new UnauthorizedException();
            if (req.getPassword().isBlank()) throw new PasswordIncorrectForUserDeletionException();

            User usr = user.getFirst();

            if (!BCrypt.checkpw(req.getPassword(), usr.getPassword())) throw new PasswordIncorrectForUserDeletionException();
            
            userRepository.deleteById(usr.getId());
        } catch (RuntimeException e) {
            throw new UnauthorizedException();
        }
    }
}
