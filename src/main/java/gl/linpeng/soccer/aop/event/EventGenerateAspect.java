package gl.linpeng.soccer.aop.event;

import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.repository.AssociationRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
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

    @Resource
    private AssociationRepository associationRepository;

    private Map<Class, JpaRepository> repositoryMap;

    public EventGenerateAspect() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(Association.class, associationRepository);
    }

    @Pointcut("execution(* gl.linpeng.soccer.repository..*.save*(..))")
    public void savePointcut() {
    }

    @Around("savePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = null;
        Class clz = args[0].getClass();

        Object objBefore = null;
        Object arg = args[0];
        Method method = ReflectionUtils.findMethod(clz, "getId");
        Object id = ReflectionUtils.invokeMethod(method, arg);
        if (id != null) {
            // update
            objBefore = repositoryMap.get(clz).findOne((Long) id);
        } else {
            //create
            objBefore = clz.newInstance();
            BeanUtils.copyProperties(arg, objBefore);
        }

        // begin process
        result = joinPoint.proceed();
        Association objAfter = (Association) result;

        // after process
        if (!objAfter.getId().equals(id)) {
            System.out.println(">>>>>>>>>>>>>>>新增记录");
        }

        return result;
    }
}
