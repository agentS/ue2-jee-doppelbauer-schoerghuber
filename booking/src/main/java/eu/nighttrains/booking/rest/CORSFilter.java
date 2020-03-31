package eu.nighttrains.booking.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Objects;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        String requestHeader = containerRequestContext.getHeaderString("Access-Control-Request-Headers");
        if ((requestHeader != null) && (!Objects.equals(requestHeader, ""))) {
            containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Headers", requestHeader);
        }
    }
}
