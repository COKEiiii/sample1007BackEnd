package sg.nus.iss.shoppingcart.model.DTO;

import sg.nus.iss.shoppingcart.model.CustomerOrder;

public record OrderAndPaymethod(
                CustomerOrder order,
                String paymentMethod) {
}