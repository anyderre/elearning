package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
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

    public Result validateNull(Long value, String valueName) {
        Result result = new Result();
        if (value == null || value <= 0) {
            result.add(valueName + " be null or equal 0");
            return result;
        }
        return result;
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


}
