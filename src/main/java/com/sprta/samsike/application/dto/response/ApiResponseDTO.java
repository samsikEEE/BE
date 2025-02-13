package com.sprta.samsike.application.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    public String status;
    private T  data;
}
