package sg.nus.iss.shoppingcart.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.shoppingcart.enums.ResponseStatus;
import sg.nus.iss.shoppingcart.interfacemethods.UserInterface;
import sg.nus.iss.shoppingcart.model.DTO.UserDTO;
import sg.nus.iss.shoppingcart.model.User;
import sg.nus.iss.shoppingcart.utils.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
            return ResponseEntity.status(500)
                    .body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "注册失败：" + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserDTO>> loginUser(@RequestBody User user, HttpSession session) {
        try {
            Response<UserDTO> response = userInterface.loginUser(user.getUsername(), user.getPassword());
            if (response.getStatusCode() == 200) {
                logger.info("登录成功，准备设置认证信息，username={}", user.getUsername());
                // 1. 从数据库获取完整用户信息（包含角色等权限信息）
                User fullUser = userInterface.findByUserName(user.getUsername());
                if (fullUser == null) {
                    logger.error("fullUser为null！无法获取用户信息，username={}", user.getUsername());
                    return ResponseEntity.status(401)
                            .body(new Response<>(ResponseStatus.UNAUTHORIZED, "User not found", null));
                }
                logger.info("成功获取fullUser，userId={}", fullUser.getId());

                // 2. 构建Spring Security认证对象（包含用户名、权限）
                // 假设User类有getAuthorities()方法返回权限列表（如角色）
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        fullUser.getUsername(),
                        null,
                        fullUser.getAuthorities() // 需确保User类实现了权限获取逻辑
                );
                logger.debug("构建的认证对象：{}", authentication);
                // 3. 将认证信息设置到SecurityContext，并绑定到会话
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

                // 4. 显式将SecurityContext保存到会话（确保持久化）
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

                logger.info("认证信息已设置到SecurityContext和会话，username={}", fullUser.getUsername());
                session.setAttribute("user", response.getData());
            }
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            logger.error("登录异常", e);
            return ResponseEntity.status(500)
                    .body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Failed Login：" + e.getMessage(), null));
        }
    }

    @GetMapping("/session")
    public ResponseEntity<Response<Map<String, Object>>> getSessionUser(HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("user");
        Map<String, Object> responseData = new HashMap<>();

        if (sessionUser != null) {
            responseData.put("isLoggedIn", true);
            responseData.put("user", sessionUser);
            return ResponseEntity.ok(new Response<>(ResponseStatus.SUCCESS, "User is logged in", responseData));
        } else {
            responseData.put("isLoggedIn", false);
            responseData.put("user", null);
            // 返回200而不是401，让前端处理登录状态
            return ResponseEntity.ok(new Response<>(ResponseStatus.SUCCESS, "No user session found", responseData));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new Response<>(ResponseStatus.SUCCESS, "Successfully logged out", null));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Response<User>> displayUser(@PathVariable Long id) {
        try {
            Response<User> response = userInterface.getUserProfile(id);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get information：" + e.getMessage(), null));
        }
    }

    //
    @GetMapping("/search")
    public ResponseEntity<Response<List<User>>> searchUserByKeyword(@RequestParam("keyword") String keyword) {
        try {
            Response<List<User>> response = userInterface.searchUserByKeyword(keyword);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Search No one：" + e.getMessage(), null));
        }
    }
}
