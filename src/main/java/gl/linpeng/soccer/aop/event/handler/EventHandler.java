package gl.linpeng.soccer.aop.event.handler;

/**
 * Soccer event handler interface
 *
 * @author lin.peng
 * @since 2017-10-26
 */
public interface EventHandler {

    /**
     * Determine is this class supported
     *
     * @param clz determine class
     * @return return true if this class is supported
     */
    boolean isSupported(Class clz);

    /**
     * Do something after create a object
     *
     * @param clz    what class is
     * @param before object before
     * @param after  object after
     */
    void handleCreate(Class clz, Object before, Object after);

    /**
     * Do something after update a object
     *
     * @param clz    what class is
     * @param before object before
     * @param after  object after
     */
    void handleUpdate(Class clz, Object before, Object after);

}
