package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Club;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Club event handler
 *
 * @author lin.peng
 * @since 2017-10-27
 */
@Service
public class ClubEventHandler implements IEventHandler {

    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Club.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        Event event = new Event();
        Club club = (Club) after;
        event.setAssociation(club.getAssociation());
        event.setClub(club);
        event.setEventTime(club.getCreateAt());
        event.setEventType(Constants.SoccerEventType.CLUB_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {
        Club clubAfter = (Club) after;
        Club clubBefore = (Club) before;
        if (!Objects.equals(clubAfter.getVenue(), clubBefore.getVenue())) {
            Event event = new Event();
            event.setAssociation(clubAfter.getAssociation());
            event.setEventTime(LocalDate.now());
            event.setEventType(Constants.SoccerEventType.CLUB_CHANGE_VENUE.getValue());
            eventRepository.save(event);
        }
    }
}
