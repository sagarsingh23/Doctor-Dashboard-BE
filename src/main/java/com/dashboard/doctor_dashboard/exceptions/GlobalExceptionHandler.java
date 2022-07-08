package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.Utils.Constants;
import com.dashboard.doctor_dashboard.Utils.wrapper.ErrorMessage;
import com.dashboard.doctor_dashboard.Utils.wrapper.ErrorDetails;
import com.dashboard.doctor_dashboard.Utils.wrapper.ValidationsSchema;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import java.util.Collections;
import java.util.Date;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //     handle specific exceptions

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorMessage> handleAPIException(APIException exception,
                                                           WebRequest webRequest) {

        log.error("APIException::"+exception.getMessage());

        var errorMessage = new ErrorMessage();

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(GoogleLoginException.class)
    public ResponseEntity<ErrorMessage> handleLoginException(GoogleLoginException ex) {
        var errorMessage = new ErrorMessage();

        log.error("GoogleLoginException::"+ex.getMessage());


        errorMessage.setErrorData(ex.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    //     global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        var errorMessage = new ErrorMessage();
        log.error("Exception::"+exception.getMessage());

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ValidationsException.class)
    public ResponseEntity<ErrorMessage> processException(ValidationsException ex, WebRequest request) {
        var errorMessage = new ErrorMessage();
        log.error("ValidationsException::"+ex.getMessage());


        var errorDetails = new ValidationsSchema(new Date(), Collections.singletonList(ex.getMessage()), request.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);


    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e) {

        var errorMessage = new ErrorMessage();
        log.error("ResourceNotFound::"+e.getMessage());


        errorMessage.setErrorData(e.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDate.class)
    public ResponseEntity<ErrorMessage> invalidDateException(InvalidDate e) {
        var errorMessage = new ErrorMessage();
        log.error("InvalidDate::"+e.getMessage());


        errorMessage.setErrorData(e.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MailErrorException.class)
    public ResponseEntity<ErrorMessage> mailErrorException(MailErrorException e) {
        var errorMessage = new ErrorMessage();
        log.error("Mail Error::"+e.getMessage());


        errorMessage.setErrorData(e.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.FAILED_DEPENDENCY);
    }
}
