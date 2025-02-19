package com.sprta.samsike.application.dto.request;


import ch.qos.logback.core.util.StringUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Builder.Default
    private Sort.Direction sortDirection = Sort.Direction.DESC;

    @Builder.Default
    private String SortColumn = "createdAt";


    public void setPage(Integer page){
        if(page < 0) {
            this.page = 0;
        }
        this.page = page - 1 ;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize < 1) this.pageSize = 1;
        this.pageSize = pageSize;
    }

    public void setSortDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("asc")) {
            this.sortDirection = Sort.Direction.ASC;
        }
    }

    public void setColumn(String column) {
        this.SortColumn = column;
    }

    public Pageable getPageable() {
        if(StringUtil.isNullOrEmpty(SortColumn)) {
            return PageRequest.of(page, pageSize, sortDirection);
        }else {
            return PageRequest.of(page, pageSize, sortDirection, SortColumn);
        }
    }
}
