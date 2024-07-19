package com.cart.shoppingcart.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cart.shoppingcart.dtos.shop.ProductDTO;
import com.cart.shoppingcart.entities.shopping.CategoryEntity;
import com.cart.shoppingcart.entities.shopping.ProductEntity;
import com.cart.shoppingcart.repositories.shopping.ProductJpaRepository;
import com.cart.shoppingcart.services.shop.ProductService;


public class ProductServiceTest {
    
    List<ProductDTO> products = new ArrayList<>();
    @Mock
    private ProductJpaRepository productJpaRepository;

    @InjectMocks
    private ProductService productService;


    @BeforeEach
    public void setUp(){
        products.add(ProductDTO.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(100))
                .stock(10)
                .image("image1")
                .build());

        products.add(ProductDTO.builder()
                .id(2L)
                .name("Product 2")
                .description("Description 2")
                .price(BigDecimal.valueOf(200))
                .stock(20)
                .image("image2")
                .build());

                  MockitoAnnotations.openMocks(this);


    
    }


    @Test
    public void getProductByIdTest(){
        CategoryEntity categoryEntity = new CategoryEntity();
        ProductEntity productEntity = new ProductEntity(1L, "Product 1", "Description 1", BigDecimal.valueOf(100), 10, "image1",categoryEntity)   ;
        when(productJpaRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        ProductDTO product = products.get(0);
        ProductDTO response = productService.getProductById(1L);

        assertEquals(product.getId(), response.getId());
    }

    
    @Test
    public void getProductsTest(){
        List<ProductEntity> productEntities = new ArrayList<>();
        CategoryEntity categoryEntity = new CategoryEntity();
        ProductEntity productEntity = new ProductEntity(1L, "Product 1", "Description 1", BigDecimal.valueOf(100), 10, "image1",categoryEntity)   ;
        ProductEntity productEntity2 = new ProductEntity(2L, "Product 2", "Description 2", BigDecimal.valueOf(100), 12, "image2",categoryEntity)   ;
        productEntities.add(productEntity);
        productEntities.add(productEntity2);

        when(productJpaRepository.findAll()).thenReturn(productEntities);


        List<ProductDTO> response = productService.getAllProducts();

        assertEquals(products.size(), response.size());
    }
}
