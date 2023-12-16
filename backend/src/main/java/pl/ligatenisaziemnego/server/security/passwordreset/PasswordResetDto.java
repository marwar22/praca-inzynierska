package pl.ligatenisaziemnego.server.security.passwordreset;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDto {
    @NotBlank(message = "email can't be blank")
    private String email;
}