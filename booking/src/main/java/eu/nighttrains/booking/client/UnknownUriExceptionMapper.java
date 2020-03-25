package eu.nighttrains.booking.client;

import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class UnknownUriExceptionMapper
        implements ResponseExceptionMapper<UnknownUriException> {

    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    Logger logger;

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        logger.info("status = " + status);
        return status == 404;
    }

    @Override
    public UnknownUriException toThrowable(Response response) {
        return new UnknownUriException();
    }
}
