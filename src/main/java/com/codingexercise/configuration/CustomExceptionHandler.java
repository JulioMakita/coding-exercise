package com.codingexercise.configuration;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.codingexercise.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<CustomErrorResponse> handleNotFoundException(Exception ex,
      HttpServletRequest request) {

    CustomErrorResponse errors = new CustomErrorResponse(LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
        request.getRequestURI(), Arrays.asList(ex.getMessage()));

    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<CustomErrorResponse> handleConstraintViolationException(Exception ex,
      HttpServletRequest request) {
    ConstraintViolationException exception = (ConstraintViolationException) ex;

    List<String> stringList = exception.getConstraintViolations().stream()
        .map(e -> e.getPropertyPath() + ": " + e.getMessage()).collect(Collectors.toList());

    CustomErrorResponse errors =
        new CustomErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), request.getRequestURI(), stringList);

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(x -> x.getDefaultMessage()).collect(Collectors.toList());

    CustomErrorResponse errors = new CustomErrorResponse(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ((ServletWebRequest) request).getRequest().getRequestURI(), fieldErrors);

    return new ResponseEntity<>(errors, headers, status);
  }
}
