package eu.nighttrains.booking.logging;

import javax.inject.Qualifier;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Qualifier
public @interface LoggerQualifier {
    LoggerType type() default LoggerType.CONSOLE;
}
