package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.domain.Referee;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Referee event handler
 *
 * @author lin.peng
 * @since 2017-10-27
 */
@Service
public class RefereeEventHandler implements IEventHandler {

    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Referee.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        Event event = new Event();
        Referee referee = (Referee) after;
        event.setAssociation(referee.getAssociation());
        event.setReferee(referee);
        event.setEventTime(LocalDate.now());
        event.setEventType(Constants.SoccerEventType.REFEREE_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {

    }
}
