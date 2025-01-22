package sg.nus.iss.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nus.iss.shoppingcart.model.Payment;

/**
 * @ClassName PaymentRepository
 * @Description
 * @Author V_E
 * @StudentID A0307246H
 * @Date 2024/10/5
 * @Version 1.0
 */

/**
 * Repository interface for managing Payment entities.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds a Payment entity by its payment ID.
     *
     * @param paymentId the ID of the payment to find
     * @return the Payment entity with the specified payment ID
     */
    Payment findByPaymentId(int paymentId);
}