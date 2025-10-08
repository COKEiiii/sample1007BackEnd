package sg.nus.iss.shoppingcart;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sg.nus.iss.shoppingcart.controller.UserController;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.UserInterface;
import sg.nus.iss.shoppingcart.model.DTO.UserDTO;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.utils.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserInterface userInterface;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_HappyPath() {
        User user = new User();
        UserDTO userDTO = new UserDTO((long) 1, "testUser", "USER");
        Response<UserDTO> expectedResponse = new Response<>(ResponseStatus.SUCCESS, "Registration successful", userDTO);

        when(userInterface.registerUser(user)).thenReturn(expectedResponse);
        ResponseEntity<Response<UserDTO>> response = userController.registerUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterUser_Exception() {
        User user = new User();

        when(userInterface.registerUser(user)).thenThrow(new RuntimeException("Database error"));
        ResponseEntity<Response<UserDTO>> response = userController.registerUser(user);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLoginUser_HappyPath() {
        User user = new User();
        UserDTO userDTO = new UserDTO((long) 1, "testUser", "USER");
        Response<UserDTO> expectedResponse = new Response<>(ResponseStatus.SUCCESS, "Login successful", userDTO);

        when(userInterface.loginUser(user.getUsername(), user.getPassword())).thenReturn(expectedResponse);
        HttpSession mockSession = new MockHttpSession();
        ResponseEntity<Response<UserDTO>> response = userController.loginUser(user, mockSession);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginUser_Exception() {
        User user = new User();
        HttpSession mockSession = new MockHttpSession();
        when(userInterface.loginUser(user.getUsername(), user.getPassword()))
                .thenThrow(new RuntimeException("Login failed"));
        ResponseEntity<Response<UserDTO>> response = userController.loginUser(user, mockSession);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDisplayUser_HappyPath() {
        Long userId = 1L;
        User user = new User();
        Response<User> expectedResponse = new Response<>(ResponseStatus.SUCCESS,
                "User information retrieved successfully", user);

        when(userInterface.getUserProfile(userId)).thenReturn(expectedResponse);
        ResponseEntity<Response<User>> response = userController.displayUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDisplayUser_Exception() {
        Long userId = 1L;

        when(userInterface.getUserProfile(userId)).thenThrow(new RuntimeException("User not found"));
        ResponseEntity<Response<User>> response = userController.displayUser(userId);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchUserByKeyword_HappyPath() {
        String keyword = "test";
        User user = new User();
        Response<List<User>> expectedResponse = new Response<>(ResponseStatus.SUCCESS, "Search successful",
                Collections.singletonList(user));

        when(userInterface.searchUserByKeyword(keyword)).thenReturn(expectedResponse);
        ResponseEntity<Response<List<User>>> response = userController.searchUserByKeyword(keyword);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchUserByKeyword_Exception() {
        String keyword = "test";

        when(userInterface.searchUserByKeyword(keyword)).thenThrow(new RuntimeException("Search failed"));
        ResponseEntity<Response<List<User>>> response = userController.searchUserByKeyword(keyword);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
