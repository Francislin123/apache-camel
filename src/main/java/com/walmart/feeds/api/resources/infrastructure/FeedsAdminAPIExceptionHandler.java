package com.walmart.feeds.api.resources.infrastructure;

import com.walmart.feeds.api.core.exceptions.*;
import com.walmart.feeds.api.resources.common.response.ErrorResponse;
import com.walmart.feeds.api.resources.common.response.FieldError;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.ServletException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class FeedsAdminAPIExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedsAdminAPIExceptionHandler.class);
    private static final String DEFAULT_ERROR_MESSAGE = "An unhandled error occurred";
    private static final String ERROR_MESSAGE = "An user error occurred";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(MethodArgumentNotValidException ex, WebRequest request) {

        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.toString())
                        .description("Invalid Request")
                        .errors(ex.getBindingResult().getAllErrors().stream().map(b ->
                                (org.springframework.validation.FieldError) b).map(f -> new FieldError(f.getField(), f.getDefaultMessage(), f.getRejectedValue()))
                                .collect(Collectors.toList()))
                        .build());

    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(EntityNotFoundException ex, WebRequest request) {
        LOGGER.info("An not found error occurred", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(EntityAlreadyExistsException ex, WebRequest request) {
        LOGGER.info("A conflict error occurred", ex);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorResponse> bindExceptionHandler(BindException ex, WebRequest request) {
        LOGGER.error(DEFAULT_ERROR_MESSAGE, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.toString())
                        .description("Invalid Request")
                        .errors(ex.getBindingResult().getAllErrors().stream().map(b ->
                                (org.springframework.validation.FieldError) b).map(f -> new FieldError(f.getField(), f.getDefaultMessage(), f.getRejectedValue()))
                                .collect(Collectors.toList()))
                        .build());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception ex, WebRequest request) {
        LOGGER.error(DEFAULT_ERROR_MESSAGE, ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .description(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = SystemException.class)
    public ResponseEntity<ErrorResponse> systemExceptionHandler(SystemException ex, WebRequest request) {
        LOGGER.error(DEFAULT_ERROR_MESSAGE, ex);

        return ResponseEntity.status(ex.getErrorCode())
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {
            MissingServletRequestPartException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponse> servletExceptionHandler(ServletException ex, WebRequest request) {
        LOGGER.error(ERROR_MESSAGE, ex);

        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ErrorResponse> userExceptionHandler(UserException ex, WebRequest request) {
        LOGGER.error(ERROR_MESSAGE, ex);

        if (ex instanceof InvalidFileException) {

            InvalidFileException invalidFileException = (InvalidFileException) ex;

            return ResponseEntity.status(ex.getErrorCode())
                    .body(ErrorResponse.builder()
                            .code(ex.getErrorCode().toString())
                            .description(ex.getMessage())
                            .errors(invalidFileException.getErrors())
                            .build());
        }

        return ResponseEntity.status(ex.getErrorCode())
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .errors(ex.getErrors())
                        .build());
    }

    @ExceptionHandler(value = InvalidFileException.class)
    public ResponseEntity<ErrorResponse> invalidFileExceptionHandler(InvalidFileException ex, WebRequest request) {
        LOGGER.error(ERROR_MESSAGE, ex);

        return ResponseEntity.status(ex.getErrorCode())
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .errors(ex.getErrors())
                        .build());
    }

    @ExceptionHandler(value = CamelExecutionException.class)
    public ResponseEntity<ErrorResponse> camelExceptionHandler(CamelExecutionException ex, WebRequest request) {
        LOGGER.error(DEFAULT_ERROR_MESSAGE, ex);

        Exception camelExceptionCaught = ex.getExchange().getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (camelExceptionCaught instanceof UserException) {
            return userExceptionHandler((UserException) camelExceptionCaught, request);
        }

        return genericExceptionHandler(ex, request);
    }

}
