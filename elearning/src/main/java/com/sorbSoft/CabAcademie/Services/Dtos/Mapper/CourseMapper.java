package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface CourseMapper {

    CourseViewModel mapToViewModel(Course course);

    @Mappings({
        @Mapping(target = "deleted", ignore = true)
    })
    Course mapToEntity (CourseViewModel vm);
}
