package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Association event handler
 *
 * @author lin.peng
 * @since 2017-10-26
 */
@Service
public class AssociationEventHandler implements IEventHandler {

    @Resource
    private EventRepository eventRepository;

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Association.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        // create association event
        Event event = new Event();
        Association association = (Association) after;
        event.setAssociation(association);
        event.setEventTime(association.getCreateAt());
        event.setEventType(Constants.SoccerEventType.ASSOCIATION_CREATE.getValue());
        eventRepository.save(event);
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {
        Association associationAfter = (Association) after;
        Association associationBefore = (Association) before;
        Dict statusAfter = associationAfter.getStatus();
        Dict statusBefore = associationBefore.getStatus();

        if (!Objects.equals(statusAfter, statusBefore)) {

            if (statusAfter != null && statusAfter.getCode() != null) {
                Event event = new Event();
                event.setAssociation(associationAfter);
                event.setEventTime(LocalDate.now());
                if (Constants.SOCCER_STATUS_INACTIVE.equalsIgnoreCase(statusAfter.getCode())) {
                    event.setEventType(Constants.SoccerEventType.ASSOCIATION_INACTIVE.getValue());
                } else if (Constants.SOCCER_STATUS_CANCEL.equalsIgnoreCase(statusAfter.getCode())) {
                    event.setEventType(Constants.SoccerEventType.ASSOCIATION_CANCEL.getValue());
                }
                eventRepository.save(event);
            }


        }
    }
}
