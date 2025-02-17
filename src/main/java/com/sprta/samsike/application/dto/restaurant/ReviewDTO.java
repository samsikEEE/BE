package com.sprta.samsike.application.dto.restaurant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    @Schema(example = "4")
    private Integer rating;
    @Schema(example = "사장님이 맛있고 음식이 친절해요")
    private String comment;
}
