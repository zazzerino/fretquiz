package kdp.fretquiz.user;

import java.lang.System.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;

    Logger logger = System.getLogger(UserService.class.getName());

    @Test
    void createAndFindAnonUser() {
        var user = userService.createUser("sessionId");
        logger.log(Logger.Level.INFO, "user created: {0}", user);
        var foundUser = userRepository.findById(user.id()).orElseThrow();
        assertEquals(user, foundUser);
    }

    @Test
    void updateName() {
        var user = userService.createUser("sessionId");
        assertEquals(User.DEFAULT_NAME, user.name());

        userService.updateName(user.sessionId(), "Alice");
        var foundUser = userRepository.findById(user.id()).orElseThrow();
        assertEquals("Alice", foundUser.name());
    }

    @Test
    void forgetUser() {
        var user = userService.createUser("sessionId");
        assertTrue(userRepository.existsById(user.id()));

        userService.deleteUser(user.sessionId());
        assertFalse(userRepository.existsById(user.id()));
    }
}