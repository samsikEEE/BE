package com.sprta.samsike.presentation.controller;


import com.sprta.samsike.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorys")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.ok().body(categoryService.getCategory());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody String category) {
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
