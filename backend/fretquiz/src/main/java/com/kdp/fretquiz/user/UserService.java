package com.kdp.fretquiz.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User createUser(String sessionId) {
        var user = User.of(sessionId);
        return userRepo.save(user);
    }

    @Transactional
    public User updateName(String sessionId, String newName) {
        var user = userRepo
                .findBySessionId(sessionId)
                .orElseThrow()
                .withName(newName);

        return userRepo.save(user);
    }

    @Transactional
    public int deleteUser(String sessionId) {
        return userRepo.deleteBySessionId(sessionId);
    }

    public Optional<User> findUser(String sessionId) {
        return userRepo.findBySessionId(sessionId);
    }
}
