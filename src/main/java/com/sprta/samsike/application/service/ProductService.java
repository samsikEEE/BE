package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.product.ProductRequestDto;
import com.sprta.samsike.application.dto.product.ProductResponseDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.domain.persistence.jpa.ProductRepository;
import com.sprta.samsike.domain.persistence.jpa.RestaurantRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    // CREATE
    @Transactional
    public ApiResponseDTO<ProductResponseDto> createProduct(ProductRequestDto requestDto, Member member) {

        // 1. 권한 체크 (관리자나 레스토랑 소유자만 추가 가능하다는 가정)
        validateRole(member);

        // 2. 동일 이름의 상품이 존재하지 않는지 확인
        if (productRepository.existsByName(requestDto.getName())) {
            throw new CustomException(ErrorCode.PROD003, "상품명이 이미 존재합니다.");
        }

        // 3. 레스토랑 확인 (Fault Tolerance: 존재하지 않으면 예외 처리)
        Restaurant restaurant = restaurantRepository.findById(UUID.fromString(requestDto.getRestaurantID()))
                .orElseThrow(() -> new CustomException(ErrorCode.PROD001, "레스토랑이 존재하지 않습니다."));

        // 4. Product 객체 생성
        Product product = new Product(
                restaurant,                                 // 연관된 Restaurant 객체
                requestDto.getName(),                      // 상품 이름
                requestDto.getImageUrl(),                  // 상품 이미지
                requestDto.getDescription(),               // 상품 설명
                requestDto.getPrice()                      // 상품 가격
        );

        // 5. Product 저장
        productRepository.save(product);

        // 6. Response 생성 및 반환
        return new ApiResponseDTO<>("상품이 성공적으로 생성되었습니다.", new ProductResponseDto(product));
    }

    // READ (조회 - 모든 Role 가능)
    @Transactional(readOnly = true)
    public ApiResponseDTO<List<ProductResponseDto>> getProductsByRestaurant(UUID restaurantUuid) {

        // 레스토랑 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantUuid)
                .orElseThrow(() -> new CustomException(ErrorCode.PROD001, "레스토랑을 찾을 수 없습니다."));

        // 상품 리스트 조회
        List<Product> products = productRepository.findAllByRestaurantAndDeletedByIsNull(restaurant);

        // Product -> ProductResponseDto 변환
        List<ProductResponseDto> productDto = products.stream()
                .map(ProductResponseDto::new) // Product 데이터를 Response DTO로 변환
                .collect(Collectors.toList());

        // ApiResponseDTO에 감싸기
        return new ApiResponseDTO<>("조회에 성공했습니다.", productDto);
    }

    // 정렬 조회
    @Transactional(readOnly = true)
    public ApiResponseDTO<Page<ProductResponseDto>> getProductsSortedBy(
            UUID restaurantUuid, String sortBy, boolean ascending, int page, int size) {

        // 유효하지 않은 정렬 기준 예외 처리
        if (!sortBy.equals("createdAt") && !sortBy.equals("updatedAt")) {
            return new ApiResponseDTO<>("error", Page.empty()); // "status" 값에 "error", 빈 페이지 반환
        }

        // 정렬 객체 생성
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Pageable 객체 생성 (페이지 번호는 0부터 시작)
        Pageable pageable = PageRequest.of(page, size, sort);

        // Repository에서 페이지네이션 및 정렬 적용하여 데이터 조회
        //Page<Product> productPage = productRepository.findByRestaurantUuid(restaurantUuid, pageable);

        Page<Product> productPage = productRepository.findAllByRestaurantUuidAndDeletedByIsNull(restaurantUuid,pageable);

        // ProductResponseDto 변환
        Page<ProductResponseDto> responseDtos = productPage.map(ProductResponseDto::new);

        // 성공 응답 생성
        return new ApiResponseDTO<>("success", responseDtos);
    }

    // UPDATE
    @Transactional
    public ApiResponseDTO<ProductResponseDto> updateProduct(UUID productId, ProductRequestDto requestDto, Member member) {

        // 권한 체크
        validateRole(member);

        // 상품 조회
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorCode.PROD005, "상품을 찾을 수 없습니다."));

//        // 사용자의 권한 확인 (상품이 해당 사용자의 레스토랑에 속하는지 검증)
//        if (!product.getRestaurant().getMember().getEmail().equals(member.getEmail())) {
//            throw new CustomException(ErrorCode.AUTH001, "권한이 없습니다.");
//        }
        // 데이터 수정 및 활성화 상태 변경
        product.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getImageUrl());
        productRepository.save(product); // 저장

        // 수정된 데이터를 ResponseDto로 반환
        return new ApiResponseDTO<>("상품이 성공적으로 수정되었습니다.", new ProductResponseDto(product));
    }
    // 상품 숨김처리
    @Transactional
    public ApiResponseDTO<String> toggleProductVisibility(UUID productId, Member member) {

        // 권한 체크
        validateRole(member);

        // 상품 조회
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorCode.PROD005, "상품을 찾을 수 없습니다."));

        // 현재 상태 반전
        boolean newVisibility = !product.isVisible();
        product.toggleVisibility(newVisibility);

        // 저장
        productRepository.save(product);

        // 상태 변경 메시지 반환
        String status = newVisibility ? "활성화" : "숨김 처리";
        return new ApiResponseDTO<>("상품이 " + status + "되었습니다.", null);
    }

    // DELETE
    @Transactional
    public ApiResponseDTO<String> deleteProduct(UUID productId, Member member) {

        // 권한 체크
        validateRole(member);

        // 상품 조회
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorCode.PROD005, "상품을 찾을 수 없습니다."));

//        // 사용자의 권한 검증 (상품이 자신의 레스토랑에 속해 있는지 확인)
//        if (!product.getRestaurant().getMember().getEmail().equals(member.getEmail())) {
//            throw new CustomException(ErrorCode.AUTH001, "권한이 없습니다.");
//        }

        // 삭제 사용자 및 삭제 일시를 설정 (소프트 삭제 처리)
        product.setDeletedBy(member.getEmail()); // 삭제 요청한 사용자의 이메일 설정
        productRepository.save(product); // Soft delete를 위해 상태 업데이트

        // 성공 메시지 반환
        return new ApiResponseDTO<>("상품이 성공적으로 삭제되었습니다.", null);
    }

    private void validateRole(Member member) {
        if (!MemberRoleEnum.ROLE_MANAGER.toString().equals(member.getRole())&&!MemberRoleEnum.ROLE_OWNER.toString().equals(member.getRole())) {
            throw new CustomException(ErrorCode.AUTH001, "권한이 없습니다.");
        }
    }
}
