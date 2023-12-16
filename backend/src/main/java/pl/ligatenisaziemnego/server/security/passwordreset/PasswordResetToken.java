package pl.ligatenisaziemnego.server.security.passwordreset;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUser;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "application user can't be null")
    @ManyToOne
    @JoinColumn(name = "application_user_id", unique = true)
    private ApplicationUser applicationUser;


    @NotBlank(message = "token can't be blank")
    @Column(name = "token", nullable = false)
    private String token;

    @NotNull(message = "expirationDate")
    @Column(name = "expiration_date")
    private Instant expirationDate;
}