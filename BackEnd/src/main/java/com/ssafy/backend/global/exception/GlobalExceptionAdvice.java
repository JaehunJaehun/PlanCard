package com.ssafy.backend.global.exception;

import com.ssafy.backend.global.common.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static com.ssafy.backend.global.exception.GlobalError.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 권한없는 사용자 요청 처리
     * @param exception
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Message<Void>> accessDeniedExceptionHandle(AccessDeniedException exception) {
        log.warn("인가에러 ex : {}", (Object[]) exception.getStackTrace());

        return ResponseEntity.status(FORBIDDEN_EXCEPTION.getHttpStatus())
                .body(Message.fail(null, FORBIDDEN_EXCEPTION.getErrorMessage()));
    }

    /**
     * 인증되지 않은 사용자 요청 처리
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Message<Void>>  authenticationExceptionHandle(AuthenticationException exception) {
        log.warn("인증에러 ex : {}", (Object[]) exception.getStackTrace());

        return ResponseEntity.status(AUTHENTICATION_EXCEPTION.getHttpStatus())
                .body(Message.fail(null, AUTHENTICATION_EXCEPTION.getErrorMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Message<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.warn("파일 업로드 에러 ex : {}", (Object[]) exception.getStackTrace());
        return ResponseEntity.status(MAXIMUM_UPLOAD_SIZE.getHttpStatus())
                .body(Message.fail(null, MAXIMUM_UPLOAD_SIZE.getErrorMessage()));
    }

    /**
     * 기본 에러 처리
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message<Void>>  defaultExceptionHandle(Exception exception) {
        log.warn("기본에러 ex : {}", (Object[]) exception.getStackTrace());

        return ResponseEntity.status(DEFAULT_EXCEPTION.getHttpStatus())
                .body(Message.fail(null, DEFAULT_EXCEPTION.getErrorMessage()));
    }
}
