package gl.linpeng.soccer.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import gl.linpeng.soccer.domain.Player;

import org.springframework.data.jpa.domain.Specification;

/**
 * A Player specification
 * 
 * @author linpeng
 *
 */
public class PlayerSpecifications {

	/**
	 * Get all player data by team id
	 * 
	 * @param id
	 * @return
	 */
	public static Specification<Player> findByTeam(Long id) {

		return new Specification<Player>() {

			@Override
			public Predicate toPredicate(Root<Player> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (null != id) {
					predicates.add(cb.equal(root.get("team").get("id"), id));
				}
				return cb.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
