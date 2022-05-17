package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class CommonExceptionHandler {

    ErrorMessage errorMessage = new ErrorMessage();

    //     handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {
        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorStatus(Constants.FAIL);
        errorMessage.setErrorData(errorDetails);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorMessage> handleAPIException(APIException exception,
                                                           WebRequest webRequest) {
        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoogleLoginException.class)
    public ResponseEntity<ErrorMessage> handleLoginException(GoogleLoginException ex) {

        errorMessage.setErrorData(ex);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    //     global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ValidationsException.class)
    public ResponseEntity<ErrorMessage> processException(final ValidationsException ex, WebRequest request) {

        var errorDetails = new ValidationsSchema(new Date(), ex.getMessages(), request.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);


    }

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleMyCustomException(MyCustomException e) {

        errorMessage.setErrorData(e.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReportNotFound.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleReportNotFoundException(ReportNotFound e) {

        errorMessage.setErrorData(e.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> validation(final ConstraintViolationException ex, WebRequest webRequest) {

        List<String> newList = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.toList());

        var errorDetails = new ErrorDetails(new Date(), newList.toString(), webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

    }
}
