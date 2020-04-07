package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Objective;
import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-07T01:20:06-0400",
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
        courseViewModel.setDescription( course.getDescription() );
        courseViewModel.setImageUrl( course.getImageUrl() );
        courseViewModel.setRatings( course.getRatings() );
        courseViewModel.setPrice( course.getPrice() );
        courseViewModel.setEnrolled( course.getEnrolled() );
        courseViewModel.setAuthor( course.getAuthor() );
        courseViewModel.setPremium( course.isPremium() );
        courseViewModel.setStartDate( course.getStartDate() );
        courseViewModel.setEndDate( course.getEndDate() );
        courseViewModel.setSection( course.getSection() );
        courseViewModel.setCategory( course.getCategory() );
        courseViewModel.setSubCategory( course.getSubCategory() );
        courseViewModel.setUser( course.getUser() );
        courseViewModel.setOverview( course.getOverview() );
        List<Syllabus> list = course.getSyllabus();
        if ( list != null ) {
            courseViewModel.setSyllabus( new ArrayList<Syllabus>( list ) );
        }
        List<Objective> list1 = course.getObjectives();
        if ( list1 != null ) {
            courseViewModel.setObjectives( new ArrayList<Objective>( list1 ) );
        }

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
        course.setDescription( vm.getDescription() );
        course.setImageUrl( vm.getImageUrl() );
        course.setRatings( vm.getRatings() );
        course.setEnrolled( vm.getEnrolled() );
        course.setAuthor( vm.getAuthor() );
        course.setPrice( vm.getPrice() );
        course.setPremium( vm.isPremium() );
        course.setStartDate( vm.getStartDate() );
        course.setEndDate( vm.getEndDate() );
        course.setUser( vm.getUser() );
        List<Syllabus> list = vm.getSyllabus();
        if ( list != null ) {
            course.setSyllabus( new ArrayList<Syllabus>( list ) );
        }
        course.setCategory( vm.getCategory() );
        course.setSubCategory( vm.getSubCategory() );
        course.setSection( vm.getSection() );
        course.setOverview( vm.getOverview() );
        List<Objective> list1 = vm.getObjectives();
        if ( list1 != null ) {
            course.setObjectives( new ArrayList<Objective>( list1 ) );
        }

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
        course1.setDescription( course.getDescription() );
        course1.setImageUrl( course.getImageUrl() );
        course1.setRatings( course.getRatings() );
        course1.setEnrolled( course.getEnrolled() );
        course1.setAuthor( course.getAuthor() );
        course1.setPrice( course.getPrice() );
        course1.setPremium( course.isPremium() );
        course1.setDeleted( course.isDeleted() );
        course1.setStartDate( course.getStartDate() );
        course1.setEndDate( course.getEndDate() );
        course1.setLastUpdate( course.getLastUpdate() );
        course1.setCreationDate( course.getCreationDate() );
        course1.setUser( course.getUser() );
        List<Syllabus> list = course.getSyllabus();
        if ( list != null ) {
            course1.setSyllabus( new ArrayList<Syllabus>( list ) );
        }
        course1.setCategory( course.getCategory() );
        course1.setSubCategory( course.getSubCategory() );
        course1.setSection( course.getSection() );
        course1.setOverview( course.getOverview() );
        List<Objective> list1 = course.getObjectives();
        if ( list1 != null ) {
            course1.setObjectives( new ArrayList<Objective>( list1 ) );
        }

        return course1;
    }
}
