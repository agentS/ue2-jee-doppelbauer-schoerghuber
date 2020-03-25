package eu.nighttrains.booking.logging;

import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import java.lang.invoke.MethodHandles;

@Dependent
@LoggerQualifier(type = LoggerType.CONSOLE)
public class Slf4jLogger implements Logger {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    @Override
    public void info(String message) {
        logger.info(message);
    }
}
