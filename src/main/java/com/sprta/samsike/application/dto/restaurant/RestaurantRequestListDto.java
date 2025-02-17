package com.sprta.samsike.application.dto.restaurant;
import com.sprta.samsike.application.dto.request.RequestListDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantRequestListDto extends RequestListDTO {

    private String restaurantName;
    private String ssgCode;
    private String categoryId;

}
