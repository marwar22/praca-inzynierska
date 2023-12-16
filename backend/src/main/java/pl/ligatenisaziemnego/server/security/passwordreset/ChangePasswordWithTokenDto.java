package pl.ligatenisaziemnego.server.security.passwordreset;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordWithTokenDto {
    @NotBlank(message = "password can't be blank")
    private String password;
}
