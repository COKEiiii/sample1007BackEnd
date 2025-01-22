package sg.nus.iss.shoppingcart.model.DTO;

import sg.nus.iss.shoppingcart.model.Category;
import sg.nus.iss.shoppingcart.model.Product;

public record CartItemDTO
        (
         int cartId,
         int id,
         int stock,
         int Quantity,
         double subTotalPrice,
         String description ,
         String name,
         String image,
         double price,
         Category category
         ) {
}
