package pl.ligatenisaziemnego.server.controlleradvice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.transaction.TransactionSystemException;

import java.util.Map;

@Getter
@Setter
@ToString
public class ApiError {
    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public ApiError() {
    }

    public ApiError(HttpStatus status, String message, Map<String, String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static ResponseEntity<ApiError> responseEntity(HttpStatus status, String message, Map<String, String> errors) {
        return new ResponseEntity<>(new ApiError(status, message, errors), status);
    }

    public static ResponseEntity<ApiError> responseEntity(HttpStatus status, Map<String, String> errors) {
        String message = String.join(",\n", errors.values());
        return new ResponseEntity<>(new ApiError(status, message, errors), status);
    }

    private static ExceptionWithResponseEntity exception(HttpStatus status, String message, Map<String, String> errors) {
        return new ExceptionWithResponseEntity(responseEntity(status, message, errors));
    }

    private static ExceptionWithResponseEntity exception(HttpStatus status, Map<String, String> errors) {
        return new ExceptionWithResponseEntity(responseEntity(status, errors));
    }


    public static ExceptionWithResponseEntity BAD_REQUEST(Map<String, String> errors) {
        return ApiError.exception(HttpStatus.BAD_REQUEST, errors);
    }

    public static ExceptionWithResponseEntity NOT_FOUND(Map<String, String> errors) {
        return ApiError.exception(HttpStatus.NOT_FOUND, errors);
    }

    public static ExceptionWithResponseEntity INTERNAL_SERVER_ERROR(Map<String, String> errors) {
        return ApiError.exception(HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }

    public static ExceptionWithResponseEntity INTERNAL_SERVER_ERROR(String serverError) {
        return ApiError.exception(HttpStatus.INTERNAL_SERVER_ERROR, Map.of("server", serverError));
    }

    public static ExceptionWithResponseEntity INTERNAL_SERVER_ERROR(Exception e) {
        if (e instanceof TransactionSystemException transactionSystemException) {
            return ApiError.INTERNAL_SERVER_ERROR(
                    Map.of("TransactionSystemException", transactionSystemException.getMostSpecificCause().getMessage(),
                            "message", e.getMessage()));
        }
        if (e instanceof HttpMessageNotWritableException hmnwe) {
            return ApiError.INTERNAL_SERVER_ERROR(
                    Map.of("HttpMessageNotWritableException", hmnwe.getMostSpecificCause().getMessage(),
                            "message", e.getMessage()));

        }
        return ApiError.INTERNAL_SERVER_ERROR(e.getMessage());
    }

    public static ExceptionWithResponseEntity FORBIDDEN(String message) {
        return ApiError.exception(HttpStatus.FORBIDDEN, message, Map.of("auth", message));
    }

    public static ExceptionWithResponseEntity UNAUTHORIZED(String message) {
        return ApiError.exception(HttpStatus.UNAUTHORIZED, message, Map.of("auth", message));
    }

    public static ExceptionWithResponseEntity NOT_FOUND_ID(Class<?> type, Long id) {
        return ApiError.NOT_FOUND(Map.of("id", "There is no " + type.getSimpleName() + " with id = " + id));
    }

    public static ExceptionWithResponseEntity ANOTHER_WITH(Class<?> type, String field, Object value) {
        return ApiError.BAD_REQUEST(
                Map.of(field, "There is another " + type.getSimpleName() + " with " + field + " = " + value.toString()));
    }
}