package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.ReviewDao;
import com.jwd.cafe.dao.specification.CountAll;
import com.jwd.cafe.dao.specification.FindAllLimitOffset;
import com.jwd.cafe.domain.Review;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.List;

@Log4j2
public class ReviewService {
    private static volatile ReviewService instance;
    private final ReviewDao reviewDao;

    public static ReviewService getInstance() {
        ReviewService localInstance = instance;
        if (localInstance == null) {
            synchronized (ReviewService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ReviewService(ReviewDao.getInstance());
                }
            }
        }
        return localInstance;
    }


    public List<Review> findAll(Integer page, Integer perPage) throws ServiceException {
        try {
            Long offset = ((long) (perPage) * (page - 1));
            return reviewDao.findBySpecification(new FindAllLimitOffset(perPage, offset));
        } catch (DaoException e) {
            log.error("Failed to load all reviews");
            throw new ServiceException(e);
        }
    }

    public Long countReviews() throws ServiceException {
        try {
            return reviewDao.countWithSpecification(new CountAll());
        } catch (DaoException e) {
            log.error("Failed to count reviews");
            throw new ServiceException(e);
        }
    }

    public void create(Review review) throws ServiceException {
        try {
            reviewDao.create(review);
        } catch (DaoException e) {
            log.error("Failed to create review");
            throw new ServiceException(e);
        }
    }

    private ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @VisibleForTesting
    public static ReviewService getTestInstance(ReviewDao reviewDao) {
        return new ReviewService(reviewDao);
    }
}
