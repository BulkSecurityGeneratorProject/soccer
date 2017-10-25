package gl.linpeng.soccer.aop.event;

import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.repository.AssociationRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * Association event aspect
 *
 * @author linpeng
 * @since 2017-10-25
 */
@Aspect
public class AssociationEventAspect {

    @Resource
    private AssociationRepository associationRepository;

    @Pointcut("execution(* gl.linpeng.soccer.repository..*.save*(..))")
    public void savePointcut() {
    }

    @Around("savePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Association objBefore = new Association();
        if (args != null && args.length > 0 && args[0].getClass() == Association.class) {
            Association association = (Association) args[0];
            if (association.getId() != null) {// update
                objBefore = associationRepository.findOne(association.getId());
            } else { //create
                // objBefore = association;
                BeanUtils.copyProperties(association, objBefore);
            }
        }
        Object result = joinPoint.proceed();
        Association objAfter = (Association) result;

        if (objBefore.getStatus() != objAfter.getStatus()) {
            System.out.println(">>>>>>>>>>>>>>>修改状态");
        }
        if (!objAfter.getId().equals(objBefore.getId())) {
            System.out.println(">>>>>>>>>>>>>>>新增记录");
        }

        return result;
    }
}
