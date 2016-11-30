package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Player;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing Player.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class PlayerResourceExt {

	private final Logger log = LoggerFactory.getLogger(PlayerResourceExt.class);

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * GET /division-events : get the player career data.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of career
	 *         data in body
	 */
	@RequestMapping(value = "/players/{id}/career", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Player> getPlayerCareer(@PathVariable Long id) {
		log.debug("REST request to get career data of Player: {}", id);

		String sql = String
				.format("SELECT "
						+ "p.id,"
						+ "p.NAME,"
						+ "d.NAME as division_name,"
						+ "s.NAME as season_name,"
						+ "tm.name as team_name,"
						+ "de.id as division_event_id,"
						+ "sum(CASE rf.name WHEN '进球' THEN rd.value END)  goal,"
						+ "sum(CASE rf.name WHEN '助攻' THEN rd.value END)  assist "
						+ "FROM "
						+ "Player p "
						+ "LEFT OUTER JOIN SQUAD_PLAYER sp "
						+ "LEFT OUTER JOIN Result_data rd "
						+ "LEFT OUTER JOIN Game g "
						+ "LEFT OUTER JOIN Timeslot tl "
						+ "LEFT OUTER JOIN division_event de "
						+ "LEFT OUTER JOIN season s ON s.id = de.season_id "
						+ "LEFT OUTER JOIN division d ON d.id = de.division_id "
						+ "ON de.id = tl.division_event_id ON tl.id = g.timeslot_id "
						+ "ON g.id = rd.game_id "
						+ "left outer join result_field rf on rf.id=rd.result_field_id ON rd.squad_player_id = sp.id "
						+ "left outer join squad sq "
						+ "left outer join Team tm on tm.id = sq.team_id on sq.id = sp.squad_id "
						+ "ON sp.player_id = p.id WHERE p.id = "
						+ id
						+ " group by p.id,p.NAME,d.NAME,s.name,de.id order by s.name desc");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}
}
