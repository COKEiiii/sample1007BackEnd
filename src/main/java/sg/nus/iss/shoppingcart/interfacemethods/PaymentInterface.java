package sg.nus.iss.shoppingcart.interfacemethods;

import sg.nus.iss.shoppingcart.enums.PaymentMethods;
import sg.nus.iss.shoppingcart.model.CustomerOrder;
import sg.nus.iss.shoppingcart.model.Payment;

public interface PaymentInterface {

    boolean makePayment(CustomerOrder order, PaymentMethods paymentMethod);

    boolean Pay();

    void Refund(Payment payment);

    byte[] generateReceipt(CustomerOrder order, Payment payment);

    Payment findByPaymentId(int paymentId);
}