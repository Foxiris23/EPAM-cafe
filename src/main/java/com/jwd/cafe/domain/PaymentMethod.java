package com.jwd.cafe.domain;

import com.jwd.cafe.exception.EntityNotFoundException;

/**
 * The representation of {@link Order}'s payment method
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public enum PaymentMethod {
    BALANCE(1), CASH(2), CARD(3);
    private final Integer id;

    PaymentMethod(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static PaymentMethod resolveMethodById(int id) throws EntityNotFoundException {
        PaymentMethod method;
        try {
            method = PaymentMethod.values()[id - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Order method with id: " + id + " not found");
        }
        return method;
    }
}
