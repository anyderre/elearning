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


    Page<Course> findAllByDeletedIsFalse(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByRatingsDesc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByRatingsAsc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByTitleDesc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByTitleAsc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByAuthorDesc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByAuthorAsc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByPriceDesc(Pageable pageable);
    Page<Course> findAllByDeletedIsFalseOrderByPriceAsc(Pageable pageable);

    Page<Course> findAllByTitleAndDeletedIsFalse(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByRatingsDesc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByRatingsAsc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByTitleDesc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByTitleAsc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByAuthorDesc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByAuthorAsc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByPriceDesc(String title, Pageable pageable);
    Page<Course> findAllByTitleAndDeletedIsFalseOrderByPriceAsc(String title, Pageable pageable);

    Page<Course> findAllByUserAndDeletedIsFalse(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByRatingsDesc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByRatingsAsc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByTitleDesc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByTitleAsc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByAuthorDesc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByAuthorAsc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByPriceDesc(User user, Pageable pageable);
    Page<Course> findAllByUserAndDeletedIsFalseOrderByPriceAsc(User user, Pageable pageable);


    Page<Course> findAllByDeletedIsFalseAndSchoolsIn(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByRatingsDesc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByRatingsAsc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByTitleDesc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByTitleAsc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByAuthorDesc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByAuthorAsc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByPriceDesc(Pageable pageable, List<User> schools);
    Page<Course> findAllByDeletedIsFalseAndSchoolsInOrderByPriceAsc(Pageable pageable, List<User> schools);

    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsIn(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByRatingsDesc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByRatingsAsc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByTitleDesc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByTitleAsc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByAuthorDesc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByAuthorAsc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByPriceDesc(String title, Pageable pageable, List<User> schools);
    Page<Course> findAllByTitleAndDeletedIsFalseAndSchoolsInOrderByPriceAsc(String title, Pageable pageable, List<User> schools);

    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsIn(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByRatingsDesc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByRatingsAsc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByTitleDesc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByTitleAsc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByAuthorDesc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByAuthorAsc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByPriceDesc(User user, Pageable pageable, List<User> schools);
    Page<Course> findAllByUserAndDeletedIsFalseAndSchoolsInOrderByPriceAsc(User user, Pageable pageable, List<User> schools);

    Course findCourseByTitle(String name);

    Course findCourseByTitleAndIdIsNot(String title, Long id);

    Page<Course> findByTitleContainsAllIgnoreCase(String searchText, Pageable pagin);

    List<Course> findAllByUser(User user);

    List<Course> findAllByUser(User user, Pageable pageable);

    long countCoursesByUser(User user);
    long countCoursesByUserAndStatus(User user, CourseStatus status);

    //subSection
    List<Course> findAllBySubSectionIdAndSchoolsNewIsNullAndStatus(Long subSectionId, CourseStatus status);

    List<Course> findAllBySubSectionId(Long subSectionId);

    List<Course> findAllBySubSectionIdAndSchoolsIn(Long subSectionId, User school);


    //subCategory
    List<Course> findAllBySubCategoryIdAndSchoolsIsNullAndStatus(Long subCategoryId, CourseStatus status);

    List<Course> findAllBySubCategoryIdAndSchoolsIn(Long subCategoryId, User school);

    /*
        find last created
     */
    @Query("select c from Course c where c.schools IS EMPTY and c.status=?1 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedPublicCoursesWithStatus(CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedPublicCoursesWithAnyStatus(Pageable pageable);

   //private last created

    List<Course> findLastCreatedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(Category category, User school, Pageable pageable);

    List<Course> findLastCreatedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(SubCategory subCategory, User school, Pageable pageable);

    List<Course> findLastCreatedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(Section section, User school, Pageable pageable);

    List<Course> findLastCreatedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(SubSection subSection, User school, Pageable pageable);

    List<Course> findLastCreatedPrivateCoursesByDeletedFalseAndSchoolsInOrderByCreationDateDesc(User school, Pageable pageable);


    @Query("select c from Course c where c.schools IS EMPTY and c.category=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedByCategoryPublicCoursesWithStatus(Category category, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subCategory=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubCategoryPublicCoursesWithStatus(SubCategory subCategory, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.section=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySectionPublicCoursesWithStatus(Section section, CourseStatus status, Pageable pageable);

    @Query("select c from Course c where c.schools IS EMPTY and c.subSection=?1 and c.status=?2 and c.deleted=false order by c.creationDate desc")
    List<Course> findLastCreatedBySubSectionPublicCoursesWithStatus(SubSection subSection, CourseStatus status, Pageable pageable);



    //private best rated

    List<Course> findBestRatedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByRatingsDesc(Category category, User school, Pageable pageable);

    List<Course> findBestRatedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByRatingsDesc(SubCategory subCategory, User school, Pageable pageable);

    List<Course> findBestRatedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByRatingsDesc(Section section, User school, Pageable pageable);

    List<Course> findBestRatedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByRatingsDesc(SubSection subSection, User school, Pageable pageable);

    List<Course> findBestRatedPrivateCoursesByDeletedFalseAndSchoolsInOrderByRatingsDesc(User school, Pageable pageable);
    /*
       public best rated
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


    //private featured courses

    List<Course> findFeaturedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(Category category, User school, Pageable pageable);

    List<Course> findFeaturedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(SubCategory subCategory, User school, Pageable pageable);

    List<Course> findFeaturedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(Section section, User school, Pageable pageable);

    List<Course> findFeaturedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(SubSection subSection, User school, Pageable pageable);

    List<Course> findFeaturedPrivateCoursesByDeletedFalseAndSchoolsInOrderByEnrolledDesc(User school, Pageable pageable);
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

    long countCoursesBySchoolsIsNull();

    long countCoursesBySchoolsIsNullAndStatus(CourseStatus status);
}
