package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.DTO.UserDTO;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

/**
 * @ClassName UserInterface
 * @Description Interface for user operations, providing methods to manage users.
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/2
 * @Version 1.0
 */
public interface UserInterface {

    /**
     * Finds a user by username.
     *
     * @param name the username of the user
     * @return the user with the given username
     */
    User findByUserName(String name);

    /**
     * Finds a user by email.
     *
     * @param email the email of the user
     * @return the user with the given email
     */
    User findByEmail(String email);

    /**
     * Finds a user by phone.
     *
     * @param phone the phone number of the user
     * @return the user with the given phone number
     */
    User findByPhone(String phone);

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return a Response object containing the registered user
     */
    Response<UserDTO> registerUser(User user);

    /**
     * Logs in a user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return a Response object containing the logged-in user
     */
    Response<UserDTO> loginUser(String username, String password);

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update
     * @param updateUser the updated user object
     * @return a Response object containing the updated user
     */
    Response<User> updateUser(Long id, User updateUser);

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @return a Response object indicating the result of the deletion
     */
    Response<Void> deleteUser(Long id);

    /**
     * Retrieves the profile of a user by ID.
     *
     * @param id the ID of the user
     * @return a Response object containing the user's profile
     */
    Response<User> getUserProfile(Long id);

    /**
     * Searches for users by a keyword.
     *
     * @param keyword the keyword to search for
     * @return a Response object containing a list of users matching the keyword
     */
    Response<List<User>> searchUserByKeyword(String keyword);

    /**
     * Checks if a user exists by username, email, and phone.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param phone the phone number of the user
     * @return true if the user exists, false otherwise
     */
    boolean isUserExists(String username, String email, String phone);
}