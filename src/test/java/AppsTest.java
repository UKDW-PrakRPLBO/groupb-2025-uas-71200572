/**
 * Author: dendy
 * Date:10/06/2025
 * Time:15:15
 * Description:
 */

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.uas.data.User;
import org.uas.repository.UserRepository;
import org.uas.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.uas.util.SessionManager;

@TestMethodOrder(OrderAnnotation.class)
public class AppsTest {

    private static float totalScore = 0;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @BeforeEach
    public void setup() throws SQLException {
        userRepository = new UserRepository(DBConnectionManager.getConnection());
        sessionManager = SessionManager.getInstance();
    }

    @Test
    @Order(2)
    public void testSingletonSameInstance() {
        Connection instance1 = DBConnectionManager.getConnection();
        Connection instance2 = DBConnectionManager.getConnection();
        assertSame(instance1, instance2, "Instances should be the same (singleton)");
        totalScore += 15;
    }

    @Test
    @Order(3)
    public void testNoPublicConstructor() {
        boolean hasPublicConstructor = false;
        for (java.lang.reflect.Constructor<?> constructor : DBConnectionManager.class.getDeclaredConstructors()) {
            if (java.lang.reflect.Modifier.isPublic(constructor.getModifiers())) {
                hasPublicConstructor = true;
                break;
            }
        }
        assertFalse(hasPublicConstructor, "Should not have public constructors");
        totalScore += 10;
    }


    @Test
    @Order(4)
    public void testFindAllUsers() throws Exception {
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getUsername());
        totalScore += 10;
    }

    static Stream<Arguments> provideUserCredentials() {
        return Stream.of(
                Arguments.of("admin", "admin123", true),
                Arguments.of("wrong", "wrongpass", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserCredentials")
    @Order(5)
    public void testAuthenticateUser(String username, String password, boolean expectedResult) throws Exception {
        boolean result = userRepository.authenticateUser(username, password);
        assertEquals(expectedResult, result);
        totalScore += 7.5F;
    }

    @ParameterizedTest
    @CsvSource({
            "alice@ukdw.com, alice, alice123",
            "bob@ukdw.com, bob, bob456"
    })
    @Order(6)
    public void testInsertUser(String email, String username, String password) throws Exception {
        boolean inserted = userRepository.insertUser(email, username, password);
        assertTrue(inserted, "User should be inserted successfully");
        totalScore += 5;
    }

    @ParameterizedTest
    @CsvSource({
            "alice@ukdw.com, alice123, alice",
            "bob@ukdw.com, bob456, bob"
    })
    @Order(7)
    public void testUpdateUser(String email, String username, String password) throws Exception {
        boolean updated = userRepository.updateUser(email, username, password);
        assertTrue(updated);
        totalScore += 5;
    }

    @ParameterizedTest
    @CsvSource({
            "alice@ukdw.com",
            "bob@ukdw.com"
    })
    @Order(8)
    public void testDeleteUser(String email) throws Exception {
        boolean deleted = userRepository.deleteUser(email);
        assertTrue(deleted);
        totalScore += 5;
    }

    @Test
    @Order(9)
    void testSingletonInstance() {
        SessionManager anotherInstance = SessionManager.getInstance();
        assertSame(sessionManager, anotherInstance, "SessionManager should use singleton pattern");
        totalScore += 10;
    }

    @Test
    @Order(10)
    void testLogin() {
        sessionManager.login();
        assertTrue(sessionManager.isLoggedIn());
        totalScore += 5;
    }

    @Test
    @Order(11)
    void testLogout() {
        sessionManager.logout();
        assertFalse(sessionManager.isLoggedIn());
        totalScore += 5;
    }

    @AfterAll
    public static void printScore() {
        System.out.println("===== Scoring Summary =====");
        System.out.println("Total Score: " + totalScore + " / 100");
        if (totalScore == 100) {
            System.out.println("✅ Great job! all feature is correctly implemented.");
        } else {
            System.out.println("⚠️ Review your implementation. Some criteria were not met.");
        }
    }
}