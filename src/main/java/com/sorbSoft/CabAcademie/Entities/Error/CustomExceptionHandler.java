package com.sorbSoft.CabAcademie.Entities.Error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, String message) {
        ErrorMessage error = new ErrorMessage(BAD_REQUEST, message, ex);
        return buildResponseEntity(error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NullPointerException.class})
    public final ResponseEntity<Object> handleNullException(NullPointerException ex, String message) {
        ErrorMessage error = new ErrorMessage(BAD_REQUEST, message, ex);
        return buildResponseEntity(error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,WebRequest request)  {
        ErrorMessage errorMessage = new ErrorMessage(BAD_REQUEST, ex);
        errorMessage.setSubErrors(new ArrayList<>());
        Object el = ex.getBindingResult().getAllErrors();
        if (ex.getBindingResult() != null && ex.getBindingResult().getAllErrors() != null)
            for (ObjectError error: ex.getBindingResult().getAllErrors()){
                ValidationError validationError = new ValidationError(error.getObjectName(),error.getDefaultMessage());
                errorMessage.getSubErrors().add(validationError);
            }
//            ex.getBindingResult().getAllErrors().forEach((error) -> {
//            });
        return buildResponseEntity(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({SQLException.class})
    public final ResponseEntity<Object> handleSqlException(SQLException ex, String message) {
        ErrorMessage error = new ErrorMessage(BAD_REQUEST, message, ex);
        return buildResponseEntity(error);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class})
    public final ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, String message) {
        ErrorMessage apiError = new ErrorMessage(NOT_FOUND, message ,ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorMessage errorMessage) {
        return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
    }

}
