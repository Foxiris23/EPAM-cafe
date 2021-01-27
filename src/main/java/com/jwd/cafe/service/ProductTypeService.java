package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.ProductTypeDao;
import com.jwd.cafe.dao.specification.FindAllLimitOffset;
import com.jwd.cafe.dao.specification.FindByTypeName;
import com.jwd.cafe.dao.specification.FindProductTypeById;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
public class ProductTypeService {
    private static volatile ProductTypeService instance;
    private final ProductTypeDao productTypeDao;

    public static ProductTypeService getInstance() {
        ProductTypeService localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductTypeService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProductTypeService();
                }
            }
        }
        return localInstance;
    }

    public Optional<ProductType> findProductTypeById(Integer id) throws ServiceException {
        try {
            List<ProductType> products = productTypeDao.findBySpecification(new FindProductTypeById(id));
            if (products.size() > 0) {
                return Optional.of(products.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to load products type by id: " + id);
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public List<ProductType> findAllProductTypes() throws ServiceException {
        try {
            return productTypeDao.findBySpecification(new FindAllLimitOffset(500, 0L));
        } catch (DaoException e) {
            log.error("Failed to load all product types");
            throw new ServiceException(e);
        }
    }

    public Optional<ProductType> findByTypeName(String name) throws ServiceException {
        try {
            List<ProductType> productTypes = productTypeDao.findBySpecification(new FindByTypeName(name));
            if (productTypes.size() > 0) {
                return Optional.of(productTypes.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to find product type");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public Optional<String> createType(ProductType productType) throws ServiceException {
        try {
            if (findByTypeName(productType.getName()).isEmpty()) {
                productTypeDao.create(productType);
                return Optional.empty();
            }
            return Optional.of("serverMessage.typeNameAlreadyTaken");
        } catch (DaoException e) {
            log.error("ProductTypeDao provided an exception for type create");
            throw new ServiceException(e);
        }
    }

    public void update(ProductType productType) throws ServiceException {
        try {
            productTypeDao.update(productType);
        } catch (DaoException e) {
            log.error("ProductTypeDao provided an exception for type update");
            throw new ServiceException(e);
        }
    }

    public void deleteTypeById(Integer id) throws ServiceException {
        try {
            productTypeDao.deleteWithSpecification(new FindProductTypeById(id));
        } catch (DaoException e) {
            log.error("ProductTypeDao provided an exception for type delete");
            throw new ServiceException(e);
        }
    }

    public Optional<String> editType(Integer id, String newName, String newFilename) throws ServiceException {
        Optional<ProductType> productTypeOptional = findProductTypeById(id);
        if (productTypeOptional.isPresent()) {
            ProductType productType = productTypeOptional.get();
            if (!productType.getName().equals(newName)) {
                if (findByTypeName(newName).isPresent()) {
                    return Optional.of("serverMessage.typeNameAlreadyTaken");
                }
            }
            productType.setName(newName);
            productType.setFilename(newFilename);
            update(productType);
        } else {
            return Optional.of("serverMessage.productTypeNotFound");
        }
        return Optional.empty();
    }

    private ProductTypeService() {
        productTypeDao = ProductTypeDao.getInstance();
    }
}
