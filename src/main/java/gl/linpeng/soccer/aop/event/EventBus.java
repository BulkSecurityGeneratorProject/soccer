package gl.linpeng.soccer.aop.event;

import gl.linpeng.soccer.aop.event.handler.EventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Event bus
 *
 * @author lin.peng
 * @since 2017-10-26
 */
@Service
public class EventBus {

    @Resource
    private ApplicationContext appContext;

    private Set<EventHandler> eventHandlers;

    private void init() {
        if (eventHandlers == null) {
            eventHandlers = new HashSet<>();
            String[] beanNames = appContext.getBeanNamesForType(EventHandler.class);
            for (String beanName : beanNames) {
                EventHandler handler = (EventHandler) appContext.getBean(beanName);
                eventHandlers.add(handler);
            }
        }
    }


    /**
     * Creating object event
     *
     * @param clz       what class is
     * @param objBefore object before
     * @param objAfter  object after
     */
    public void onCreate(Class clz, Object objBefore, Object objAfter) {
        init();
        for (EventHandler handler : eventHandlers) {
            if (handler.isSupported(clz)) {
                handler.handleCreate(clz, objBefore, objAfter);
            }
        }
    }

    /**
     * Updating object event
     *
     * @param clz       what class is
     * @param objBefore object before
     * @param objAfter  object after
     */
    public void onUpdate(Class clz, Object objBefore, Object objAfter) {
        init();
        for (EventHandler handler : eventHandlers) {
            if (handler.isSupported(clz)) {
                handler.handleUpdate(clz, objBefore, objAfter);
            }
        }
    }
}
