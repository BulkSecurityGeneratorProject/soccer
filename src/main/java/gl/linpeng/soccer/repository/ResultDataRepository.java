package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.ResultData;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ResultData entity.
 */
@SuppressWarnings("unused")
public interface ResultDataRepository extends JpaRepository<ResultData,Long> {

}
