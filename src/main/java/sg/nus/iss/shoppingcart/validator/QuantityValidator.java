package sg.nus.iss.shoppingcart.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.Product;

/**
 * @ClassName QuantityValidator
 * @Description
 * @Author YAO YIYANG
 * @StudentID A0294873L
 * @Date 2024/10/3
 * @Version 1.0
 */

@Component
public class QuantityValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CartItem.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CartItem cartItem = (CartItem) target;
        int quantity = cartItem.getQuantity();
        Product product = cartItem.getProduct();

        if (quantity <= 0) {
            errors.rejectValue("quantity", "error.quantity", "Quantity must be greater than 0");
        }

        if (product != null && quantity > product.getStock()) {
            errors.rejectValue("quantity", "error.quantity", "Quantity cannot exceed product stock");
        }
    }
}

