package sg.nus.iss.shoppingcart.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.UserInterface;
import sg.nus.iss.shoppingcart.model.DTO.UserDTO;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.List;

/**
 * @ClassName UserController
 * @Description Controller for handling user-related operations.
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/2
 * @Version 1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserInterface userInterface;

    public UserController(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @PostMapping("/register")
    public ResponseEntity<Response<UserDTO>> registerUser(@RequestBody User user) {
        try {
            Response<UserDTO> response = userInterface.registerUser(user);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "注册失败：" + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserDTO>> loginUser(@RequestBody User user, HttpSession session) {
        try {
            Response<UserDTO> response = userInterface.loginUser(user.getUsername(), user.getPassword());
            // 如果登录成功，存储用户信息到 session
            if (response.getStatusCode() == 200) {
                session.setAttribute("user", response.getData());
            }
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed Login：" + e.getMessage(), null));
        }
    }

    @GetMapping("/session")
    public ResponseEntity<Response<UserDTO>> getSessionUser(HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("user");
        if (sessionUser != null) {
            return ResponseEntity.ok(new Response<>(ResponseStatus.SUCCESS, "User is logged in", sessionUser));
        } else {
            return ResponseEntity.status(401).body(new Response<>(ResponseStatus.UNAUTHORIZED, "No user session found", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new Response<>(ResponseStatus.SUCCESS, "Successfully logged out", null));
    }

    // @PutMapping("/update/{id}")
    // public ResponseEntity<Response<User>> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
    //     try {
    //         Response<User> response = userInterface.updateUser(id, updateUser);
    //         return ResponseEntity.status(response.getStatusCode()).body(response);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed Update：" + e.getMessage(), null));
    //     }
    // }
    //
    // @DeleteMapping("/delete/{id}")
    // public ResponseEntity<Response<Void>> deleteUser(@PathVariable Long id) {
    //     try {
    //         Response<Void> response = userInterface.deleteUser(id);
    //         return ResponseEntity.status(response.getStatusCode()).body(response);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed Delete：" + e.getMessage(), null));
    //     }
    // }
    //
     @GetMapping("/profile/{id}")
     public ResponseEntity<Response<User>> displayUser(@PathVariable Long id) {
         try {
             Response<User> response = userInterface.getUserProfile(id);
             return ResponseEntity.status(response.getStatusCode()).body(response);
         } catch (Exception e) {
             return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed to get information：" + e.getMessage(), null));
         }
     }
    //
     @GetMapping("/search")
     public ResponseEntity<Response<List<User>>> searchUserByKeyword(@RequestParam("keyword") String keyword) {
         try {
             Response<List<User>> response = userInterface.searchUserByKeyword(keyword);
             return ResponseEntity.status(response.getStatusCode()).body(response);
         } catch (Exception e) {
             return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Search No one：" + e.getMessage(), null));
         }
     }
}
