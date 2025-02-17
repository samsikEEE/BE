package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.restaurant.RestaurantRequestDto;
import com.sprta.samsike.application.dto.restaurant.RestaurantRequestListDto;
import com.sprta.samsike.application.service.RestaurantService;
import com.sprta.samsike.infrastructure.persistence.jpa.RestaurantRepository;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "가게 생성", description = "가게를 생성합니다.")
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto){
        restaurantService.createRestaurant(requestDto);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "가게 수정", description = "가게를 수정합니다.")
    public ResponseEntity<?> updateStore(@PathVariable String restaurantId, @RequestBody RestaurantRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<?> result = restaurantService.updateRestaurant(restaurantId ,requestDto, userDetails.getMember());
        return ResponseEntity.ok(result);
    }


    @GetMapping
    @Operation(summary = "가게 조회", description = "가게 목록을 조회 합니다.")
    public ResponseEntity<?> getRestaurantList(@ModelAttribute RestaurantRequestListDto requestDto , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<?> result = restaurantService.getRestaurantList(requestDto , userDetails.getMember());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "가게 삭제", description = "가게를 삭제 합니다.")
    public ResponseEntity<?> deleteRestaurant(@PathVariable("restaurantId") String restaurantId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        restaurantService.deleteRestaurant(restaurantId, userDetails.getMember());

        return ResponseEntity.noContent().build();
    }
}
