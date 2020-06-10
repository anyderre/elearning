package com.sorbSoft.CabAcademie.Repository;


import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAll(Pageable pagin);

    Course findCourseByTitle(String name);

    Course findCourseByTitleAndIdIsNot(String title, Long id);

    Page<Course> findByTitleContainsAllIgnoreCase(String searchText, Pageable pagin);


    //subSection
    List<Course> findAllBySubSectionIdAndSchoolsIsNullAndStatus(Long subSectionId, CourseStatus status);

    List<Course> findAllBySubSectionId(Long subSectionId);

    List<Course> findAllBySubSectionIdAndSchoolsInAndStatus(Long subSectionId, User school, CourseStatus status);


    //subCategory
    List<Course> findAllBySubCategoryIdAndSchoolsIsNullAndStatus(Long subCategoryId, CourseStatus status);

    List<Course> findAllBySubCategoryIdAndSchoolsInAndStatus(Long subCategoryId, User school, CourseStatus status);

    /*
        find last created
     */
    @Query("select c from Course c where c.schools IS EMPTY and c.status=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedPublicCoursesWithStatus(CourseStatus status, Pageable pageable);

   //private
    List<Course> findLastCreatedPrivateCoursesByStatusAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(CourseStatus status, User school, Pageable pageable);


    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedByCategoryPublicCoursesWithStatus(Category category, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubCategoryPublicCoursesWithStatus(SubCategory subCategory, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySectionPublicCoursesWithStatus(Section section, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubSectionPublicCoursesWithStatus(SubSection subSection, CourseStatus status, Pageable pageable);


    /*
       find best rated
    */
    @Query("select c from Course c where c.schools IS EMPTY and c.status=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedPublicCoursesWithStatus(CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.status=?2 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedByCategoryPublicCoursesWithStatus(Category category, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.status=?2 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySubCategoryPublicCoursesWithStatus(SubCategory subCategory, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.status=?2 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySectionPublicCoursesWithStatus(Section section, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.status=?2 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySubSectionPublicCoursesWithStatus(SubSection subSection, CourseStatus status, Pageable pageable);


    /*
         find featured
      */
    @Query("select c from Course c where c.schools IS EMPTY and c.status=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedPublicCoursesWithStatus(CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.status=?2 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedByCategoryPublicCoursesWithStatus(Category category, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.status=?2 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySubCategoryPublicCoursesWithStatus(SubCategory subCategory, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.status=?2 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySectionPublicCoursesWithStatus(Section section, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.status=?2 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySubSectionPublicCoursesWithStatus(SubSection subSection, CourseStatus status, Pageable pageable);


    long countCoursesBySchoolsIn(User school);
    long countCoursesBySchoolsInAndStatus(User school, CourseStatus status);
}
