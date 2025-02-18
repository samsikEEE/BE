package com.sprta.samsike.application.dto.restaurant;
import com.sprta.samsike.application.dto.request.RequestListDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantRequestListDto extends RequestListDTO {

    @Schema(description = "가게 검색값" , example = "삼식")
    private String restaurantName;
    @Schema(description = "지역구 코드", example = "1110")
    private String ssgCode;
    @Schema(description = "카테고리 Id", example = "1123123-1231")
    private String categoryId;

}
