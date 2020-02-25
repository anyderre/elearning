package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-25T00:29:26-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseViewModel mapToViewModel(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseViewModel courseViewModel = new CourseViewModel();

        courseViewModel.setId( course.getId() );
        courseViewModel.setTitle( course.getTitle() );
        courseViewModel.setUser( course.getUser() );
        courseViewModel.setPrice( course.getPrice() );
        courseViewModel.setCategory( course.getCategory() );
        courseViewModel.setSection( course.getSection() );
        courseViewModel.setPremium( course.isPremium() );
        courseViewModel.setStartDate( course.getStartDate() );
        courseViewModel.setEndDate( course.getEndDate() );
        List<Syllabus> list = course.getSyllabus();
        if ( list != null ) {
            courseViewModel.setSyllabus( new ArrayList<Syllabus>( list ) );
        }
        courseViewModel.setDeleted( course.isDeleted() );

        return courseViewModel;
    }

    @Override
    public Course mapToEntity(CourseViewModel vm) {
        if ( vm == null ) {
            return null;
        }

        Course course = new Course();

        course.setId( vm.getId() );
        course.setTitle( vm.getTitle() );
        course.setUser( vm.getUser() );
        course.setPrice( vm.getPrice() );
        List<Syllabus> list = vm.getSyllabus();
        if ( list != null ) {
            course.setSyllabus( new ArrayList<Syllabus>( list ) );
        }
        course.setCategory( vm.getCategory() );
        course.setSection( vm.getSection() );
        course.setPremium( vm.isPremium() );
        course.setStartDate( vm.getStartDate() );
        course.setEndDate( vm.getEndDate() );

        return course;
    }

    @Override
    public Course mapEntityToEntity(Course course) {
        if ( course == null ) {
            return null;
        }

        Course course1 = new Course();

        course1.setId( course.getId() );
        course1.setTitle( course.getTitle() );
        course1.setUser( course.getUser() );
        course1.setPrice( course.getPrice() );
        List<Syllabus> list = course.getSyllabus();
        if ( list != null ) {
            course1.setSyllabus( new ArrayList<Syllabus>( list ) );
        }
        course1.setCategory( course.getCategory() );
        course1.setSection( course.getSection() );
        course1.setPremium( course.isPremium() );
        course1.setStartDate( course.getStartDate() );
        course1.setEndDate( course.getEndDate() );
        course1.setDeleted( course.isDeleted() );

        return course1;
    }
}
