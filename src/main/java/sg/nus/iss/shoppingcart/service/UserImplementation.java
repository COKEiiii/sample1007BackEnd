package sg.nus.iss.shoppingcart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.UserInterface;
import sg.nus.iss.shoppingcart.model.Cart;
import sg.nus.iss.shoppingcart.model.DTO.UserDTO;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.repository.CartRepository;
import sg.nus.iss.shoppingcart.repository.UserRepository;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

/**
 * @ClassName UserImplementation
 * @Description Implementation of the UserInterface, providing services related to user registration, login, update, and deletion.
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/2
 * @Version 1.0
 */
@Service
public class UserImplementation implements UserInterface {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    /**
     * Constructor for UserImplementation.
     *
     * @param userRepository the repository for users
     * @param passwordEncoder the password encoder
     * @param cartRepository the repository for carts
     */
    public UserImplementation(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the found user object
     */
    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by email.
     *
     * @param email the email address
     * @return the found user object
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by phone number.
     *
     * @param phone the phone number
     * @return the found user object
     */
    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    /**
     * Registers a new user.
     *
     * @param user the user object to register
     * @return a response object with the registration result
     */
    @Override
    public Response<UserDTO> registerUser(User user) {
        if (isUserExists(user.getUsername(), user.getEmail(), user.getPhone())) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Username or email or phone already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCart(cartRepository.save(new Cart()));
        User savedUser = userRepository.save(user);

        // translate to DTO
        UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getRole().name());

        return new Response<>(ResponseStatus.SUCCESS, "User registered successfully", userDTO);
    }

    /**
     * Logs in a user.
     *
     * @param username the username
     * @param password the password
     * @return a response object with the login result
     */
    @Override
    public Response<UserDTO> loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "User does not exist");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Password mismatch");
        }

        // translate to DTO
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getRole().name());

        return new Response<>(ResponseStatus.SUCCESS, "Login Successfully", userDTO);
    }

    /**
     * Updates user information.
     *
     * @param id the user ID
     * @param updateUser the updated user object
     * @return a response object with the update result
     */
    @Override
    public Response<User> updateUser(Long id, User updateUser) {
        return userRepository.findById(id)
            .map(existUser -> {
                existUser.setUsername(updateUser.getUsername());
                existUser.setEmail(updateUser.getEmail());
                existUser.setPhone(updateUser.getPhone());
                existUser.setAddress(updateUser.getAddress());
                existUser.setFirstName(updateUser.getFirstName());
                existUser.setLastName(updateUser.getLastName());
                return new Response<>(ResponseStatus.SUCCESS, "User updated successfully", userRepository.save(existUser));
            })
            .orElseGet(() -> new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user"));
    }

    /**
     * Deletes a user.
     *
     * @param id the user ID
     * @return a response object with the deletion result
     */
    @Override
    public Response<Void> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user");
        }

        userRepository.deleteById(id);
        return new Response<>(ResponseStatus.SUCCESS, "User deleted successfully");
    }

    /**
     * Retrieves user profile.
     *
     * @param id the user ID
     * @return a response object with the user profile
     */
    @Override
    public Response<User> getUserProfile(Long id) {
        return userRepository.findById(id)
            .map(user -> new Response<>(ResponseStatus.SUCCESS, "Successfully", user))
            .orElseGet(() -> new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user"));
    }

    /**
     * Searches for users by keyword.
     *
     * @param keyword the search keyword
     * @return a response object with the search result
     */
    @Override
    public Response<List<User>> searchUserByKeyword(String keyword) {
        List<User> userList = userRepository.searchByKeyword(keyword);
        if (userList.isEmpty()) {
            return new Response<>(ResponseStatus.NOT_FOUND, "Cannot find any users");
        }
        return new Response<>(ResponseStatus.SUCCESS, "Successfully", userList);
    }

    /**
     * Checks if a user exists.
     *
     * @param username the username
     * @param email the email address
     * @param phone the phone number
     * @return a boolean indicating if the user exists
     */
    @Override
    public boolean isUserExists(String username, String email, String phone) {
        return userRepository.findByUsername(username) != null ||
               userRepository.findByEmail(email) != null ||
               userRepository.findByPhone(phone) != null;
    }
}