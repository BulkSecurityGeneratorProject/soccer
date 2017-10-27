package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Team event handler
 *
 * @author lin.peng
 * @since 2017-10-27
 */
@Service
public class TeamEventHandler implements IEventHandler {

    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Team.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        Event event = new Event();
        Team team = (Team) after;
        // event.setAssociation(team.getClub().getAssociation());
        event.setTeam(team);
        event.setEventTime(LocalDate.now());
        event.setEventType(Constants.SoccerEventType.TEAM_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {
        Team teamAfter = (Team) after;
        Team teamBefore = (Team) before;

        // division event
        DivisionEvent divisionEventAfter = teamAfter.getDivisionEvent();
        DivisionEvent divisionEventBefore = teamBefore.getDivisionEvent();

        if (!Objects.equals(divisionEventAfter, divisionEventBefore)) {
            Event event = new Event();
            event.setTeam(teamAfter);
            event.setEventTime(LocalDate.now());
            if (divisionEventAfter != null && divisionEventBefore == null) {
                // join a new division event
                event.setDivisionEvent(divisionEventAfter);
                event.setEventType(Constants.SoccerEventType.TEAM_JOIN_DIVISION_EVENT.getValue());
            } else if (divisionEventBefore != null && divisionEventAfter == null) {
                // leave from a division event
                event.setDivisionEvent(divisionEventBefore);
                event.setEventType(Constants.SoccerEventType.TEAM_LEAVE_DIVISION_EVENT.getValue());
            } else {
                // CHANGE
                event.setDivisionEvent(divisionEventAfter);
                event.setEventType(Constants.SoccerEventType.TEAM_UPDATE_DIVISION_EVENT.getValue());
            }
            eventRepository.save(event);
        }
    }
}
