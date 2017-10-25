package gl.linpeng.soccer.config;

import gl.linpeng.soccer.aop.event.AssociationEventAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Soccer-Event aspect configuration class
 *
 * @author linpeng
 * @since 2017-10-25
 */
@Configuration
@EnableAspectJAutoProxy
public class EventAspectConfiguration {

    @Bean
    public AssociationEventAspect saveAspect() {
        return new AssociationEventAspect();
    }

}
