package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Venue;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Venue entity.
 */
@SuppressWarnings("unused")
public interface VenueRepository extends JpaRepository<Venue,Long> {

}
