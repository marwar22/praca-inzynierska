package pl.ligatenisaziemnego.server.controlleradvice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        System.err.println("handleMethodArgumentNotValid");
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        int globalErrorIndex = 0;
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName() + "_" + (globalErrorIndex++), error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        System.err.printf("NotReadable %s%n", ex.getLocalizedMessage());
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                Map.of("NotReadable", ex.getMostSpecificCause().getMessage()));
        return super.handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        System.err.printf("NotWritable %s%n", ex.getLocalizedMessage());
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
                Map.of("NotWritable", ex.getMostSpecificCause().getMessage()));
        return super.handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        // Only for breakpoints
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler({ExceptionWithResponseEntity.class})
    ResponseEntity<?> handleExceptionWithResponseEntity(ExceptionWithResponseEntity ex) {
        return ex.getResponseEntity();
    }

    @ExceptionHandler({Exception.class})
    ResponseEntity<?> handleException(Exception ex) {
        return ExceptionWithResponseEntity.responseEntityFromException(ex);
    }

}