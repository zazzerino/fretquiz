package com.kdp.fretquiz.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String sessionId) {
        var user = User.of(sessionId);
        return userRepository.save(user);
    }

    @Transactional
    public User updateName(String sessionId, String newName) {
        var user = userRepository.findBySessionId(sessionId)
                           .map(u -> u.withName(newName))
                           .orElseThrow();

        return userRepository.save(user);
    }

    @Transactional
    public int deleteUser(String sessionId) {
        return userRepository.deleteBySessionId(sessionId);
    }

    public Optional<User> findUser(String sessionId) {
        return userRepository.findBySessionId(sessionId);
    }
}
