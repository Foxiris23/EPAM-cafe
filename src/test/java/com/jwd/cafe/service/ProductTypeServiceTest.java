package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.ProductTypeDao;
import com.jwd.cafe.dao.specification.Specification;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductTypeServiceTest {
    @Mock
    private ProductTypeDao productTypeDao;
    private ProductTypeService productTypeService;

    @Before
    public void beforeTests() {
        productTypeDao = mock(ProductTypeDao.class);
        productTypeService = ProductTypeService.getTestInstance(productTypeDao);
    }

    @Test
    public void createTypeShouldReturnEmptyMessage() throws DaoException, ServiceException {
        when(productTypeDao.findBySpecification(any(Specification.class))).thenReturn(new ArrayList<>());
        doNothing().when(productTypeDao).create(any(ProductType.class));

        assertThat(productTypeService.createType(ProductType.builder().withName("Burgers").build()).isEmpty())
                .isTrue();
    }

    @Test
    public void createTypeShouldReturnNameAlreadyTakenMessage() throws DaoException, ServiceException {
        when(productTypeDao.findBySpecification(any(Specification.class))).
                thenReturn(List.of(ProductType.builder().withName("Burgers").build()));
        doNothing().when(productTypeDao).create(any(ProductType.class));

        assertThat(productTypeService.createType(ProductType.builder().withName("Burgers").build()).get())
                .isEqualTo("serverMessage.typeNameAlreadyTaken");
    }

    @Test(expected = ServiceException.class)
    public void createTypeShouldThrowServiceException() throws DaoException, ServiceException {
        when(productTypeDao.findBySpecification(any(Specification.class))).
                thenThrow(DaoException.class);

        productTypeService.createType(ProductType.builder().withName("Burgers").build());
    }

    @Test
    public void editTypeShouldReturnNameAlreadyTakenMessage() throws DaoException, ServiceException {
        when(productTypeDao.findBySpecification(any(Specification.class))).
                thenReturn(List.of(ProductType.builder().withName("Burgers").build()));
        when(productTypeDao.update(any(ProductType.class))).thenReturn(ProductType.builder().build());

        assertThat(productTypeService.editType(1, "NotBurgers", anyString()).get())
                .isEqualTo("serverMessage.typeNameAlreadyTaken");
    }

    @Test
    public void editTypeShouldReturnTypeNotFoundMessage() throws DaoException, ServiceException {
        when(productTypeDao.findBySpecification(any(Specification.class))).
                thenReturn(new ArrayList<>());

        assertThat(productTypeService.editType(1, "NotBurgers", anyString()).get())
                .isEqualTo("serverMessage.productTypeNotFound");
    }
}