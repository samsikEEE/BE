package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.product.ProductRequestDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.product.ProductResponseDto;
import com.sprta.samsike.application.service.ProductService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Create Product,생성
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ApiResponseDTO<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.createProduct(requestDto, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201: Created
    }

    // Get Products by Restaurant,조회
    @GetMapping("/restaurant/{restaurantUuid}")
    public ResponseEntity<?> getProductsByRestaurant(@PathVariable UUID restaurantUuid) {
        ApiResponseDTO<List<ProductResponseDto>> response = productService.getProductsByRestaurant(restaurantUuid);
        return ResponseEntity.ok(response);
    }

    // Update Product,수정
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateProduct(@PathVariable UUID productId,
                                           @RequestBody ProductRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<ProductResponseDto> result = productService.updateProduct(productId, requestDto, userDetails.getMember());
        return ResponseEntity.ok(result); // HTTP 200: OK
    }

    // Delete Product,삭제
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<String> result = productService.deleteProduct(productId, userDetails.getMember());
        return ResponseEntity.ok(result); // HTTP 200: OK
    }

}
