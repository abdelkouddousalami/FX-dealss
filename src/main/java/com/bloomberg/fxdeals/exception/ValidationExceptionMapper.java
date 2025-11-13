package com.bloomberg.fxdeals.exception;

import com.bloomberg.fxdeals.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception mapper for ValidationException.
 * Returns HTTP 400 Bad Request with validation error details.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionMapper.class);

    @Override
    public Response toResponse(ValidationException exception) {
        logger.error("Validation exception: {}", exception.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            Response.Status.BAD_REQUEST.getStatusCode(),
            "Validation Error",
            exception.getMessage()
        );

        return Response.status(Response.Status.BAD_REQUEST)
            .entity(error)
            .build();
    }
}
