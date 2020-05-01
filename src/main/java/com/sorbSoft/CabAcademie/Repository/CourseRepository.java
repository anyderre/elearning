package com.sorbSoft.CabAcademie.Repository;


import com.sorbSoft.CabAcademie.Entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Pageable pagin);
    Course findCourseByTitle(String name);
    Course findCourseByTitleAndIdIsNot(String title, Long id);
    Page<Course> findByTitleContainsAllIgnoreCase(String searchText, Pageable pagin);

    List<Course> findAllBySubSectionId(Long subSectionId);
    List<Course> findAllBySubCategoryId(Long subCategoryId);

    /*
        find last created
     */
    @Query("select c from Course c where c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreated(Pageable pageable);

    @Query("select c from Course c where c.category=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedByCategory(Category category, Pageable pageable);

    @Query("select c from Course c where c.subCategory=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubCategory(SubCategory subCategory, Pageable pageable);

    @Query("select c from Course c where c.section=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySection(Section section, Pageable pageable);

    @Query("select c from Course c where c.subSection=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubSection(SubSection subSection, Pageable pageable);


    /*
       find best rated
    */
    @Query("select c from Course c where c.deleted=false order by c.ratings desc")
    List<Course> findBestRated(Pageable pageable);

    @Query("select c from Course c where c.category=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedByCategory(Category category, Pageable pageable);

    @Query("select c from Course c where c.subCategory=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySubCategory(SubCategory subCategory, Pageable pageable);

    @Query("select c from Course c where c.section=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySection(Section section, Pageable pageable);

    @Query("select c from Course c where c.subSection=?1 and c.deleted=false order by c.ratings desc")
    List<Course> findBestRatedBySubSection(SubSection subSection, Pageable pageable);


    /*
         find featured
      */
    @Query("select c from Course c where c.deleted=false order by c.enrolled desc")
    List<Course> findFeatured(Pageable pageable);

    @Query("select c from Course c where c.category=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedByCategory(Category category, Pageable pageable);

    @Query("select c from Course c where c.subCategory=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySubCategory(SubCategory subCategory, Pageable pageable);

    @Query("select c from Course c where c.section=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySection(Section section, Pageable pageable);

    @Query("select c from Course c where c.subSection=?1 and c.deleted=false order by c.enrolled desc")
    List<Course> findFeaturedBySubSection(SubSection subSection, Pageable pageable);

}
