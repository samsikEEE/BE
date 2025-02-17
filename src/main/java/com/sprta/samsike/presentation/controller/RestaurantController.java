package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.restaurant.RestaurantRequestDto;
import com.sprta.samsike.application.dto.restaurant.RestaurantRequestListDto;
import com.sprta.samsike.application.service.RestaurantService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
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
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto){
        restaurantService.createRestaurant(requestDto);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasAuthority('ROLE_OWNER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateStore(@PathVariable String restaurantId, @RequestBody RestaurantRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<?> result = restaurantService.updateRestaurant(restaurantId ,requestDto, userDetails.getMember());
        return ResponseEntity.ok(result);
    }


    @GetMapping
    public ResponseEntity<?> getRestaurantList(@ModelAttribute RestaurantRequestListDto requestDto , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDTO<?> result = restaurantService.getRestaurantList(requestDto , userDetails.getMember());
        return ResponseEntity.ok(result);
    }

}
