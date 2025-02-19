package com.sprta.samsike.application.dto.restaurant;
import com.sprta.samsike.application.dto.request.RequestListDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantRequestListDto extends RequestListDTO {

    @Parameter(name = "restaurantName", description = "가게 이름" ,example = "삼식이")
    private String restaurantName;

    @Parameter(name = "ssgCode", description = "지역구 코드" ,example = "11110")
    private String ssgCode;

    @Parameter(name = "categoryId", description = "카테고리 Id" )
    private String categoryId;

}
