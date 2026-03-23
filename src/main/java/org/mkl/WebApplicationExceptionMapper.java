package org.mkl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class WebApplicationExceptionMapper {

    @ServerExceptionMapper
    public Response handle(WebApplicationException exception) {

        int status = 500;
        if (exception.getResponse() != null) {
            status = exception.getResponse().getStatus();
        }

        String message = "request failed";
        if (exception.getMessage() != null && !exception.getMessage().isBlank()) {
            message = exception.getMessage();
        }

        ErrorResponse body = new ErrorResponse();
        body.message = message;
        body.status = status;

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }

    public static class ErrorResponse {
        public String message;
        public int status;
    }
}