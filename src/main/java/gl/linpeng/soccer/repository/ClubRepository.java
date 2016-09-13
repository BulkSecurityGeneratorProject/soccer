package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Club;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Club entity.
 */
@SuppressWarnings("unused")
public interface ClubRepository extends JpaRepository<Club,Long> {

}
