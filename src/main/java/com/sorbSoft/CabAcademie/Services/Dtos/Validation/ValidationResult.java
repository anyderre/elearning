package com.sorbSoft.CabAcademie.Services.Dtos.Validation;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ValidationResult {

    public String message;
    public List<String> fields;

    public boolean isValid()
    {
        return message.length() == 0;
    }

    public ValidationResult()
    {
        this("", "");
    }

    public ValidationResult(String message)
    {
        this(message, "");
    }

    public ValidationResult(String message, String field)
    {
        this (message, Collections.singletonList(field));
    }

    public ValidationResult(String message, List<String> fields)
    {
        this.message = message;
        this.fields = fields;
    }
}
