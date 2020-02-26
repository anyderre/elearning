package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface CategoryMapper {
    CategoryViewModel mapToViewModel(Category category);

    Category mapToEntity(CategoryViewModel vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Category mapEntityToEntity(Category category);
}
