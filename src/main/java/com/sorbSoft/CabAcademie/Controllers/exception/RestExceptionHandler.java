package com.sorbSoft.CabAcademie.Controllers.exception;


import com.sorbSoft.CabAcademie.exception.*;

import com.sorbSoft.CabAcademie.model.error.ApiError;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        log.warn(error);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(error);
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(SchoolNotFoundExcepion.class)
    protected ResponseEntity<Object> handleSchoolNotFoundExcepion(SchoolNotFoundExcepion ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(error);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CourseNotFoundExcepion.class)
    protected ResponseEntity<Object> handleCourseNotFoundExcepion(CourseNotFoundExcepion ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(error);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UserNotFoundExcepion.class)
    protected ResponseEntity<Object> handleUserNotFoundExcepion(UserNotFoundExcepion ex) {
        String error = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(error);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);
        //apiError.setDebugMessage(ex.getStackTrace().toString());
        return buildResponseEntity(apiError);
    }



    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    protected ResponseEntity<Object> handleIncorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        String error = ex.getMessage();
        String debugError = "Actual size:"+ex.getActualSize()+", Expected size:"+ex.getExpectedSize();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);
        apiError.setDebugMessage(debugError);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(WorkspaceNameIsAlreadyTaken.class)
    protected ResponseEntity<Object> handleWorkspaceNameIsAlreadyTaken(WorkspaceNameIsAlreadyTaken ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SubCategoryNotFoundException.class)
    protected ResponseEntity<Object> handleSubCategoryNotFoundException(SubCategoryNotFoundException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SectionNotFoundException.class)
    protected ResponseEntity<Object> handleSectionNotFoundException(SectionNotFoundException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SubSectionNotFoundException.class)
    protected ResponseEntity<Object> handleSubSectionNotFoundException(SubSectionNotFoundException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EmptyValueException.class)
    protected ResponseEntity<Object> handleEmptyValueException(EmptyValueException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    protected ResponseEntity<Object> handlePasswordsDoNotMatchException(PasswordsDoNotMatchException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CsvParseException.class)
    protected ResponseEntity<Object> handleCsvParseException(CsvParseException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SaveUserException.class)
    protected ResponseEntity<Object> handleSaveUserException(SaveUserException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RoleNotAllowedException.class)
    protected ResponseEntity<Object> handleRoleNotAllowedException(RoleNotAllowedException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RoleFormatException.class)
    protected ResponseEntity<Object> handleRoleFormatException(RoleFormatException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CourseAccessDeniedException.class)
    protected ResponseEntity<Object> handleCourseAccessDeniedException(CourseAccessDeniedException ex) {
        String error = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(error);

        return buildResponseEntity(apiError);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        log.warn(apiError.getMessage());

        if(apiError.getDebugMessage()!=null) {
            log.warn(apiError.getDebugMessage());
        }

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}