package com.cart.shoppingcart.services.shop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart.shoppingcart.dtos.shop.ProductDTO;
import com.cart.shoppingcart.repositories.shopping.ProductJpaRepository;

@Service
public class ProductService {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = new ArrayList<>();

        productJpaRepository.findAll().forEach(product -> {
            products.add(ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .image(product.getImage())
                    .build());
        });

        return products;
    }

    
    public ProductDTO getProductById(Long id) {
        return productJpaRepository.findById(id).map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .image(product.getImage())
                .build()).orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
