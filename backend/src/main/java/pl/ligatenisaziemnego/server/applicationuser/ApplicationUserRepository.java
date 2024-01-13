package pl.ligatenisaziemnego.server.applicationuser;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("select a from ApplicationUser a where a.id not in ?2 and upper(concat(a.firstName, ' ', a.lastName, ' ', a.firstName)) like upper(concat('%', ?1, '%'))")
    List<ApplicationUser> findAllByFirstNameWithLastNameAndIdNotIn(String name, List<Long> ids, Pageable pageable);

    @Query("select a from ApplicationUser a where upper(concat(a.firstName, ' ', a.lastName, ' ', a.firstName)) like upper(concat('%', ?1, '%'))")
    List<ApplicationUser> findAllByFirstNameWithLastName(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"roles"})
    @Query("select a from ApplicationUser a where a.username = ?1 or a.email = ?1")
    ApplicationUser findByUsernameOrEmail(String usernameOrEmail);

    Optional<ApplicationUser> findByEmail(String email);
}