package fiveonestudy.ddait.global.handler;

import fiveonestudy.ddait.global.exception.ForbiddenException;
import fiveonestudy.ddait.global.exception.NotFoundException;
import fiveonestudy.ddait.global.exception.UnauthorizedException;
import fiveonestudy.ddait.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("INVALID_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail("SERVER_ERROR", "서버 내부 오류"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail("INTERNAL_SERVER_ERROR", "서버 오류"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(401)
                .body(ApiResponse.fail("UNAUTHORIZED", e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<?>> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(403)
                .body(ApiResponse.fail("FORBIDDEN", e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.fail("NOT_FOUND", e.getMessage()));
    }
}
