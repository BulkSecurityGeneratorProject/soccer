package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.DictKind;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DictKind entity.
 */
@SuppressWarnings("unused")
public interface DictKindRepository extends JpaRepository<DictKind,Long> {

}
