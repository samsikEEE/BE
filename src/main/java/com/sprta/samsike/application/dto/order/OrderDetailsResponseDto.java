package com.sprta.samsike.application.dto.order;

import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.order.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderDetailsResponseDto {
    private UUID uuid;
    private String username;
    private String address;
    private String restorantname;
    private String type;
    private String status;
    private Integer amount;
    private String requirement;
    private Boolean isDisposal;
    private LocalDateTime createdAt;
    private List<OrderProductResponseDto> productList;

    public OrderDetailsResponseDto(Order order, List<OrderProductResponseDto> productList) {
        this.uuid = order.getUuid();
        this.username = order.getMember().getUsername();
        this.address = order.getUserRegion().getAddress();
        this.restorantname = order.getRestaurant().getName();
        this.type = order.getType();
        this.status = order.getStatus();
        this.amount = order.getAmount();
        this.requirement = order.getRequirement();
        this.isDisposal = order.getIsDisposal();
        this.createdAt = order.getCreatedAt();
        this.productList = productList;
    }
}
