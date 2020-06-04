package com.sorbSoft.CabAcademie.Repository;


import com.sorbSoft.CabAcademie.Entities.*;
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


    List<Course> findAllBySubSectionIdAndSchoolsIsNull(Long subSectionId);

    List<Course> findAllBySubSectionId(Long subSectionId);

    List<Course> findAllBySubSectionIdAndSchoolsIn(Long subSectionId, User school);


    List<Course> findAllBySubCategoryIdAndSchoolsIsNull(Long subCategoryId);

    List<Course> findAllBySubCategoryIdAndSchoolsIn(Long subCategoryId, User school);

    /*
        find last created
     */
    @Query("select c from Course c where c.schools IS EMPTY and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedPublicCourses(Pageable pageable);

    /*@Query("select c from Course c where :school IN c.schools   and c.deleted=false order by c.creationDate desc")
    //TODO: finish
    List<Course> findLastCreatedPrivateCourses(Pageable pageable, @Param("school") User school);*/


    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedByCategoryPublicCourses(Category category, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubCategoryPublicCourses(SubCategory subCategory, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySectionPublicCourses(Section section, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubSectionPublicCourses(SubSection subSection, Pageable pageable);


    /*
       find best rated
    */
    @Query("select c from Course c where c.schools IS EMPTY and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedPublicCourses(Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedByCategoryPublicCourses(Category category, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySubCategoryPublicCourses(SubCategory subCategory, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySectionPublicCourses(Section section, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySubSectionPublicCourses(SubSection subSection, Pageable pageable);


    /*
         find featured
      */
    @Query("select c from Course c where c.schools IS EMPTY and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedPublicCourses(Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedByCategoryPublicCourses(Category category, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySubCategoryPublicCourses(SubCategory subCategory, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySectionPublicCourses(Section section, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySubSectionPublicCourses(SubSection subSection, Pageable pageable);


    long countCoursesBySchoolsIn(User school);
}
