package com.moje_wydatki;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SessionControllerTest {

    @Test
    public void testSingletonInstance() {
        SessionController instance1 = SessionController.getInstance();
        SessionController instance2 = SessionController.getInstance();
        assertSame(instance1, instance2, "Instancje powinny być takie same (singleton)");
    }

    @Test
    public void testSetAndGetUserId() {
        SessionController session = SessionController.getInstance();
        session.setUserId(42);
        assertEquals(42, session.getUserId(), "UserId powinno być 42");
    }
}
