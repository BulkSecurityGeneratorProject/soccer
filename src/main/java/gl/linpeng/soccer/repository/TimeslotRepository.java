package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Timeslot;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Timeslot entity.
 */
@SuppressWarnings("unused")
public interface TimeslotRepository extends JpaRepository<Timeslot,Long> {

}
