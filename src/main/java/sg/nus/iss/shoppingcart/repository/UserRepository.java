package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.User;

import java.util.List;

/**
 * @ClassName UserRepository
 * @Description Repository interface for User entity, providing CRUD operations and custom queries.
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2024/10/2
 * @Version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user
     * @return the user with the given username
     */
    User findByUsername(String username);

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
     * Fuzzy query to search for users by username, email, or phone.
     *
     * @param keyword the keyword to search for in username, email, or phone
     * @return a list of users matching the search criteria
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.phone LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}