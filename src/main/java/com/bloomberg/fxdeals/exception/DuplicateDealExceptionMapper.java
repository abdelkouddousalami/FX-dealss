package com.bloomberg.fxdeals.exception;

import com.bloomberg.fxdeals.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception mapper for DuplicateDealException.
 * Returns HTTP 409 Conflict with error details.
 */
@Provider
public class DuplicateDealExceptionMapper implements ExceptionMapper<DuplicateDealException> {

    private static final Logger logger = LoggerFactory.getLogger(DuplicateDealExceptionMapper.class);

    @Override
    public Response toResponse(DuplicateDealException exception) {
        logger.error("Duplicate deal exception: {}", exception.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            Response.Status.CONFLICT.getStatusCode(),
            "Duplicate Deal",
            exception.getMessage()
        );

        return Response.status(Response.Status.CONFLICT)
            .entity(error)
            .build();
    }
}
