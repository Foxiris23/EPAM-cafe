package com.jwd.cafe.service;

import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.dao.impl.UserDao;
import com.jwd.cafe.dao.specification.*;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.mail.ActivationMailSender;
import com.lambdaworks.crypto.SCryptUtil;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;

@Log4j2
public class UserService {
    private static volatile UserService instance;
    private final UserDao userDao;

    public static UserService getInstance() {
        UserService localInstance = instance;
        if (localInstance == null) {
            synchronized (UserService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserService();
                }
            }
        }
        return localInstance;
    }

    public Optional<User> findByUsername(String username) throws ServiceException {
        try {
            List<User> users = userDao.findBySpecification(new FindByUsername(username));
            if (users.size() > 0) {
                return Optional.of(users.get(0));
            }
        } catch (DaoException e) {
            log.error("Dao provided an exception for a user search");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public Optional<User> findByActivationCode(String activationCode) {
        try {
            List<User> users = userDao.findBySpecification(new FindByActivationCode(activationCode));
            if (users.size() > 0) {
                return Optional.of(users.get(0));
            }
        } catch (DaoException daoException) {
            log.error("Dao provided an exception for a user search");
            daoException.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<User> findByEmail(String email) throws ServiceException {
        User user = null;
        try {
            List<User> users = userDao.findBySpecification(new FindByEmail(email));
            if (users.size() > 0) {
                return Optional.of(users.get(0));
            }
        } catch (DaoException e) {
            log.error("Dao provided an exception for a user search");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public Optional<String> login(String username, String password, Map<String, Object> session) throws ServiceException {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (SCryptUtil.check(password, user.getPassword())) {
                if (user.getIsBlocked()) {
                    return Optional.of("serverMessage.blockedAccount");
                }
                if (user.getIsActive()) {
                    session.put(RequestConstant.USER, user);
                    return Optional.empty();
                }
                return Optional.of("serverMessage.activateAccountPlease");
            }
        }
        return Optional.of("serverMessage.incorrectUsernameOrPassword");
    }

    public Optional<String> register(User user) throws ServiceException {
        if (findByUsername(user.getUsername()).isEmpty()) {
            if (findByEmail(user.getEmail()).isEmpty()) {
                String code = UUID.randomUUID().toString();
                Runnable emailSender = new ActivationMailSender(user.getEmail(), code);
                Executors.newSingleThreadExecutor().submit(emailSender);
                try {
                    user.setActivationCode(code);
                    userDao.create(user);
                    return Optional.empty();
                } catch (DaoException e) {
                    log.error("UserDao provided an exception");
                    throw new ServiceException(e);
                }
            }
            return Optional.of("serverMessage.emailAlreadyTaken");
        }
        return Optional.of("serverMessage.usernameAlreadyTaken");
    }

    public Optional<String> activateUser(String activationCode) throws ServiceException {
        Optional<User> userOptional = findByActivationCode(activationCode);
        if (userOptional.isPresent()) {
            try {
                User user = userOptional.get();
                user.setActivationCode(null);
                user.setIsActive(true);
                userDao.update(user);
                return Optional.empty();
            } catch (DaoException e) {
                log.error("UserDao provided an exception for user update");
                throw new ServiceException(e);
            }
        }
        return Optional.of("serverMessage.incorrectActivationCode");
    }

    public void updateUser(User user) throws ServiceException {
        try {
            userDao.update(user);
        } catch (DaoException e) {
            log.error("Failed to update user");
            throw new ServiceException();
        }
    }

    public List<User> findAll(Integer page, Integer perPage) throws ServiceException {
        try {
            Long offset = ((long) (perPage) * (page - 1));
            return userDao.findBySpecification(new FindAllLimitOffset(perPage, offset));
        } catch (DaoException e) {
            log.error("Failed to load all users");
            throw new ServiceException(e);
        }
    }

    public Long countUsers() throws ServiceException {
        try {
            return userDao.countWithSpecification(new CountAll());
        } catch (DaoException e) {
            log.error("Failed to count users");
            throw new ServiceException(e);
        }
    }

    public Optional<User> findById(Long id) throws ServiceException {
        try {
            List<User> users = userDao.findBySpecification(new FindUserById(id));
            if (users.size() > 0) {
                return Optional.of(users.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to find user by id");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    private UserService() {
        userDao = UserDao.getInstance();
    }
}