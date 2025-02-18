package com.sprta.samsike.application.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private UUID uuid;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
