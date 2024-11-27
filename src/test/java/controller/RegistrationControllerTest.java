package controller;

import com.bookstore.controller.RegistrationController;
import com.bookstore.dto.LoginDTO;
import com.bookstore.dto.RegistrationDTO;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.ValidationException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    private RegistrationDTO validRegistration;
    private LoginDTO validLogin;
    private User user;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        validRegistration = new RegistrationDTO(
                "testuser",
                "password123",
                "test@example.com",
                "John",
                "Doe"
        );

        validLogin = new LoginDTO();
        // Using reflection to set private fields since LoginDTO doesn't have a constructor
        try {
            var usernameField = LoginDTO.class.getDeclaredField("username");
            var passwordField = LoginDTO.class.getDeclaredField("password");
            usernameField.setAccessible(true);
            passwordField.setAccessible(true);
            usernameField.set(validLogin, "testuser");
            passwordField.set(validLogin, "password123");
        } catch (Exception e) {
            fail("Failed to setup test data");
        }

        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.CUSTOMER);
    }

    @Test
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    /**
     * Tests successful user registration
     */
    @Test
    void testSuccessfulRegistration() {
        ResponseEntity<String> response = registrationController.RegisterUser(validRegistration);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Registration successful"));
        verify(userService).register(validRegistration, Role.CUSTOMER);
    }

    /**
     * Tests successful login with valid credentials
     */
    @Test
    void testSuccessfulLogin() {
        String token = "valid.jwt.token";
        when(userService.verify(any(LoginDTO.class))).thenReturn(token);

        ResponseEntity<?> response = registrationController.LoginUser(validLogin);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonMap("token", token), response.getBody());
    }

    /**
     * Tests failed login with invalid credentials
     */
    @Test
    void testFailedLogin() {
        when(userService.verify(any(LoginDTO.class))).thenReturn(null);

        ResponseEntity<?> response = registrationController.LoginUser(validLogin);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"error\": \"Authorization failed\"}", response.getBody());
    }

    /**
     * Tests retrieving logged-in user profile
     */
    @Test
    void testGetLoggedInUserProfile() {
        User result = registrationController.getLoggedInUserProfile(user);

        assertEquals(user, result);
    }

    /**
     * Tests registration validation
     */
    @Test
    void testRegistrationValidation() {
        RegistrationDTO invalidRegistration = new RegistrationDTO(
                "",  // invalid username
                "password123",
                "test@example.com",
                "John",
                "Doe"
        );

        doThrow(new ValidationException("Invalid registration data"))
                .when(userService)
                .register(eq(invalidRegistration), any(Role.class));

        assertThrows(ValidationException.class, () -> {
            registrationController.RegisterUser(invalidRegistration);
        });
    }

    /**
     * Tests login with invalid data
     */
    @Test
    void testLoginValidation() {
        when(userService.verify(any(LoginDTO.class))).thenReturn(null);

        ResponseEntity<?> response = registrationController.LoginUser(validLogin);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Tests null user profile retrieval
     */
    @Test
    void testNullUserProfile() {
        User result = registrationController.getLoggedInUserProfile(null);

        assertNull(result);
    }

    /**
     * Tests registration with service error
     */
    @Test
    void testRegistrationServiceError() {
        doThrow(new RuntimeException("Service error"))
                .when(userService)
                .register(any(RegistrationDTO.class), any(Role.class));

        assertThrows(RuntimeException.class, () -> {
            registrationController.RegisterUser(validRegistration);
        });
    }

    /**
     * Tests login with service error
     */
    @Test
    void testLoginServiceError() {
        when(userService.verify(any(LoginDTO.class)))
                .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> {
            registrationController.LoginUser(validLogin);
        });
    }
}