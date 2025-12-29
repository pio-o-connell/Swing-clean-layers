package Concordia.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
        @Test
        void testGettersAndSetters() {
                User user = new User(1, 2, "username", "password");
                assertEquals(1, user.getUserId());
                assertEquals(2, user.getCompanyId());
                assertEquals("username", user.getUsername());
                assertEquals("password", user.getPassword());
                user.setUserId(10);
                user.setCompanyId(20);
                user.setPassword("newpass");
                assertEquals(10, user.getUserId());
                assertEquals(20, user.getCompanyId());
                assertEquals("newpass", user.getPassword());
        }
}

