package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.model.DTO.UserDTO;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

public interface UserInterface {

    User findByUserName(String name);

    User findByEmail(String email);

    User findByPhone(String phone);

    Response<UserDTO> registerUser(User user);

    Response<UserDTO> loginUser(String username, String password);

    Response<User> updateUser(Long id, User updateUser);

    Response<Void> deleteUser(Long id);

    Response<User> getUserProfile(Long id);

    Response<List<User>> searchUserByKeyword(String keyword);

    boolean isUserExists(String username, String email, String phone);
}