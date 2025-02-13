package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/category")
    public ResponseEntity<?> category() {
        return ResponseEntity.ok(restaurantService.getCategory());

    }


}
