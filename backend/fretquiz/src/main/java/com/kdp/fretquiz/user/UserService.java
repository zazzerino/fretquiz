package com.kdp.fretquiz.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createAnonUser(String sessionId) {
        var user = User.of(sessionId);
        return userRepository.save(user);
    }

    @Transactional
    public User updateName(String sessionId, String newName) {
        var user = userRepository
                .findBySessionId(sessionId)
                .orElseThrow()
                .withName(newName);

        return userRepository.save(user);
    }

    @Transactional
    public void forgetUser(String sessionId) {
        userRepository.deleteBySessionId(sessionId);
    }
}
