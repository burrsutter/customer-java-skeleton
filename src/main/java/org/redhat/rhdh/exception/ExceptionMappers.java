package org.redhat.rhdh.exception;

import java.util.stream.Collectors;

import org.redhat.rhdh.dto.ErrorResponse;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionMappers {

    @Provider
    public static class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
        @Override
        public Response toResponse(NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found", e.getMessage(), 404))
                    .build();
        }
    }

    @Provider
    public static class DuplicateEntityExceptionMapper implements ExceptionMapper<DuplicateEntityException> {
        @Override
        public Response toResponse(DuplicateEntityException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Conflict", e.getMessage(), 409))
                    .build();
        }
    }

    @Provider
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException e) {
            String details = e.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Validation Error", details, 400))
                    .build();
        }
    }
}
