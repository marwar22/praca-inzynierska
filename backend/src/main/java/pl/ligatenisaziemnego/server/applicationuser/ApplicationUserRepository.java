package pl.ligatenisaziemnego.server.applicationuser;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    @Query("UPDATE ApplicationUser u SET u.lastLogin=:lastLogin WHERE u.username=:username")
    @Modifying
    @Transactional
    void updateLastLogin(@Param("username") String username, @Param("lastLogin") Date lastLogin);

    @Query("select (count(a) > 0) from ApplicationUser a where a.username = ?1")
    boolean existsByUsername(String username);

    @Query("select (count(a) > 0) from ApplicationUser a where a.email = ?1")
    boolean existsByEmail(String email);

    @Query("select a from ApplicationUser a where concat(upper(a.firstName), ' ', upper(a.lastName), ' ', upper(a.firstName)) like upper(concat('%', ?1, '%'))")
    List<ApplicationUser> findAllByFirstNameWithLastName(String name, Pageable pageable);

    ApplicationUser findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);
}