package com.pos.service;

import com.pos.modal.Product;
import com.pos.modal.User;
import com.pos.payload.dto.ProductDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, User user) throws Exception;
    ProductDto updateProduct(Long id, ProductDto productDto, User user) throws Exception;
    Product getProductById(Long id, User user) throws Exception;
    List<ProductDto> getProductsByStoreId(Long storeId) throws Exception;
    List<ProductDto> searchByKeyword(Long storeId, String keyword) throws Exception;
    List<ProductDto> searchAllByKeyword(String keyword) throws Exception;
    void deleteProduct(Long id, User user) throws Exception;
}
