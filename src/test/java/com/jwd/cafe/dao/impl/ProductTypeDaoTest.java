package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.specification.FindAll;
import com.jwd.cafe.dao.specification.FindProductTypeById;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeDaoTest {
    private static TestDatabaseConf testDatabaseConf;

    @BeforeAll
    static void beforeAll() throws ClassNotFoundException {
        testDatabaseConf = new TestDatabaseConf();
        testDatabaseConf.initDatabase();
    }


    @Test
    public void successCRUDOperationsTest() throws DaoException {
        ProductTypeDao productTypeDao = ProductTypeDao.getInstance();
        ProductType productType = ProductType.builder().withId(1).withName("burgers").withFilename("test").build();

        productTypeDao.create(productType);
        List<ProductType> createProducts = productTypeDao.findBySpecification(new FindProductTypeById(1));
        assertThat(createProducts).contains(productType);

        productType.setName("Burgers2");
        productTypeDao.update(productType);
        List<ProductType> updateProducts = productTypeDao.findBySpecification(new FindProductTypeById(1));
        assertThat(updateProducts).contains(productType);

        Long actualCount = productTypeDao.countWithSpecification(new FindAll());
        assertThat(actualCount).isEqualTo(1L);

        productTypeDao.deleteWithSpecification(new FindProductTypeById(productType.getId()));
        List<ProductType> deleteProducts = productTypeDao.findBySpecification(new FindProductTypeById(1));
        assertThat(deleteProducts).doesNotContain(productType);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        testDatabaseConf.destroyDatabase();
    }
}