package com.jwd.cafe.domain;

import com.jwd.cafe.exception.EntityNotFoundException;

/**
 * The representation of {@link Order}'s status
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public enum OrderStatus{
    ACTIVE(1), CANCELLED(2), COMPLETED(3), UNACCEPTED(4);
    private final Integer id;

    OrderStatus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static OrderStatus resolveStatusById(int id) throws EntityNotFoundException {
        OrderStatus status;
        try {
            status = OrderStatus.values()[id - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Order status with id: " + id + " not found");
        }
        return status;
    }
}
