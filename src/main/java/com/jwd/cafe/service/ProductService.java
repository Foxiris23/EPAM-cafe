package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.ProductDao;
import com.jwd.cafe.dao.specification.FindAllProductsByTypeId;
import com.jwd.cafe.dao.specification.FindByProductName;
import com.jwd.cafe.dao.specification.FindByProductTypeId;
import com.jwd.cafe.dao.specification.FindProductById;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.VisibleForTesting;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The class provides a business logic of application connected with {@link Product}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ProductService {
    private static volatile ProductService instance;
    private final ProductDao productDao;

    public static ProductService getInstance() {
        ProductService localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProductService(ProductDao.getInstance());
                }
            }
        }
        return localInstance;
    }

    public Optional<Product> findProductById(Integer id) throws ServiceException {
        try {
            List<Product> products = productDao.findBySpecification(new FindProductById(id));
            if (products.size() > 0) {
                return Optional.of(products.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to find a product by id");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public Optional<Product> findProductByName(String name) throws ServiceException {
        try {
            List<Product> products = productDao.findBySpecification(new FindByProductName(name));
            if (products.size() > 0) {
                return Optional.of(products.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to find a product by name");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public Optional<String> createProduct(Product product) throws ServiceException {
        try {
            Optional<Product> optional = findProductByName(product.getName());
            if (optional.isEmpty()) {
                productDao.create(product);
                return Optional.empty();
            }
        } catch (DaoException e) {
            log.error("Failed to create product");
            throw new ServiceException(e);
        }
        return Optional.of("serverMessage.productNameAlreadyTaken");
    }

    public List<Product> findProductsByTypeId(Integer id, Integer page, Integer perPage) throws ServiceException {
        try {
            Long offset = ((long) (perPage) * (page - 1));
            return productDao.findBySpecification(new FindAllProductsByTypeId(id, perPage, offset));
        } catch (DaoException e) {
            log.error("Failed to load all products by type id: " + id);
            throw new ServiceException(e);
        }
    }

    public Long countProductWithTypeId(Integer id) throws ServiceException {
        try {
            return productDao.countWithSpecification(new FindByProductTypeId(id));
        } catch (DaoException e) {
            log.error("Failed to count products by type id: " + id);
            throw new ServiceException(e);
        }
    }

    public Map<Product, Integer> loadCart(Map<Integer, Integer> cart) throws ServiceException {
        Map<Product, Integer> products = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Optional<Product> productOptional = findProductById(entry.getKey());
            productOptional.ifPresent(product -> products.put(product, entry.getValue()));
        }
        return products;
    }

    public Double calcTotalCost(Map<Product, Integer> products) {
        BigDecimal bigDecimal = new BigDecimal(0);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal price = BigDecimal.valueOf(entry.getKey().getPrice());
            bigDecimal = bigDecimal.add(price.multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return bigDecimal.doubleValue();
    }

    public void deleteByTypeId(Integer id) throws ServiceException {
        try {
            productDao.deleteWithSpecification(new FindByProductTypeId(id));
        } catch (DaoException e) {
            log.error("Failed to delete products by type id: " + id);
            throw new ServiceException(e);
        }
    }

    public void update(Product product) throws ServiceException {
        try {
            productDao.update(product);
        } catch (DaoException e) {
            log.error("Failed to update product with id: " + product.getId());
            throw new ServiceException(e);
        }
    }

    public void delete(Integer id) throws ServiceException {
        try {
            productDao.deleteWithSpecification(new FindProductById(id));
        } catch (DaoException e) {
            log.error("Failed to delete product with id: " + id);
            throw new ServiceException(e);
        }
    }

    public Optional<String> editProduct(Integer id, String name, Double price, String description) throws ServiceException {
        Optional<Product> productOptional = findProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (!product.getName().equals(name)) {
                if (findProductByName(name).isPresent()) {
                    return Optional.of("serverMessage.productNameAlreadyTaken");
                }
            }
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            update(product);
        } else {
            return Optional.of("serverMessage.productNotFound");
        }
        return Optional.empty();
    }

    private ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @VisibleForTesting
    public static ProductService getTestInstance(ProductDao productDao) {
        return new ProductService(productDao);
    }
}