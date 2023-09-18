package com.sparta.springcafeservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 회원가입 -> Dto -> @Valid, 예외 메시지 반환
    // ConstraintViolationException => 정규 표현식 맞지 않을 경우

    // 정규 표현식이 아니라 유효성 검사 실패 -> 직접적인 호출을 했을 때
    // 그러나 @Valid같은 어노테이션을 활용햇을 때는 MethodArgumentNotValidException 가 발생


    // @Valid 유효성 걸렸을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // BindeResult 에서 모든 에러를 가지고 온다.
            // 해당 필드의 이름과 메시지를 담는다.
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
