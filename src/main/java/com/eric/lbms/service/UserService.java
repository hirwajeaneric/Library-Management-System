package com.eric.lbms.service;

import com.eric.lbms.model.User;
import com.eric.lbms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        log.info("Creating user: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}