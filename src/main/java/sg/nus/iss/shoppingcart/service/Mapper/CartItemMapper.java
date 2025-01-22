package sg.nus.iss.shoppingcart.service.Mapper;

import org.springframework.stereotype.Component;
import sg.nus.iss.shoppingcart.model.CartItem;
import sg.nus.iss.shoppingcart.model.DTO.CartItemDTO;

@Component
public class CartItemMapper {
    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getCart().getCartId(),
                cartItem.getCartItemId(),
                cartItem.getProduct().getStock(),
                cartItem.getQuantity(),
                cartItem.getsubTotalPrice(),
                cartItem.getProduct().getDescription(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getCategory()
        );
    }
}
