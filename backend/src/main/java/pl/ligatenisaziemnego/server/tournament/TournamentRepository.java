package pl.ligatenisaziemnego.server.tournament;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    @EntityGraph(attributePaths = {"knockoutBracket", "scoring.ratingForKnockoutStageParticipation", "groups.playerIds"})
    @Override
    @Nonnull
    Optional<Tournament> findById(@Nonnull Long id);


    @EntityGraph(attributePaths = {"players"})
    List<Tournament> findAllByNameContainsIgnoreCase(String name);

    @EntityGraph(attributePaths = {"knockoutBracket", "scoring.ratingForKnockoutStageParticipation", "groups.playerIds"})
    List<Tournament> findByOrderByEndDateAsc();
}
