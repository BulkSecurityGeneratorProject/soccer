package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Player event handler
 *
 * @author lin.peng
 * @since 2017-10-27
 */
@Service
public class PlayerEventHandler implements IEventHandler {

    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Player.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        Event event = new Event();
        Player player = (Player) after;
        // event.setAssociation(player.getTeam().getClub().getAssociation());
        event.setPlayer(player);
        event.setEventTime(LocalDate.now());
        event.setEventType(Constants.SoccerEventType.PLAYER_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {

    }
}
