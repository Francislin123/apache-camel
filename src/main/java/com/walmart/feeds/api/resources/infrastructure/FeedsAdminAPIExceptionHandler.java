package com.walmart.feeds.api.resources.infrastructure;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.resources.feed.response.ErrorResponse;
import com.walmart.feeds.api.resources.feed.response.FieldValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.stream.Collectors;

@EnableWebMvc
@RestControllerAdvice
public class FeedsAdminAPIExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(MethodArgumentNotValidException ex, WebRequest request) {

        ErrorResponse e = new ErrorResponse();

        e.setDescription("Invalid Request");
        e.setCode(HttpStatus.BAD_REQUEST.toString());
        e.setFieldValidations(ex.getBindingResult().getAllErrors().stream().map(b -> (FieldError) b).map(f -> new FieldValidation(f.getField(), f.getDefaultMessage(), f.getRejectedValue())).collect(Collectors.toList()));

        return ResponseEntity.badRequest().body(e);

    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(NotFoundException ex, WebRequest request) {
        logger.info("An not found error occurred", ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(DataIntegrityViolationException ex, WebRequest request) {
        logger.info("A conflict error occurred", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponse e = new ErrorResponse();
        e.setDescription(ex.getMessage());

        logger.error("An unhandled error occurred", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }

}
