package pl.ligatenisaziemnego.server.applicationuser;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationUserPermission {
    LEAGUE__CREATE("LEAGUE:CREATE"),
    USER__CHANGE("USER:CHANGE"),
    ROLE__CHANGE("ROLE:CHANGE");

    @JsonValue
    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
