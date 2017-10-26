package gl.linpeng.soccer.aop.event;

import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.repository.AssociationRepository;
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

    private Map<String, JpaRepository> repositoryMap;

    @Resource
    private AssociationRepository associationRepository;


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
        Method methodGetId = ReflectionUtils.findMethod(clz, METHOD_GET_ID);
        Object id = ReflectionUtils.invokeMethod(methodGetId, arg);
        if (id != null) {
            // update
            if (null == repositoryMap) {
                repositoryMap = new HashMap<>();
                String[] beanNames = appContext.getBeanNamesForType(JpaRepository.class);
                // Arrays.sort(beanNames);
                for (String beanName : beanNames) {
                    Object bean = appContext.getBean(beanName);
                    if (beanName.endsWith(REPOSITORY_SUFFIX) && null != bean) {
                        String clzName = beanName.replace(REPOSITORY_SUFFIX, "").toUpperCase();
                        repositoryMap.put(clzName, (JpaRepository) bean);
                    }
                }
            }
            Association association = associationRepository.findOne((Long) id);
            objBefore = repositoryMap.get(clz.getSimpleName().toUpperCase()).findOne((Long) id);
        } else {
            //create
            objBefore = clz.newInstance();
            BeanUtils.copyProperties(arg, objBefore);
        }
        System.out.println("====before " + objBefore);
        // begin process
        result = joinPoint.proceed();

        // after process
        Object afterId = ReflectionUtils.invokeMethod(methodGetId, result);
        if (!afterId.equals(id)) {
            System.out.println(">>>>>>>>>>>>>>>新增记录");
        } else {
            System.out.println(">>>>>>>>>>>>>>>修改记录，" + objBefore + "," + result);
        }

        return result;
    }
}
