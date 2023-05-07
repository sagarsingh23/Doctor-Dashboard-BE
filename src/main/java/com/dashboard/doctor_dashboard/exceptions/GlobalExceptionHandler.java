package com.dashboard.doctor_dashboard.exceptions;

import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.ErrorDetails;
import com.dashboard.doctor_dashboard.utils.wrapper.ErrorMessage;
import com.dashboard.doctor_dashboard.utils.wrapper.ValidationsSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //     handle specific exceptions

    /**
     * This function of service is call whenever API exception is thrown
     * @param exception Custom API exception
     * @param webRequest
     * @return ResponseEntity<ErrorMessage> with status code 405
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorMessage> handleAPIException(APIException exception,
                                                           WebRequest webRequest) {

        log.error("APIException::"+exception.getMessage());

        var errorMessage = new ErrorMessage();

        var errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);  // status code 405
    }

    /**
     * This function of service is call whenever GoogleLogin exception is thrown
     * @param ex Custom GoogleLoginException
     * @return ResponseEntity<ErrorMessage> with status code 401
     */
    @ExceptionHandler(GoogleLoginException.class)
    public ResponseEntity<ErrorMessage> handleLoginException(GoogleLoginException ex) {
        var errorMessage = new ErrorMessage();

        log.error("GoogleLoginException::"+ex.getMessage());


        errorMessage.setErrorData(ex.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);   //status code 401
    }

    /**
     * This function of service is call whenever  exception is thrown
     * @param exception
     * @param webRequest
     * @return ResponseEntity<ErrorMessage> with status code 404
     */
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

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);   //status code 400
    }


    /**
     * This function of service is call whenever Validation exception is thrown
     * @param ex Custom Validation exception
     * @param request
     * @return ResponseEntity<ErrorMessage> with status code 422
     */
    @ExceptionHandler(ValidationsException.class)
    public ResponseEntity<ErrorMessage> processException(ValidationsException ex, WebRequest request) {
        var errorMessage = new ErrorMessage();
        log.error("ValidationsException::"+ex.getMessages());
        log.info(ex.toString());


        var errorDetails = new ValidationsSchema(new Date(), ex.getMessages(), request.getDescription(false));

        log.debug(errorDetails.toString());
        errorMessage.setErrorData(errorDetails);
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY); //status code 422


    }

    /**
     * This function of service is call whenever ResourceNotFound exception is thrown
     * @param e Custom ResourceNotFound Exception
     * @return ResponseEntity<ErrorMessage> with status code 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e) {

        var errorMessage = new ErrorMessage();
        log.error("ResourceNotFound::"+e.getMessage());


        errorMessage.setErrorData(e.getMessage());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); //status code 404
    }

    /**
     * This function of service is call whenever InvalidDate exception is thrown
     * @param e Custom Invalid Date exception
     * @return ResponseEntity<ErrorMessage> with status code 409
     */
    @ExceptionHandler(InvalidDate.class)
    public ResponseEntity<ErrorMessage> invalidDateException(InvalidDate e) {
        var errorMessage = new ErrorMessage();
        log.error("InvalidDate::"+e.getMessage());


        errorMessage.setErrorData(e.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);    //status code 409
    }

    /**
     * This function of service is call whenever Mail Error exception is thrown
     * @param e Custom Mail Error Exception
     * @return ResponseEntity<ErrorMessage> with status code 424
     */
    @ExceptionHandler(MailErrorException.class)
    public ResponseEntity<ErrorMessage> mailErrorException(MailErrorException e) {
        var errorMessage = new ErrorMessage();
        log.error("Mail Error::"+e.getMessage());


        errorMessage.setErrorData(e.toString());
        errorMessage.setErrorStatus(Constants.FAIL);

        return new ResponseEntity<>(errorMessage, HttpStatus.FAILED_DEPENDENCY);  //status code 424
    }


    /**
     * This function of service is call whenever MethodArgumentNotValid exception is thrown
     * @param exception MethodArgumentNotValid exception
     * @return ResponseEntity<ErrorMessage> with status code 422
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        log.error("MethodArgumentNotValidException::"+exception.getMessage());
        List<String> messages = exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Error:"+messages);
        var errorMessage = new ErrorMessage();
        errorMessage.setErrorData(Objects.requireNonNull(messages));
        errorMessage.setErrorStatus(Constants.FAIL);


        return new ResponseEntity<>(errorMessage,HttpStatus.UNPROCESSABLE_ENTITY); //status code 422
    }
}
