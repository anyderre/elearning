package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorbSoft.CabAcademie.Entities.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CategoryViewModel {
    private Long id;
    private String name;
    private String description;
}
