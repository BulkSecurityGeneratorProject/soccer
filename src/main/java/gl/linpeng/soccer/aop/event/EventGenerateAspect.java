package gl.linpeng.soccer.aop.event;

import gl.linpeng.soccer.domain.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * soccer event generate aspect
 *
 * @author linpeng
 * @since 2017-10-25
 */
@Aspect
public class EventGenerateAspect {

    private static final String REPOSITORY_SUFFIX = "Repository";
    private static final String METHOD_GET_ID = "getId";

    @Resource
    private ApplicationContext appContext;
    @Resource
    private EventBus eventBus;

    private Map<String, JpaRepository> repositoryMap;

    @Pointcut("execution(* gl.linpeng.soccer.repository..*.save*(..))")
    public void savePointcut() {
    }

    @Around("savePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = null;
        Object arg = args[0];
        Class clz = arg.getClass();

        Object objBefore = null;
        Object id = null;
        Method methodGetId = ReflectionUtils.findMethod(clz, METHOD_GET_ID);
        if (methodGetId != null && !(arg instanceof Event)) {
            id = ReflectionUtils.invokeMethod(methodGetId, arg);
            if (id != null) {
                // update
                initRepositoryMap();
                JpaRepository jpaRepository = repositoryMap.get(clz.getSimpleName().toUpperCase());
                if (jpaRepository != null) {
                    objBefore = jpaRepository.findOne((Long) id);
                }
            } else {
                //create
                objBefore = clz.newInstance();
                BeanUtils.copyProperties(arg, objBefore);
            }
        }

        // begin process
        result = joinPoint.proceed();

        // after process
        Object afterId = null;
        if (methodGetId != null && !(arg instanceof Event)) {
            afterId = ReflectionUtils.invokeMethod(methodGetId, result);
            if (!afterId.equals(id)) {
                eventBus.onCreate(clz, objBefore, result);
            } else {
                eventBus.onUpdate(clz, objBefore, result);
            }
        }

        return result;
    }

    private void initRepositoryMap() {
        if (null == repositoryMap) {
            repositoryMap = new HashMap<>();
            String[] beanNames = appContext.getBeanNamesForType(JpaRepository.class);
            for (String beanName : beanNames) {
                Object bean = appContext.getBean(beanName);
                if (beanName.endsWith(REPOSITORY_SUFFIX) && null != bean) {
                    String clzName = beanName.replace(REPOSITORY_SUFFIX, "").toUpperCase();
                    repositoryMap.put(clzName, (JpaRepository) bean);
                }
            }
        }
    }
}
