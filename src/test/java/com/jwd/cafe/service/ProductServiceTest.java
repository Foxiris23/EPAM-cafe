package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.ProductDao;
import com.jwd.cafe.dao.specification.Specification;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    private ProductService productService;

    @Before
    public void beforeTests() {
        productDao = mock(ProductDao.class);
        productService = ProductService.getTestInstance(productDao);
    }

    @Test
    public void createProductShouldReturnEmptyMessage() throws DaoException, ServiceException {
        when(productDao.findBySpecification(any(Specification.class))).thenReturn(new ArrayList<>());
        doNothing().when(productDao).create(any(Product.class));

        assertThat(productService.createProduct(Product.builder().withName("burgers").build()).isEmpty()).isTrue();
    }

    @Test
    public void createProductShouldReturnNameAlreadyTakenMessage() throws DaoException, ServiceException {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().withName("burgers").build());
        when(productDao.findBySpecification(any(Specification.class))).thenReturn(products);
        doNothing().when(productDao).create(any(Product.class));

        assertThat(productService.createProduct(Product.builder().withName("burgers").build()).isEmpty()).isFalse();
    }

    @Test(expected = ServiceException.class)
    public void createProductShouldThrowServiceException() throws DaoException, ServiceException {
        when(productDao.findBySpecification(any(Specification.class))).thenReturn(new ArrayList<>());
        doThrow(DaoException.class).when(productDao).create(any(Product.class));

        productService.createProduct(Product.builder().withName("burgers").build());
    }

    @Test
    public void loadCartShouldReturnCart() throws DaoException, ServiceException {
        Product product = Product.builder().withId(3).build();
        Map<Integer, Integer> idCart = Map.of(3, 5);
        Map<Product, Integer> productCart = Map.of(product, 5);

        when(productDao.findBySpecification(any(Specification.class))).thenReturn(List.of(product));

        assertThat(productService.loadCart(idCart)).isEqualTo(productCart);
    }

    @Test
    public void calcTotalCostShouldReturn45() {
        Map<Product, Integer> cart = Map.of(
                Product.builder().withPrice(3.0).build(), 5,
                Product.builder().withPrice(2.5).build(), 4,
                Product.builder().withPrice(10.0).build(), 2);

        assertThat(productService.calcTotalCost(cart)).isCloseTo(45.0, Offset.offset(0.01));
    }
}