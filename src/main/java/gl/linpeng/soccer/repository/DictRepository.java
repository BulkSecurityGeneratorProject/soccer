package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Dict;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dict entity.
 */
@SuppressWarnings("unused")
public interface DictRepository extends JpaRepository<Dict,Long> {

}
