package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.RankingRule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RankingRule entity.
 */
@SuppressWarnings("unused")
public interface RankingRuleRepository extends JpaRepository<RankingRule,Long> {

}
