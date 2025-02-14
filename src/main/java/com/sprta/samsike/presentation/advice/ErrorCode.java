package com.sprta.samsike.presentation.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // member
    MEMB001("MEBE001","일치하는 아이디 없음"),
    MEMB002("MEBE002","로그인 정보 불일치"),

    // restaurant
    REST001("REST001",""),

    //category
    CATE001("CATE001",""),

    // order
    ORDER001("ORDER001", "존재하지 않는 주문입니다."),
    ORDER002("ORDER002", "관리자는 특정 가게의 주문만 조회 가능."),
    ORDER003("ORDER003", "자신의 주문만 조회할 수 있음."),
    ORDER004("ORDER004", "주문을 취소할 수 있는 시간이 초과."),
    ORDER005("ORDER005", "주문을 취소 권한이 없음."),
    ORDER006("ORDER006", "주문을 취소할 수 있는 시간이 경과."),

    //token
    AUTH001("AUTH001","유효하지 않은 토큰"),

    //UNKNOWN_ERROR
    UNKNOWN_ERROR("UNKNOWN_ERROR","예외 처리 하지 않은 에러")


    ;

    private final String code;
    private final String message;
}
