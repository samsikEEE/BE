package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.product.ProductRequestDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.product.ProductResponseDto;
import com.sprta.samsike.application.service.ProductService;
import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_OWNER')")
    public ResponseEntity<ApiResponseDTO<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Product 생성 후 응답 데이터를 반환받음
        ApiResponseDTO<ProductResponseDto> response = productService.createProduct(requestDto, userDetails.getMember());
        // 201 상태 코드와 함께 응답 본문에 message와 data를 포함
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get Products by Restaurant,조회
    @GetMapping("/restaurant/{restaurantUuid}")
    public ResponseEntity<?> getProductsByRestaurant(@PathVariable("restaurantUuid") UUID restaurantUuid) {
        ApiResponseDTO<List<ProductResponseDto>> response = productService.getProductsByRestaurant(restaurantUuid);
        return ResponseEntity.ok(response);
    }

    //페이지네이션으로 정렬 조회
//    @GetMapping
//    public Page<ProductResponseDto> getProducts(
//            @RequestParam UUID restaurantUuid,
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String sortBy,
//            @RequestParam boolean ascending
//    ) {
//        // Service에서 Page<Product>를 받아옴
//        Page<Product> productPage = productService.getProductsWithPagination(
//                restaurantUuid, page, size, sortBy, ascending
//        );
//
//        // Page<Product> → Page<ProductResponseDto> 변환 (map 사용)
//        return productPage.map(ProductResponseDto::new);
//    }


    // Update Product,수정
    @PutMapping("/{productUuid}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateProduct(@PathVariable("productUuid") UUID productId,
                                           @RequestBody ProductRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<ProductResponseDto> response = productService.updateProduct(productId, requestDto, userDetails.getMember());
        return ResponseEntity.ok(response); // HTTP 200: OK
    }

    // Delete Product,삭제
    @DeleteMapping("/{productUuid}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteProduct(@PathVariable("productUuid") UUID productId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<String> response = productService.deleteProduct(productId, userDetails.getMember());
        return ResponseEntity.ok(response); // HTTP 200: OK
    }

    // 숨김 product
    @PatchMapping("/{productUuid}/visibility")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ApiResponseDTO<String>> toggleProductVisibility(
            @PathVariable("productUuid") UUID productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<String> response = productService.toggleProductVisibility(productId, userDetails.getMember());
        return ResponseEntity.ok(response);
    }
    // Get Products by CreatedAt or UpdatedAt (정렬)
    @GetMapping("/restaurant/{restaurantUuid}/sorted")
    public ResponseEntity<?> getProductsSortedBy(
            @PathVariable("restaurantUuid") UUID restaurantUuid,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,  // 정렬 기준 (기본값: createdAt)
            @RequestParam(name = "ascending", defaultValue = "true") boolean ascending // 정렬 순서 (기본값: true)
    ) {
        ApiResponseDTO<List<ProductResponseDto>> response = productService.getProductsSortedBy(restaurantUuid, sortBy, ascending);
        return ResponseEntity.ok(response);
    }
}

