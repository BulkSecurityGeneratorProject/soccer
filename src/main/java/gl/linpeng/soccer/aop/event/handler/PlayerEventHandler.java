package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;

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
        Player playerAfter = (Player) after;
        Player playerBefore = (Player) before;

        // status
        Dict statusAfter = playerAfter.getStatus();
        Dict statusBefore = playerBefore.getStatus();
        if (!Objects.equals(statusAfter, statusBefore)) {

            if (statusAfter != null && statusAfter.getCode() != null) {
                Event event = new Event();
                event.setPlayer(playerAfter);
                event.setEventTime(LocalDate.now());
                if (statusAfter.getCode().equalsIgnoreCase(Constants.SOCCER_STATUS_INACTIVE)) {
                    event.setEventType(Constants.SoccerEventType.PLAYER_INACTIVE.getValue());
                } else if (statusAfter.getCode().equalsIgnoreCase(Constants.SOCCER_STATUS_CANCEL)) {
                    event.setEventType(Constants.SoccerEventType.PLAYER_CANCEL.getValue());
                }
                eventRepository.save(event);
            }

        }

        // team
        Team teamAfter = playerAfter.getTeam();
        Team teamBefore = playerBefore.getTeam();

        if (!Objects.equals(teamAfter, teamBefore)) {
            Event event = new Event();
            event.setPlayer(playerAfter);
            event.setEventTime(LocalDate.now());
            if (teamAfter != null && teamBefore == null) {
                // accession
                event.setTeam(teamAfter);
                event.setEventType(Constants.SoccerEventType.PLAYER_ACCESSION.getValue());
            } else if (teamBefore != null && teamAfter == null) {
                // dimission
                event.setTeam(teamBefore);
                event.setEventType(Constants.SoccerEventType.PLAYER_DIMISSION.getValue());
            } else {
                // transfer
                event.setTeam(teamBefore);
                event.setTargetTeam(teamAfter);
                event.setEventType(Constants.SoccerEventType.PLAYER_TRANSFER.getValue());
            }
            eventRepository.save(event);
        }

    }
}
