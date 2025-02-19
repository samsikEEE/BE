package com.sprta.samsike.application.dto.restaurant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    @NotNull
    @Min(1) @Max(5)
    @Schema(example = "3")
    private Integer rating;

    @Schema(example = "사장님이 맛있고 음식이 친절해요")
    private String comment;
}
