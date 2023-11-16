package com.tickerBell.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    NON_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "비회원 정보가 존재하지 않습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 access token 입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 refresh token 입니다."),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "잘못된 refresh token 요청 입니다."),
    REFRESH_TOKEN_UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "refresh token 재발급 중 알 수 없는 에러 발생."),
    AUTHORIZATION_HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Authorization 헤더가 필요한 요청입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다."),

    REQUEST_INVALID(HttpStatus.BAD_REQUEST, "valid 옵션에 맞지 않는 형식입니다."),
    IMAGE_NOT_SUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식 입니다. jpg, png 형식으로 보내주세요."),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "저장되지 않은 이미지입니다."),
    IMAGE_NOT_FOUND_EXTENSION(HttpStatus.BAD_REQUEST, "확장자를 찾을 수 없습니다."),
    IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지 처리 중 에러 발생"),
    EVENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록되지 않은 Event 입니다."),
    EVENT_CANCEL_FAIL(HttpStatus.BAD_REQUEST, "이벤트 취소에 실패하였습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록되지 않은 Category 입니다."),
    SEAT_INFO_NOT_VALID_FORMAT(HttpStatus.BAD_REQUEST, "선택 좌석의 형식이 올바르지 않습니다. A-1, B-1 형식으로 보내주세요"),
    SALE_DEGREE_NOT_VALID_FORMAT(HttpStatus.BAD_REQUEST, "세일 degree 형식이 올바르지 않습니다."),
    ALREADY_SELECTED_SEAT(HttpStatus.BAD_REQUEST, "이미 선택된 좌석입니다. 다른 좌석을 선택해주세요"),
    SMS_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "sms 문자 전송 중 에러가 발생했습니다."),
    TICKETING_NOT_FOUND(HttpStatus.BAD_REQUEST, "예매내역이 존재하지 않습니다."),
    TICKETING_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "티켓 삭제 중 에러가 발생했습니다."),
    MAP_API_CALL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "naver map api 호출 중 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String errorMessage;

}
