package com.jwd.cafe.command;

import com.jwd.cafe.command.impl.*;
import com.jwd.cafe.service.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum CommandType {
    ERROR(new ErrorCommand(), "error"),
    TO_MAIN(new ToMainCommand(), "to-main"),
    TO_LOGIN(new ToLoginCommand(), "to-login"),
    TO_REGISTRATION(new ToRegistrationCommand(), "to-registration"),
    TO_MENU(new ToMenuCommand(ProductTypeService.getInstance()), "to-menu"),
    LOCALE_SWITCH(new LocaleSwitchCommand(), "locale-switch"),
    VERIFICATION(new ActivateUserCommand(UserService.getInstance()), "verification"),
    USER_LOGOUT(new LogoutCommand(), "user-logout"),
    ADMIN_TO_ADD_TYPE(new ToAddTypeCommand(), "admin-to-add-type"),
    TO_MENU_ITEM(new ToMenuItemCommand(ProductTypeService.getInstance(), ProductService.getInstance()),
            "to-menu-item"),
    ADMIN_TO_ADD_PRODUCT(new ToAddProductCommand(), "admin-to-add-product"),
    USER_TO_CREATE_ORDER(new ToCreateOrderCommand(ProductService.getInstance()), "user-to-create-order"),
    USER_TO_CART(new ToCartCommand(ProductService.getInstance()), "user-to-cart"),
    USER_TO_ORDER_CONFIRM(new ToOrderConfirmCommand(), "user-to-order-confirm"),
    USER_TO_MY_ORDERS(new ToMyOrdersCommand(OrderService.getInstance()), "user-to-my-orders"),
    USER_TO_PROFILE(new ToProfileCommand(UserService.getInstance()), "user-to-profile"),
    USER_TO_REVIEW(new ToReviewCommand(), "user-to-review"),
    USER_TO_REVIEW_CONFIRM(new ToReviewConfirmCommand(), "user-to-review-confirm"),
    ADMIN_TO_USERS(new ToUsersCommand(UserService.getInstance()), "admin-to-users"),
    ADMIN_TO_ORDERS(new ToOrdersCommand(OrderService.getInstance()), "admin-to-orders"),
    ADMIN_TO_REVIEWS(new ToReviewsCommand(ReviewService.getInstance()), "admin-to-reviews");
    private final Command command;
    private final String name;

    CommandType(Command command, String name) {
        this.command = command;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Command getCommand() {
        return command;
    }

    public static Command getCommandByName(String name) {
        CommandType commandType;
        try {
            commandType = CommandType.valueOf(name.toUpperCase().replaceAll("-", "_"));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Unknown command: " + name);
            commandType = CommandType.ERROR;
        }
        return commandType.command;
    }
}