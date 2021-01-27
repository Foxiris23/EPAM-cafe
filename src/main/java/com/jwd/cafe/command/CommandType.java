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
    LOGOUT(new LogoutCommand(), "logout"),
    TO_ADD_TYPE(new ToAddTypeCommand(), "to-add-type"),
    TO_MENU_ITEM(new ToMenuItemCommand(ProductTypeService.getInstance(), ProductService.getInstance()),
            "to-menu-item"),
    TO_ADD_PRODUCT(new ToAddProductCommand(), "to-add-product"),
    TO_CREATE_ORDER(new ToCreateOrderCommand(ProductService.getInstance()), "to-create-order"),
    TO_CART(new ToCartCommand(ProductService.getInstance()), "to-cart"),
    TO_ORDER_CONFIRM(new ToOrderConfirmCommand(), "to-order-confirm"),
    TO_MY_ORDERS(new ToMyOrdersCommand(OrderService.getInstance()), "to-my-orders"),
    TO_PROFILE(new ToProfileCommand(UserService.getInstance()), "to-profile"),
    TO_REVIEW(new ToReviewCommand(), "to-review"),
    TO_REVIEW_CONFIRM(new ToReviewConfirmCommand(), "to-review-confirm"),
    TO_USERS(new ToUsersCommand(UserService.getInstance()), "to-users"),
    TO_ORDERS(new ToOrdersCommand(OrderService.getInstance()), "to-orders"),
    TO_REVIEWS(new ToReviewsCommand(ReviewService.getInstance()), "to-reviews");
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