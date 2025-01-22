package sg.nus.iss.shoppingcart.model.DTO;

import sg.nus.iss.shoppingcart.model.CustomerOrder;

/**
 * A Data Transfer Object (DTO) that encapsulates a customer order and a payment method.
 *
 * @param order the customer order
 * @param paymentMethod the payment method as a string
 */
public record OrderAndPaymethod(
        CustomerOrder order,
        String paymentMethod
) {
}