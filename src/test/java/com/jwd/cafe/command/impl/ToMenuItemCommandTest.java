package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.ForwardResponse;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.command.ResponseType;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.service.ProductTypeService;
import com.jwd.cafe.util.Pageable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToMenuItemCommandTest {
    @Mock
    private ProductTypeService productTypeService;
    @Mock
    private ProductService productService;
    private RequestContext requestContext;
    private ToMenuItemCommand command;

    @BeforeEach
    public void setUp() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestConstant.PAGE, "1");
        params.put(RequestConstant.TYPE_ID, "2");
        productTypeService = mock(ProductTypeService.class);
        productService = mock(ProductService.class);
        requestContext = RequestContext.builder().withRequestParameters(params).withLocale("en_US").build();
        command = new ToMenuItemCommand(productTypeService, productService);
    }

    @Test
    public void executeShouldReturnRespWithPageableAndPageAndType() throws ServiceException {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().withName("test").build());
        Long count = 1L;
        Pageable<Product> pageable = new Pageable<>(products, 1, count, 1);
        Optional<ProductType> productTypeOptional = Optional.of(ProductType.builder().withId(1).build());
        when(productTypeService.findProductTypeById(anyInt()))
                .thenReturn(productTypeOptional);
        when(productService.countProductWithTypeId(anyInt())).thenReturn(count);
        when(productService.findProductsByTypeId(anyInt(), anyInt(), anyInt())).thenReturn(products);
        ResponseContext responseContext = new ResponseContext(
                new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.PRODUCTS),
                Map.of(RequestConstant.PAGEABLE, pageable,
                        RequestConstant.PRODUCT_TYPE, productTypeOptional.get()), new HashMap<>());

        assertThat(command.execute(requestContext)).isEqualTo(responseContext);
    }

    @Test
    public void executeShouldReturnErrorResp() throws ServiceException {
        when(productTypeService.findProductTypeById(anyInt())).thenThrow(new ServiceException());
        when(productService.countProductWithTypeId(anyInt())).thenThrow(new ServiceException());
        when(productService.findProductsByTypeId(anyInt(), anyInt(), anyInt())).thenThrow(new ServiceException());
        ResponseContext responseContext =
                new ResponseContext(
                        new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.ERROR_PAGE),
                        new HashMap<>(), new HashMap<>());
        assertThat(command.execute(requestContext)).isEqualTo(responseContext);
    }

    @AfterEach
    public void tearDown() {
        productTypeService = null;
        productService = null;
        requestContext = null;
        command = null;
    }
}