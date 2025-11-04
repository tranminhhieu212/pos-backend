package com.pos.controller;

import com.pos.exception.UserException;
import com.pos.modal.Product;
import com.pos.payload.dto.ProductDto;
import com.pos.payload.response.ApiResponse;
import com.pos.service.ProductService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto
    ) throws UserException, Exception {
        return ResponseEntity.ok(productService.createProduct(productDto, userService.getCurrentUser()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id", required = true)  Long id,
            @RequestBody ProductDto productDto
    ) throws UserException, Exception {
        return ResponseEntity.ok(productService.updateProduct(id, productDto, userService.getCurrentUser()));
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<ProductDto>> searchProductByKeyword(
            @PathVariable(name = "storeId", required = true)  Long storeId,
            @RequestParam(name = "keyword", required = false) String keyword
    ) throws Exception {
        return ResponseEntity.ok(productService.searchByKeyword(storeId, keyword));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchAllProductByKeyword(
            @RequestParam(name = "keyword", required = false) String keyword
    ) throws Exception {
        return ResponseEntity.ok(productService.searchAllByKeyword(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable(name = "id", required = true)  Long id
    ) throws UserException, Exception {
        return ResponseEntity.ok(productService.getProductById(id, userService.getCurrentUser()));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDto>> getProductsByStoreId(
            @PathVariable(name = "storeId", required = true) Long storeId
    ) throws UserException, Exception {
        return ResponseEntity.ok(productService.getProductsByStoreId(storeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(
            @PathVariable(name = "id", required = true)  Long id
    ) throws UserException, Exception {

        productService.deleteProduct(id, userService.getCurrentUser());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Product deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }
}
