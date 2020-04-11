package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.SubCategory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubCategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubCategoryViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface SubCategoryMapper {
    @Mappings({
            @Mapping(target = "allSubCategories", ignore = true),
            @Mapping(target = "allCategories", ignore = true),
    })
    SubCategoryViewModel mapToViewModel(SubCategory subCategory);

    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "deleted", ignore = true),
    })
    SubCategory mapToEntity(SubCategoryViewModel vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    SubCategory mapEntityToEntity(SubCategory subCategory);

    @Mappings({
            @Mapping(target = "subCategoryInfoList", ignore = true),
            @Mapping(target = "categoryName",  source = "category.name"),
    })
    SubCategoryInfo mapEntityToInfo(SubCategory subCategory);

}
