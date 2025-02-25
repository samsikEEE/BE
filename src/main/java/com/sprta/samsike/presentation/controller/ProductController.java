package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.product.ProductRequestDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.product.ProductResponseDto;
import com.sprta.samsike.application.service.ProductService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "상품 생성", description = "해당 가게에 대한 상품을 생성합니다.")
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
    @Operation(summary = "상품 조회", description = "restaurantId로 해당 가게의 상품을 조회")
    @GetMapping("/restaurant/{restaurantUuid}")
    public ResponseEntity<?> getProductsByRestaurant(@PathVariable("restaurantUuid") UUID restaurantUuid) {
        ApiResponseDTO<List<ProductResponseDto>> response = productService.getProductsByRestaurant(restaurantUuid);
        return ResponseEntity.ok(response);
    }

    // Update Product,수정
    @Operation(summary = "상품 내용 수정", description = "상품의 정보를 업데이트 합니다")
    @PutMapping("/{productUuid}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateProduct(@PathVariable("productUuid") UUID productId,
                                           @RequestBody ProductRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<ProductResponseDto> response = productService.updateProduct(productId, requestDto, userDetails.getMember());
        return ResponseEntity.ok(response); // HTTP 200: OK
    }

    // Delete Product,삭제
    @Operation(summary = "상품 소프트 삭제", description = "deletedBy를 이용하여 삭제")
    @DeleteMapping("/{productUuid}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteProduct(@PathVariable("productUuid") UUID productId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<String> response = productService.deleteProduct(productId, userDetails.getMember());
        return ResponseEntity.ok(response); // HTTP 200: OK
    }

    // 숨김 product
    @Operation(summary = "상품 숨김", description = "상품을 숨겨 조회시 나오지 않도록 합니다")
    @PatchMapping("/{productUuid}/visibility")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ApiResponseDTO<String>> toggleProductVisibility(
            @PathVariable("productUuid") UUID productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<String> response = productService.toggleProductVisibility(productId, userDetails.getMember());
        return ResponseEntity.ok(response);
    }
    // Get Products by CreatedAt or UpdatedAt (정렬)
    @Operation(summary = "상품 정렬 조회", description = "레스토랑의 상품 목록을 정렬 및 페이지네이션하여 조회합니다.")
    @GetMapping("/restaurant/{restaurantUuid}/sorted")
    public ApiResponseDTO<Page<ProductResponseDto>> getProductsSortedBy(
            @PathVariable(name = "restaurantUuid") UUID restaurantUuid,  // 명시적 이름 추가
            @RequestParam(name = "sortBy", defaultValue = "createdAt")
            @Parameter(description = "정렬 기준 (createdAt, updatedAt)", example = "createdAt") String sortBy,

            @RequestParam(name = "ascending", defaultValue = "true")
            @Parameter(description = "오름차순 정렬 여부 (true: 오름차순, false: 내림차순)", example = "true") boolean ascending,

            @RequestParam(name = "page", defaultValue = "0")
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,

            @RequestParam(name = "size", defaultValue = "10")
            @Parameter(description = "한 페이지당 상품 개수", example = "10") int size
    ) {
        return productService.getProductsSortedBy(restaurantUuid, sortBy, ascending, page, size);
    }

}

