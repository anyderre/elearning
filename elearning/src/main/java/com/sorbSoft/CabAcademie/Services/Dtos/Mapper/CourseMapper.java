package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface CourseMapper {

        @Mappings({
            @Mapping(target="categories", ignore = true),
            @Mapping(target="users", ignore = true),
            @Mapping(target="subCategories", ignore = true),
            @Mapping(target="sections", ignore = true)})
    CourseViewModel mapToViewModel(Course course);

    @Mappings({
        @Mapping(target = "deleted", ignore = true),
        @Mapping(target = "lastUpdate", ignore = true),
        @Mapping(target = "creationDate", ignore = true)
    })
    Course mapToEntity (CourseViewModel vm);

//    @Mappings({
//            @Mapping(target="id", source="dto.employeeId"),
//            @Mapping(target="name", source="dto.employeeName"),
//            @Mapping(target="startDt", source="dto.employeeStartDt",
//                    dateFormat="dd-MM-yyyy HH:mm:ss")})
    Course mapEntityToEntity (Course course);
}
