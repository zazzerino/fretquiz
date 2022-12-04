package com.kdp.fretquiz.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User createAnonymousUser(String sessionId) {
        var user = new User(sessionId, User.DEFAULT_NAME);
        userRepo.save(user);
        return user;
    }
}
