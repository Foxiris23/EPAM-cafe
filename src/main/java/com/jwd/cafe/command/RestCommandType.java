package com.jwd.cafe.command;

import com.jwd.cafe.command.impl.*;
import com.jwd.cafe.service.*;
import lombok.extern.log4j.Log4j2;

/**
 * Types of rest {@link Command}s
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public enum RestCommandType {
    LOGIN(new RestLoginCommand(UserService.getInstance()), "login"),
    ERROR(new RestErrorCommand(), "rest-error"),
    REGISTRATION(new RestRegistrationCommand(UserService.getInstance()), "register"),
    ADMIN_ADD_TYPE(new RestAddTypeCommand(ProductTypeService.getInstance()), "admin-add-type"),
    ADMIN_ADD_PRODUCT(new RestAddProductCommand(ProductTypeService.getInstance(), ProductService.getInstance()),
            "admin-add-product"),
    USER_CHECKOUT(new RestCheckoutCommand(UserService.getInstance(), ProductService.getInstance(),
            OrderService.getInstance()), "user-checkout"),
    USER_TOP_UP(new RestTopUpCommand(UserService.getInstance()), "user-top-up"),
    USER_EDIT_PROFILE(new RestEditProfileCommand(UserService.getInstance()), "user-edit-profile"),
    USER_REVIEW(new RestReviewCommand(ReviewService.getInstance(), OrderService.getInstance()),
            "user-review"),
    USER_ADD_TO_CART(new RestAddToCartCommand(ProductService.getInstance()),"user-add-to-cart"),
    USER_DELETE_FROM_CART(new RestDeleteFromCartCommand(ProductService.getInstance()),"user-delete-from-cart"),
    ADMIN_UPDATE_USER(new RestUpdateUserCommand(UserService.getInstance()), "admin-user-update"),
    ADMIN_UPDATE_ORDER(new RestUpdateOrderCommand(UserService.getInstance(), OrderService.getInstance()),
            "admin-order-update"),
    ADMIN_EDIT_TYPE(new RestEditTypeCommand(ProductTypeService.getInstance()), "admin-edit-type"),
    ADMIN_DELETE_TYPE(new RestDeleteTypeCommand(ProductTypeService.getInstance()), "admin-delete-type"),
    ADMIN_DELETE_PRODUCT(new RestDeleteProductCommand(ProductService.getInstance()), "admin-delete-product"),
    ADMIN_EDIT_PRODUCT(new RestEditProductCommand(ProductService.getInstance()), "admin-edit-product");
    private final Command command;
    private final String name;

    RestCommandType(Command command, String name) {
        this.command = command;
        this.name = name;
    }

    public Command getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    public static Command getCommandByName(String name) {
        RestCommandType restCommandType;
        try {
            restCommandType = RestCommandType.valueOf(name.toUpperCase().replaceAll("-", "_"));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Unknown command: " + name);
            restCommandType = RestCommandType.ERROR;
        }
        return restCommandType.command;
    }
}