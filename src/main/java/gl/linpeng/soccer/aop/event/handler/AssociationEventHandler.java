package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.config.Constants;
import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.domain.Event;
import gl.linpeng.soccer.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    }
}
