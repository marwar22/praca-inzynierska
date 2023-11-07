package pl.ligatenisaziemnego.server.controlleradvice;

import org.springframework.http.ResponseEntity;

public class ExceptionWithResponseEntity extends Exception implements WithResponseEntity {
    private final ResponseEntity<?> responseEntity;

    public ExceptionWithResponseEntity(ResponseEntity<?> responseEntity) {
        super("No message provided (ExceptionWithResponseEntity), this exception should be caught (probable cause: missing implementation (missing correct catch in some controller)))");
        this.responseEntity = responseEntity;
    }

    @Override
    public ResponseEntity<?> getResponseEntity() {
        return responseEntity;
    }

    public static ResponseEntity<?> responseEntityFromException(Exception e) {
        if (e instanceof ExceptionWithResponseEntity exceptionWithResponseEntity) {
            return exceptionWithResponseEntity.getResponseEntity();
        }
        return ApiError.INTERNAL_SERVER_ERROR(e).getResponseEntity();
    }
}