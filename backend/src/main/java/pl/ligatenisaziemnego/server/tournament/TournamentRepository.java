package pl.ligatenisaziemnego.server.tournament;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    @EntityGraph(attributePaths = {"players"})
    @Override
    @Nonnull
    @Query("select t from Tournament t")
    List<Tournament> findAll();
}
