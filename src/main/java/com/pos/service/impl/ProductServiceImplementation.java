package com.pos.service.impl;

import com.pos.modal.Product;
import com.pos.modal.Store;
import com.pos.modal.User;
import com.pos.payload.dto.ProductDto;
import com.pos.payload.mapper.ProductMapper;
import com.pos.repository.ProductRepository;
import com.pos.repository.StoreRepository;
import com.pos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto, User user) throws Exception {

        Store store = validateStoreAccess(productDto.getStoreId(), user);

        System.out.println(store.getId());

        Product product = new Product();
        productMapper.updateFromDto(productDto, product);
        product.setStore(store);

        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) throws Exception {

        Product existedProduct = productRepository.findById(id).orElse(null);
        if(existedProduct == null) throw new Exception("Product not found");

        validateStoreAccess(existedProduct.getStore().getId(), user);

        productMapper.updateFromDto(productDto, existedProduct);

        if (productDto.getStoreId() != null && !productDto.getStoreId().equals(existedProduct.getStore().getId())) {
            Store newStore = validateStoreAccess(productDto.getStoreId(), user);
            existedProduct.setStore(newStore);
        }

        Product updatedProduct =  productRepository.save(existedProduct);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    public Product getProductById(Long id, User user) throws Exception {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found with ID: " + id));

        validateStoreAccess(product.getStore().getId(), user);

        return product;
    }


    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) throws Exception {

        if(!storeRepository.existsById(storeId)) {
            throw new Exception("Store not found");
        }

        return productRepository.findByStoreId(storeId).stream().map(productMapper::toDto).toList();
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) throws Exception {

        if(!storeRepository.existsById(storeId)) {
            throw new Exception("Store not found");
        }
        if (keyword == null || keyword.trim().isEmpty()) {
            return getProductsByStoreId(storeId);
        }

        return productRepository.searchByKeywordComprehensive(storeId, keyword.trim()).stream().map(productMapper::toDto).toList();
    }

    @Override
    public void deleteProduct(Long id, User user) throws Exception {

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new Exception("Product not found");
        }

        validateStoreAccess(product.getStore().getId(), user);

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> searchAllByKeyword(String keyword) throws Exception {

        return productRepository.searchByKeyword(keyword.trim()).stream().map(productMapper::toDto).toList();
    }

    private Store validateStoreAccess(Long storeId, User user) throws Exception {

        Store store = storeRepository.findById(storeId).orElse(null);
        if(store == null) {
            throw new Exception("Store not found!");
        }
        if(!store.getStoreAdmin().getId().equals(user.getId())) {
            throw new Exception("You don't have access to this store");
        }

        return store;
    }
}
