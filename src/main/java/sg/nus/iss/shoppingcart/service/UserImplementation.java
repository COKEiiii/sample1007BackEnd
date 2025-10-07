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

@Service
public class UserImplementation implements UserInterface {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserImplementation(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Response<UserDTO> registerUser(User user) {
        if (isUserExists(user.getUsername(), user.getEmail(), user.getPhone())) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Username or email or phone already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCart(cartRepository.save(new Cart()));
        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getRole().name());

        return new Response<>(ResponseStatus.SUCCESS, "User registered successfully", userDTO);
    }

    @Override
    public Response<UserDTO> loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "User does not exist");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new Response<>(ResponseStatus.BAD_REQUEST, "Password mismatch");
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getRole().name());

        return new Response<>(ResponseStatus.SUCCESS, "Login Successfully", userDTO);
    }

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
                    return new Response<>(ResponseStatus.SUCCESS, "User updated successfully",
                            userRepository.save(existUser));
                })
                .orElseGet(() -> new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user"));
    }

    @Override
    public Response<Void> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user");
        }

        userRepository.deleteById(id);
        return new Response<>(ResponseStatus.SUCCESS, "User deleted successfully");
    }

    @Override
    public Response<User> getUserProfile(Long id) {
        return userRepository.findById(id)
                .map(user -> new Response<>(ResponseStatus.SUCCESS, "Successfully", user))
                .orElseGet(() -> new Response<>(ResponseStatus.NOT_FOUND, "Cannot find user"));
    }

    @Override
    public Response<List<User>> searchUserByKeyword(String keyword) {
        List<User> userList = userRepository.searchByKeyword(keyword);
        if (userList.isEmpty()) {
            return new Response<>(ResponseStatus.NOT_FOUND, "Cannot find any users");
        }
        return new Response<>(ResponseStatus.SUCCESS, "Successfully", userList);
    }

    @Override
    public boolean isUserExists(String username, String email, String phone) {
        return userRepository.findByUsername(username) != null ||
                userRepository.findByEmail(email) != null ||
                userRepository.findByPhone(phone) != null;
    }
}