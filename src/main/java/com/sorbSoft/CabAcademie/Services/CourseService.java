package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Repository.OverviewRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.CourseFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.CourseMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import com.sorbSoft.CabAcademie.Repository.CourseRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Created by Dany on 16/05/2018.
 */
@Service
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SubSectionService subSectionService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private OverviewRepository overviewRepository;

    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);

    public List<Course> fetchAllCourses(){
        return courseRepository.findAll();
    }

    public Page<Course> fetchAllCoursesByPage(int page, int itemsPerPage){
        Pageable pageable = new PageRequest(page, itemsPerPage);
        return courseRepository.findAll(pageable);
    }

    public Page<Course> fetchAllCoursesByPageAndSerchText(int page, int itemsPerPage, String searchText){
        Pageable pageable = new PageRequest(page, itemsPerPage);
        return courseRepository.findByTitleContainsAllIgnoreCase(searchText, pageable);
    }

    public Course fetchCourse(Long id){
        return courseRepository.findOne(id);
    }

    public Pair<String, Course> updateCourse(CourseViewModel vm){
        Course savedCourse = courseRepository.findCourseByTitleAndIdIsNot(vm.getTitle(), vm.getId());
        if (savedCourse != null) {
            return Pair.of("The course name already exist for another definition", null);
        }
        Course currentCourse = courseRepository.findOne(vm.getId());
        if(currentCourse == null) {
            return Pair.of("The course you are trying to edit does not exist anymore", null);
        }

        Course course = mapper.mapToEntity(vm);
        course.setLastUpdate(new Date());
        Course result = courseRepository.save(course);
        if (result == null) {
            return Pair.of("Couldn't update the course", null);
        } else {
            return Pair.of("Course updated successfully", result);
        }
    }

    public String deleteCourse(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the course";
        }
        Course course = fetchCourse(id);
        if (course == null) {
            return "The course you want to delete doesn't exist";
        }
        course.setDeleted(true);
        return "Course successfully deleted";
    }

    public CourseViewModel getCourseViewModel(Long courseId){
        CourseViewModel vm = CourseFactory.getCourseViewModel();
        if (courseId != null && courseId > 0) {
            Course course = this.fetchCourse(courseId);
            if (course == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(course);
            }
        }
        vm.setSections(sectionService.fetchAllSection()); // TODO: could be filtered
        vm.setSubSections(subSectionService.fetchAllSubSections()); // TODO: could be filtered
        vm.setSubCategories(subCategoryService.fetchAllSubCategories()); // TODO: could be filtered
        vm.setCategories(categoryService.fetchAllCategories());// TODO: could be filtered
        vm.setUsers(userServices.findAllUser()); // Filtered // TODO: could have more filters
        return vm;
    }
    
    public Pair<String, Course> saveCourse(CourseViewModel vm){
        if (vm.getId() > 0L) {
            return updateCourse(vm);
        } else {
            Course savedCourse = courseRepository.findCourseByTitle(vm.getTitle());

            if ( savedCourse != null){
                return Pair.of("The course you are trying to save already exist", null);
            }

            Course course = mapper.mapToEntity(vm);
            course.getOverview().setCourse(course);
            course.setCreationDate(new Date());
            course.setLastUpdate(new Date());
            course.setCreationDate(new Date());
            course.setLastUpdate(new Date());
            Overview overviewEntity = course.getOverview();
            course.setOverview(null);
            Course result = courseRepository.save(course);
            overviewEntity.setCourse(result);
            Overview overview = overviewRepository.save(overviewEntity);
            course.setOverview(overview);
            result = courseRepository.save(course);
            if (result == null){
                return Pair.of("Couldn't save the course", null);
            } else {
                return  Pair.of("Course saved successfully", result);
            }
        }
    }
}