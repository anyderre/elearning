package com.sorbSoft.CabAcademie.Services.Dtos.Info;

import lombok.Data;

import java.util.List;

@Data
public class SubCategoryInfo {
    private Long id;
    private String name;
    private String description;
    private String categoryName;
    private List<SubCategoryInfo> subCategoryInfoList;
}
