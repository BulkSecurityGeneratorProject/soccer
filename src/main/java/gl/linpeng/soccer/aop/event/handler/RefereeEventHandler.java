package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.domain.Referee;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;

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
        Referee refereeAfter = (Referee) after;
        Referee refereeBefore = (Referee) before;

        // status
        Dict statusAfter = refereeAfter.getStatus();
        Dict statusBefore = refereeBefore.getStatus();

        if (!Objects.equals(statusAfter, statusBefore)) {

            if (statusAfter != null && statusAfter.getCode() != null) {
                Event event = new Event();
                event.setAssociation(refereeAfter.getAssociation());
                event.setReferee(refereeAfter);
                event.setEventTime(LocalDate.now());
                if (statusAfter.getCode().equalsIgnoreCase(Constants.SOCCER_STATUS_INACTIVE)) {
                    event.setEventType(Constants.SoccerEventType.REFEREE_INACTIVE.getValue());
                } else if (statusAfter.getCode().equalsIgnoreCase(Constants.SOCCER_STATUS_CANCEL)) {
                    event.setEventType(Constants.SoccerEventType.REFEREE_CANCEL.getValue());
                }
                eventRepository.save(event);
            }
        }

    }
}
