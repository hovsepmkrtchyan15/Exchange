package com.example.exchange.service;

import com.example.exchange.entity.RoleUser;
import com.example.exchange.entity.User;
import com.example.exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleUser(RoleUser.CLIENT);
        userRepository.save(user);
        log.info("user by email: {} saved with role {}", user.getEmail(), user.getRoleUser());
    }

}
