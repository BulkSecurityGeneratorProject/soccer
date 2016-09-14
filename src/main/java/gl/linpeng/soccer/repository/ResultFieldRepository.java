package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.ResultField;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ResultField entity.
 */
@SuppressWarnings("unused")
public interface ResultFieldRepository extends JpaRepository<ResultField,Long> {

}
