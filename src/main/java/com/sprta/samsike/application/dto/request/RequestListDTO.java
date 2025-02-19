package com.sprta.samsike.application.dto.request;


import ch.qos.logback.core.util.StringUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class RequestListDTO {

    @Parameter(name = "page",description = "현재 페이지", example = "1")
    @Builder.Default
    private Integer page = 0;

    @Parameter(name = "pageSize", description = "페이지 사이즈", example = "10")
    @Builder.Default
    private Integer pageSize = 10;

    @Parameter(name="sortDirection", description = "정렬 조건", example = "asc")
    @Builder.Default
    private Sort.Direction sortDirection = Sort.Direction.DESC;

    @Parameter(name = "sortBy", description = "정렬할 컬럼" , example = "createdAt")
    @Builder.Default
    private String sortBy = "createdAt";


    public void setPage(Integer page){
        this.page = page - 1 ;
        if(page < 0) {
            this.page = 0;
        }
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if(pageSize < 1) this.pageSize = 1;
    }

    public void setSortDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("asc")) {
            this.sortDirection = Sort.Direction.ASC;
        }
    }

    public void setColumn(String sortBy) {
        this.sortBy = sortBy;
    }

    public Pageable getPageable() {
        if(StringUtil.isNullOrEmpty(sortBy)) {
            return PageRequest.of(page, pageSize, sortDirection);
        }else {
            return PageRequest.of(page, pageSize, sortDirection, sortBy);
        }
    }
}
