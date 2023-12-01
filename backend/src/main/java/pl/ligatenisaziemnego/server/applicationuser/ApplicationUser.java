package pl.ligatenisaziemnego.server.applicationuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.ligatenisaziemnego.server.security.ValidEmail;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "username can't be empty")
    @Column(nullable = false, unique = true)
    private String username;


    @NotEmpty(message = "firstName can't be empty")
    @Column(nullable = false)
    private String firstName;

    @NotEmpty(message = "lastName can't be empty")
    @Column(nullable = false)
    private String lastName;

    @NotEmpty(message = "email can't be empty")
    @ValidEmail(message = "email must be vaild email")
    @Column(nullable = false, unique = true)
    private String email;

    @ToString.Exclude
    @NotEmpty(message = "password can't be empty")
    @JsonIgnore
    private String password;

    @Column(name = "last_login")
    private Date lastLogin;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;

    @NotNull(message = "roles can't be null")
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "role")
    @CollectionTable(name = "application_user_role")
    @Enumerated(EnumType.STRING)
    private List<ApplicationUserRole> roles;


    public ApplicationUser(String username, String email, String firstName, String lastName, String password,
            List<ApplicationUserRole> roles) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        var permissions = new HashSet<ApplicationUserPermission>();
        this.roles.forEach(role -> permissions.addAll(role.getPermissions()));
        return permissions;
    }
}
