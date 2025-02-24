package com.sprta.samsike.presentation.controller;


import com.sprta.samsike.application.service.CategoryService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorys")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 전체 조회", description = "카테고리를 조회 합니다.")
    public ResponseEntity<?> getCategory(@RequestParam("category") @Parameter(name = "category") String category) {
        return ResponseEntity.ok(categoryService.getCategory(category));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MASTER') or hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성 합니다.")
    public ResponseEntity<?> createCategory(@RequestBody String category) {
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ROLE_MASTER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") String categoryId , @AuthenticationPrincipal  UserDetailsImpl userDetails) {
        categoryService.deleteCategory(categoryId, userDetails.getMember());
        return ResponseEntity.noContent().build();
    }
}
