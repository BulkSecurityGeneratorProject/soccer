package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.DivisionEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DivisionEvent entity.
 */
@SuppressWarnings("unused")
public interface DivisionEventRepository extends JpaRepository<DivisionEvent,Long> {

}
