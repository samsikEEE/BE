package com.sprta.samsike.application.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private T  data;
}
