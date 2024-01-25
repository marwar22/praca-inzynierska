package pl.ligatenisaziemnego.server.applicationuser;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ApplicationUserPermission {
    TOURNAMENT__CREATE("TOURNAMENT:CREATE"),
    TOURNAMENT__UPDATE_ANY("TOURNAMENT:UPDATE_ANY"),
    ROLE__CHANGE("ROLE:CHANGE"),
    USER__GET_ANY("USER:GET_ANY");

    @JsonValue
    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
