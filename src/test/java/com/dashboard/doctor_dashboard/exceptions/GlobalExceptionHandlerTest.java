package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.utils.wrapper.ErrorDetails;
import com.dashboard.doctor_dashboard.utils.wrapper.ErrorMessage;
import com.dashboard.doctor_dashboard.utils.wrapper.ValidationsSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }


    @Test
    void handleAPIException() {
        WebRequest request = mock(WebRequest.class);
        ErrorDetails details = new ErrorDetails(new Date(), "test for API exception", request.getDescription(false));
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleAPIException(new APIException("test for API exception"), request);
        Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        Assertions.assertEquals(details.getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());
    }

    @Test
    void handleLoginException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for google login exception", null);

        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleLoginException(new GoogleLoginException("test for google login exception"));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals(details.getMessage(), Objects.requireNonNull(response.getBody()).getErrorData());
    }

    @Test
    void handleGlobalException() {
        WebRequest request = mock(WebRequest.class);
        ErrorDetails details = new ErrorDetails(new Date(), "test for global exception", request.getDescription(false));
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleGlobalException(new Exception("test for global exception"), request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(details.getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());

    }

    @Test
    void processException() {
        WebRequest request = mock(WebRequest.class);
        List<String> message = new ArrayList<>(Arrays.asList("errorMessage1", "errorMessage2", "errorMessage3"));

        ValidationsSchema schema = new ValidationsSchema(new Date(), message, request.getDescription(false));
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.processException(new ValidationsException(message), request);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Assertions.assertEquals(schema.getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());
    }

    @Test
    void handleResourceNotFoundException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for resource not found exception", null);

        ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("test for resource not found exception"));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(details.getMessage(), Objects.requireNonNull(response.getBody()).getErrorData());
    }

//    @Test
//    void methodArgumentNotValid() {
//        WebRequest request = mock(WebRequest.class);
////        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
//        List<String> message = new ArrayList<>(Arrays.asList("errorMessage1", "errorMessage2", "errorMessage3"));
//        ErrorDetails details = new ErrorDetails(new Date(), "test for MethodArgumentNotValid exception", null);
//
//         ResponseEntity<ErrorMessage> response = globalExceptionHandler.methodArgumentNotValidException(new MethodArgumentNotValidException(message,));
//        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,response.getStatusCode());
//    }

    @Test
    void invalidDateException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for invalid date exception", null);
        ResponseEntity<ErrorMessage> response = globalExceptionHandler.invalidDateException(new InvalidDate("test for invalid date exception"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals(details.getMessage().getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());

    }

    @Test
    void mailErrorException() {
        ErrorDetails details = new ErrorDetails(new Date(), "test for mail error",null);

        ResponseEntity<ErrorMessage> response = globalExceptionHandler.mailErrorException(new MailErrorException(details.getMessage()));
        Assertions.assertEquals(HttpStatus.FAILED_DEPENDENCY,response.getStatusCode());
        Assertions.assertEquals(details.getMessage().getClass(), Objects.requireNonNull(response.getBody()).getErrorData().getClass());

    }


}