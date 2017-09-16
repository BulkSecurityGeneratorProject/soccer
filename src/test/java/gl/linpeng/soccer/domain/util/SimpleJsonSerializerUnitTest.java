package gl.linpeng.soccer.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Squad;
import gl.linpeng.soccer.domain.SquadPlayer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the SimpleJsonSerializer utility class.
 *
 * @author linpeng
 * @since 0.0.1
 */
public class SimpleJsonSerializerUnitTest {

    @Test
    public void testIsSerializeIdAndName() throws JsonProcessingException {
        SquadPlayer sp = new SquadPlayer();
        sp.setId(11L);
        sp.setPlayerNumber(9);
        Player player = new Player();
        player.setId(9L);
        player.setName("Jim Green");
        player.setHeight(199);
        player.setWeight(56);
        sp.setPlayer(player);
        Squad squad = new Squad();
        squad.setId(6L);
        sp.setSquad(squad);

        ObjectMapper mapper = new ObjectMapper();
        String string = mapper.writeValueAsString(sp);
        assertThat(string != null);
    }
}
