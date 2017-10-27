package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Division-event event handler
 *
 * @author lin.peng
 * @since 2017-10-27
 */
@Service
public class DivisionEventEventHandler implements IEventHandler {
    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(DivisionEvent.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        Event event = new Event();
        DivisionEvent divisionEvent = (DivisionEvent) after;
        //event.setAssociation(divisionEvent.getDivision().getAssociation());
        event.setDivisionEvent(divisionEvent);
        event.setEventTime(LocalDate.now());
        event.setEventType(Constants.SoccerEventType.DIVISION_EVENT_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {

    }
}
