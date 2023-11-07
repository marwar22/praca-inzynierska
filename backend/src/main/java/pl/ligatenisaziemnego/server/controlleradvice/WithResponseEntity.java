package pl.ligatenisaziemnego.server.controlleradvice;

import org.springframework.http.ResponseEntity;

public interface WithResponseEntity {
    ResponseEntity<?> getResponseEntity();
}
