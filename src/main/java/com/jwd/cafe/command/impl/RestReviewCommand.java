package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.Review;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.service.ReviewService;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.impl.ReviewCodeValidator;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class RestReviewCommand implements Command {
    private final ReviewService reviewService;
    private final OrderService orderService;

    public RestReviewCommand(ReviewService reviewService, OrderService orderService) {
        this.reviewService = reviewService;
        this.orderService = orderService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new ReviewCodeValidator(
                new StringValidator(null, RequestConstant.REVIEW)).validate(requestContext);

        if (violationMessages.isEmpty()) {
            try {
                String feedback = requestContext.getRequestParameters().get(RequestConstant.REVIEW);
                Integer rate = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.RATE));
                String code = requestContext.getRequestParameters().get(RequestConstant.CODE);
                User user = (User) requestContext.getSessionAttributes().get(RequestConstant.USER);
                Optional<Order> orderOptional = orderService.findOrderByUserAndReviewCode(user, code);
                if (orderOptional.isEmpty()) {
                    return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE,
                            LocalizationHelper.localize(
                                    requestContext.getLocale(), "serverMessage.orderNotFound")),
                            new HashMap<>());
                }
                Order order = orderOptional.get();
                Review review = Review.builder().withFeedback(feedback).withRate(rate).withOrder(order)
                        .build();
                reviewService.create(review);
                order.setReviewCode(null);
                orderService.updateOrder(order);
                return new ResponseContext(
                        Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.USER_TO_REVIEW_CONFIRM.getName()),
                        new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to create review");
                return RestCommandType.ERROR.getCommand().execute(requestContext);
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}