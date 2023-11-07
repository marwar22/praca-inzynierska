package pl.ligatenisaziemnego.server.security;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters long")
    private String username;

    @NotEmpty(message = "Email can't be empty")
    @ValidEmail(message = "Email must be vaild email")
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;
}
