package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Coach;
import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.domain.Event;
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
public class CoachEventHandler implements IEventHandler {

    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Coach.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        Event event = new Event();
        Coach coach = (Coach) after;
        event.setAssociation(coach.getAssociation());
        event.setCoach(coach);
        event.setEventTime(LocalDate.now());
        event.setEventType(Constants.SoccerEventType.COACH_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {
        Coach coachAfter = (Coach) after;
        Coach coachBefore = (Coach) before;

        Dict statusAfter = coachAfter.getStatus();
        Dict statusBefore = coachBefore.getStatus();
        // status
        if (!Objects.equals(statusAfter, statusBefore)) {
            if (statusAfter != null && statusAfter.getCode() != null) {
                Event event = new Event();
                event.setAssociation(coachAfter.getAssociation());
                event.setCoach(coachAfter);
                event.setEventTime(LocalDate.now());
                if (statusAfter.getCode().equalsIgnoreCase(Constants.SOCCER_STATUS_INACTIVE)) {
                    event.setEventType(Constants.SoccerEventType.COACH_INACTIVE.getValue());
                } else if (statusAfter.getCode().equalsIgnoreCase(Constants.SOCCER_STATUS_CANCEL)) {
                    event.setEventType(Constants.SoccerEventType.COACH_CANCEL.getValue());
                }
                eventRepository.save(event);
            }

        }

        // team
        Team teamAfter = coachAfter.getTeam();
        Team teamBefore = coachBefore.getTeam();
        if (!Objects.equals(teamAfter, teamBefore)) {
            Event event = new Event();
            event.setAssociation(coachAfter.getAssociation());
            event.setCoach(coachAfter);
            event.setEventTime(LocalDate.now());
            if (teamAfter != null && teamBefore == null) {
                // accession
                event.setTeam(teamAfter);
                event.setEventType(Constants.SoccerEventType.COACH_ACCESSION.getValue());
            } else if (teamBefore != null && teamAfter == null) {
                // dimission
                event.setTeam(teamBefore);
                event.setEventType(Constants.SoccerEventType.COACH_DIMISSION.getValue());
            } else {
                // transfer
                event.setTeam(coachBefore.getTeam());
                event.setTargetTeam(coachAfter.getTeam());
                event.setEventType(Constants.SoccerEventType.COACH_TRANSFER.getValue());
            }
            eventRepository.save(event);
        }
    }
}
