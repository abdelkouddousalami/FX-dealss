package com.bloomberg.fxdeals.exception;

import com.bloomberg.fxdeals.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception mapper for DealNotFoundException.
 * Returns HTTP 404 Not Found with error details.
 */
@Provider
public class DealNotFoundExceptionMapper implements ExceptionMapper<DealNotFoundException> {

    private static final Logger logger = LoggerFactory.getLogger(DealNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(DealNotFoundException exception) {
        logger.error("Deal not found exception: {}", exception.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            Response.Status.NOT_FOUND.getStatusCode(),
            "Deal Not Found",
            exception.getMessage()
        );

        return Response.status(Response.Status.NOT_FOUND)
            .entity(error)
            .build();
    }
}
