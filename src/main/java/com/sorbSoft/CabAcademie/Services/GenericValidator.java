package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.SchoolNotFoundExcepion;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GenericValidator {

    public Result validateNull(Object value, String valueName) {
        Result result = new Result();
        if (value == null) {
            result.add(valueName + " be null or equal 0");
            return result;
        }
        return result;
    }

    public void validateNull(Long value, String valueName) throws EmptyValueException {
        Result result = new Result();
        if (value == null || value <= 0) {
            throw new EmptyValueException(valueName + " be null or equal 0");
        }
    }

    public Result validateNull(Integer value, String valueName) {
        Result result = new Result();
        if (value == null || value <= 0) {
            result.add(valueName + " can't be null or equal 0");
            return result;
        }
        return result;
    }

    public Result validateNull(Date value, String valueName) {
        Result result = new Result();
        if (value == null) {
            result.add(valueName + " can't be zero or less!");
            return result;
        }
        return result;
    }

    public Result validateNull(List value, String valueName) {
        Result result = new Result();
        if (value == null || value.isEmpty()) {
            result.add(valueName + " can not be empty or null");
            return result;
        }
        return result;
    }

    public Result validateDateBeforeNow(Date date, String valueName) {
        Result result = new Result();
        DateTime date1 = new DateTime(date);
        DateTime now = DateTime.now();
        if (date1.isBefore(now)) {
            result.add(valueName + " can't be before 'Now'");
            return result;
        }
        return result;
    }

    public Result validateDateFromToOrder(Date from, Date to) {
        Result result = new Result();
        DateTime curFrom = new DateTime(from);
        DateTime curTo = new DateTime(to);

        if (curFrom.isAfter(curTo)) {
            result.add("Date 'From' can't be after Date 'To'");
            return result;
        }
        if (curFrom.isEqual(curTo)) {
            result.add("Date 'From' can't be Equal to Date 'To'");
            return result;
        }
        return result;
    }

    public void validateNull(String value, String valueName) throws EmptyValueException {
        if (value == null || value.isEmpty()) {
            throw new EmptyValueException(valueName + " can't be null or empty");
        }
    }

    public void validateNull(User user, String valueName, String byValue) throws UserNotFoundExcepion {
        if(user == null) {
            throw new UserNotFoundExcepion("User with "+valueName+": "+byValue+" has not been found in db");
        }
    }

    public void validateNull(User user, String valueName, Long byValue) throws UserNotFoundExcepion {
        if(user == null) {
            throw new UserNotFoundExcepion("User with "+valueName+": "+byValue+" has not been found in db");
        }
    }

    public void validateSchoolsNull(List<User> schools, String valueName, String byValue) throws UserNotFoundExcepion, SchoolNotFoundExcepion {
        if(schools == null || schools.isEmpty()) {
            throw new SchoolNotFoundExcepion("User with "+valueName+": "+byValue+" doesn't belong to any school or organization");
        }
    }

    public void validateSchoolsNull(List<User> schools, String valueName, Long byValue) throws UserNotFoundExcepion, SchoolNotFoundExcepion {
        if(schools == null || schools.isEmpty()) {
            throw new SchoolNotFoundExcepion("User with "+valueName+": "+byValue+" doesn't belong to any school or organization");
        }
    }


}
