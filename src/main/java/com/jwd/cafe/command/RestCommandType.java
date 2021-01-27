package com.jwd.cafe.command;

import com.jwd.cafe.command.impl.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum RestCommandType {
    LOGIN(new RestLoginCommand(), "login"),
    ERROR(new RestErrorCommand(), "rest-error"),
    REGISTRATION(new RestRegistrationCommand(), "register"),
    ADMIN_ADD_TYPE(new RestAddTypeCommand(), "admin-add-type"),
    ADMIN_ADD_PRODUCT(new RestAddProductCommand(), "admin-add-product"),
    USER_CHECKOUT(new RestCheckoutCommand(), "user-checkout"),
    USER_TOP_UP(new RestTopUpCommand(), "user-top-up"),
    USER_EDIT_PROFILE(new RestEditProfileCommand(), "user-edit-profile"),
    USER_REVIEW(new RestReviewCommand(), "user-review"),
    ADMIN_UPDATE_USER(new RestUpdateUserCommand(), "admin-user-update"),
    ADMIN_UPDATE_ORDER(new RestUpdateOrderCommand(), "admin-order-update"),
    ADMIN_EDIT_TYPE(new RestEditTypeCommand(), "admin-edit-type"),
    ADMIN_DELETE_TYPE(new RestDeleteTypeCommand(), "admin-delete-type"),
    ADMIN_DELETE_PRODUCT(new RestDeleteProductCommand(), "admin-delete-product"),
    ADMIN_EDIT_PRODUCT(new RestEditProductCommand(), "admin-edit-product");
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