package gl.linpeng.soccer.aop.event.handler;

import gl.linpeng.soccer.domain.Association;
import org.springframework.stereotype.Service;

/**
 * Association event handler
 *
 * @author lin.peng
 * @since 2017-10-26
 */
@Service
public class AssociationEventHandler implements EventHandler {

    @Override
    public boolean isSupported(Class clz) {
        return clz.equals(Association.class);
    }

    @Override
    public void handleCreate(Class clz, Object before, Object after) {
        System.out.println("====新增协会");
    }

    @Override
    public void handleUpdate(Class clz, Object before, Object after) {
        System.out.println("====修改协会");
    }
}
