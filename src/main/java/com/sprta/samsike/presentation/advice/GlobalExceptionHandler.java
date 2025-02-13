package com.sprta.samsike.presentation.advice;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDTO<ErrorData>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorData errorData = new ErrorData(
                errorCode.getCode(),
                errorCode.getMessage(),
                e.getDetails()
        );

        ApiResponseDTO<ErrorData> response = new ApiResponseDTO<>(
                "fail", errorData
        );

        return ResponseEntity.ok(response);

    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiException> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ApiException restApiException = new ApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(restApiException,HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ApiException> nullPointerExceptionHandler(NullPointerException ex) {
        ApiException restApiException = new ApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(restApiException,HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({EntityNotFoundException.class, ChangeSetPersister.NotFoundException.class})
    public ResponseEntity<ApiException> entityNotFoundExceptionHandler(RuntimeException ex) {
        ApiException restApiException = new ApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(restApiException, HttpStatus.NOT_FOUND);
    }

    // 404 응답을 직접 반환
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiException> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiException restApiException = new ApiException("요청한 페이지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(restApiException, HttpStatus.NOT_FOUND);
    }

}
