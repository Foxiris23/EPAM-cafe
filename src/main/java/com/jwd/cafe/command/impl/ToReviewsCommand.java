package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Review;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ReviewService;
import com.jwd.cafe.util.Pageable;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
public class ToReviewsCommand implements Command {
    private static final Integer PER_PAGE = 30;
    private final ReviewService reviewService;

    public ToReviewsCommand(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new IntValidator(null, RequestConstant.PAGE).validate(requestContext);

        if (violationMessages.isEmpty()) {
            Integer page = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.PAGE));
            try {
                List<Review> reviews = reviewService.findAll(page, PER_PAGE);
                Long count = reviewService.countReviews();
                Pageable<Review> pageable = new Pageable<>(reviews, page, count, PER_PAGE);
                return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.REVIEWS),
                        Map.of(RequestConstant.PAGEABLE, pageable), new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to return all reviews");
            }
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
