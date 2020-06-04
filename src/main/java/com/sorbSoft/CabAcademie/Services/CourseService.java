package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Repository.*;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.CourseFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.CourseMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import com.sorbSoft.CabAcademie.annotation.NotUsed;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
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
    private SectionRepository sectionRepository;

    @Autowired
    private SubSectionService subSectionService;

    @Autowired
    private SubSectionRepository subSectionRepository;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolServices rolServices;

    @Autowired
    private OverviewRepository overviewRepository;

    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);

    public List<Course> fetchAllCourses(){
        return courseRepository.findAll();
    }

    public Page<Course> fetchAllCoursesByPage(int page, int itemsPerPage){
        Pageable pageable = new PageRequest(page - 1 , itemsPerPage);
        return courseRepository.findAll(pageable);
    }

    public Course fetchCourse(Long id){
        return courseRepository.findOne(id);
    }

    public Result updateCourse(CourseViewModel vm){
        Result result = new Result();
        Course current = courseRepository.findOne(vm.getId());

        if (current == null) {
            result.add("The course you want to update does not exist");
            return result;
        }

        Course savedCourse = courseRepository.findCourseByTitleAndIdIsNot(vm.getTitle(), vm.getId());
        if (savedCourse != null) {
            result.add("The course name already exist for another definition");
            return result;
        }
        Course currentCourse = courseRepository.findOne(vm.getId());
        if(currentCourse == null) {
            result.add("The course you are trying to edit does not exist anymore");
            return result;
        }

        return save(vm);
    }

    //and this
    public boolean exists(Long id) {
        return courseRepository.exists(id);
    }

    private  Result save (CourseViewModel vm) {
        Result result = new Result();
        Course course = null;
        try {
            course = courseRepository.save(getEntity(vm));
            if (vm.getId() > 0) {
                Overview overviewEntity = course.getOverview();
                course.setOverview(null);
                Course savedCourse = courseRepository.save(course);
                if (overviewEntity != null) {
                    overviewEntity.setCourse(savedCourse);
                    Overview overview = overviewRepository.save(overviewEntity);
                    course.setOverview(overview);
                    courseRepository.save(course);
                }
            }
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public Result deleteCourse(Long id){
        Result result = new Result();

        if (id <= 0L) {
            result.add("You should indicate the id of the course");
            return result;
        }
        Course course = fetchCourse(id);
        if (course == null) {
            result.add("The course you want to delete doesn't exist");
            return result;
        }
        course.setDeleted(true);
        try {
            courseRepository.save(course);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
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

        Rol rolSchool = rolServices.findRoleByDescription(Roles.ROLE_SCHOOL.name());
        if (rolSchool != null){
            vm.setAllSchools(userServices.filterUserByRole(rolSchool.getId()));
        }
        vm.setSections(sectionService.fetchAllSection()); // TODO: could be filtered
        vm.setSubSections(subSectionService.fetchAllSubSections()); // TODO: could be filtered
        vm.setSubCategories(subCategoryService.fetchAllSubCategories()); // TODO: could be filtered
        vm.setCategories(categoryService.fetchAllCategories());// TODO: could be filtered
        vm.setUsers(userServices.findAllUser()); // Filtered // TODO: could have more filters
        return vm;
    }
    
    public Result saveCourse(CourseViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateCourse(vm);
        } else {
            Course savedCourse = courseRepository.findCourseByTitle(vm.getTitle());

            if ( savedCourse != null){
                result.add("The course you are trying to save already exist");
                return result;
            }

            return save(vm);
        }
    }

    public List<Course> fetchPublicCourseBySubSection(Long subSectionId) {
        return courseRepository.findAllBySubSectionIdAndSchoolsIsNull(subSectionId);
    }

    public List<Course> fetchPrivateCourseBySubSectionAlternative(Long subSectionId, String userName) {
        User user = userRepository.findByUsername(userName);

        List<User> schoolsAndOrganizations = new ArrayList<>();
        schoolsAndOrganizations.addAll(user.getSchools());
        schoolsAndOrganizations.addAll(user.getOrganizations());

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schoolsAndOrganizations) {
            List<Course> coursesBySubSectionAndSchool = courseRepository.findAllBySubSectionIdAndSchoolsIn(subSectionId, schoolOrOrganization);
            courses.addAll(coursesBySubSectionAndSchool);
        }

        return courses;
    }


    @Deprecated
    @NotUsed
    public List<Course> fetchPrivateCourseBySubSection(Long subSectionId, String userName) {
        User user = userRepository.findByUsername(userName);

        List<User> schoolsAndOrganizations = new ArrayList<>();
        schoolsAndOrganizations.addAll(user.getSchools());
        schoolsAndOrganizations.addAll(user.getOrganizations());

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schoolsAndOrganizations) {
            //TODO: should be refactored: added sql query with subquesry at db level
            //select * from Course where Course.id IN (select course_id from Course_School where Course_School.user_id = school.id)
            List<Course> coursesBySubSection = courseRepository.findAllBySubSectionId(subSectionId);
            for(Course course : coursesBySubSection) {
                for(User courseSchool : course.getSchools()) {

                    if(schoolOrOrganization.getId() == courseSchool.getId()) {
                        courses.add(course);
                    }
                }

            }
        }

        return courses;
    }


    public List<Course> fetchPublicCourseBySubCategory(Long subCategoryId) {
        return courseRepository.findAllBySubCategoryIdAndSchoolsIsNull(subCategoryId);
    }

    public List<Course> fetchPrivateCourseBySubCategory(Long subCategoryId, String userName) {
        User user = userRepository.findByUsername(userName);

        List<User> schoolsAndOrganizations = new ArrayList<>();
        schoolsAndOrganizations.addAll(user.getSchools());
        schoolsAndOrganizations.addAll(user.getOrganizations());

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schoolsAndOrganizations) {
            List<Course> coursesBySubSectionAndSchool = courseRepository.findAllBySubCategoryIdAndSchoolsIn(subCategoryId, schoolOrOrganization);
            courses.addAll(coursesBySubSectionAndSchool);
        }

        return courses;
    }



    private Result ValidateModel(CourseViewModel vm){
        Result result = new Result();

        if (vm.getTitle().isEmpty()) {
            result.add("You should specify the title of the course");
            return result;
        }

        if (vm.isPremium()) {
            if (vm.getPrice()<=0){
                result.add("You should specify the price of the course");
                return result;
            }
        }

        if (vm.getAuthor().isEmpty()) {
            result.add("You should specify the author of the course");
            return result;
        }

        if (vm.getStartDate()== null) {
            result.add("You should specify the start date");
            return result;
        }
        if (vm.getEndDate()== null) {
            result.add("You should specify the end date");
            return result;
        }
        if (vm.getStartDate().after(vm.getEndDate())) {
            result.add("The start date cannot be greater than the end date");
            return result;
        }
        if (vm.getUser().getId() <= 0) {
            result.add("You should specify the user");
            return result;
        }

        User user = userRepository.findOne(vm.getUser().getId());
        if (user == null) {
            result.add("The user you specified does not exist");
            return result;
        }

        if (vm.getCategory().getId() <= 0){
            result.add("You should specify a category for the course");
            return result;
        }

        Category category = categoryRepository.findOne(vm.getCategory().getId());
        if (category == null) {
            result.add("The category you specified does not exist");
            return result;
        }

        if (vm.getSubCategory().getId() > 0){
            SubCategory subCategory = subCategoryRepository.findOne(vm.getSubCategory().getId());
            if (subCategory == null) {
                result.add("The sub-category you specified does not exist");
                return result;
            }
        }

        if (vm.getSection().getId() <= 0){
            result.add("You should specify a section for the course");
            return result;
        }

        Section section = sectionRepository.findOne(vm.getSection().getId());
        if (section == null) {
            result.add("The section you specified does not exist");
            return result;
        }

        if (vm.getSubSection().getId() > 0){
            SubSection subSection = subSectionRepository.findOne(vm.getSubSection().getId());
            if (subSection == null) {
                result.add("The sub-section you specified does not exist");
                return result;
            }
        }

        result.add(validateOverview(vm.getOverview()));
        result.add(userServices.validateSchools(vm.getSchools()));
        result.add(validateObjectives(vm.getObjectives()));

        return result;
    }

    private CourseViewModel prepareEntity(CourseViewModel vm) {
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getTitle()== null)  {
            vm.setTitle("");
        }
        if (vm.getDescription()== null)  {
            vm.setDescription("");
        }
        if (vm.getImageUrl()== null)  {
            vm.setImageUrl("");
        }
        if (vm.getAuthor() == null) {
            vm.setAuthor("");
        }
        if (vm.getPrice() < 0) {
            vm.setPrice(0D);
        }
        if (vm.getRatings() < 0) {
            vm.setRatings(0);
        }
        if (vm.getEnrolled() < 0) {
            vm.setEnrolled(0);
        }
        if (vm.getUser()== null) {
            vm.setUser(new User(){
                @Override
                public Long getId() {
                    return 0L;
                }
            });
        }
        if (vm.getSyllabus()== null) {
            vm.setSyllabus(new ArrayList<>());
        }

        if (vm.getCategory()== null) {
            vm.setCategory(new Category(){
                @Override
                public Long getId() {
                    return 0L;
                }
            });
        }
//        if (vm.getSubCategory()== null) {
//            vm.setSubCategory(new SubCategory(){
//                @Override
//                public Long getId() {
//                    return 0L;
//                }
//            });
//        }
        if (vm.getSection()== null) {
            vm.setSection(new Section(){
                @Override
                public Long getId() {
                    return 0L;
                }
            });
        }
//        if (vm.getSubSection()== null) {
//            vm.setSubSection(new SubSection(){
//                @Override
//                public Long getId() {
//                    return 0L;
//                }
//            });
//        }
        if (vm.getOverview()!= null) {
            if (vm.getOverview().getFeatures().size() > 0) {
                for (Feature feature : vm.getOverview().getFeatures()) {
                    if (feature.getId() < 0L) {
                        feature.setId(0L);
                    }
                    if (feature.getTitle() == null) {
                        feature.setTitle("");
                    }
                    if (feature.getIcon() == null) {
                        feature.setIcon("");
                    }
                }
            }

            if (vm.getOverview().getRequirements().size() > 0) {
                for (Requirement requirement : vm.getOverview().getRequirements()) {
                    if (requirement.getDescription() == null) {
                       requirement.setDescription("");
                    }
                    if (requirement.getId() < 0L) {
                        requirement.setId(0L);
                    }
                }
            }
        }
        if (vm.getObjectives()== null) {
            vm.setObjectives(new ArrayList<>());
        } else {
            for (Objective objective: vm.getObjectives()) {
                if (objective.getDescription().isEmpty()) {
                    objective.setDescription("");
                }
                if (objective.getId() < 0L) {
                    objective.setId(0L);
                }
            }
        }
        if (vm.getSchools()== null) {
            vm.setSchools(new ArrayList<>());
        }

        return vm;
    }

    private Result validateOverview(Overview overview) {
        Result result = new Result();
        if (overview == null) {
            return result;
        }

        if (overview.getFeatures().size() > 0) {
            for (Feature feature : overview.getFeatures()) {
                if (feature.getTitle().isEmpty()) {
                    result.add(String.format("For the feature no. {0} in the list of features the title cannot be empty", overview.getFeatures().indexOf(feature) + 1));
                    return result;
                }

            }
        }

        if (overview.getRequirements().size() > 0) {
            for (Requirement requirement : overview.getRequirements()) {
                if (requirement.getDescription().isEmpty()) {
                    result.add(String.format("For the requirement no. {0} in the list of requirements the title cannot be empty", overview.getRequirements().indexOf(requirement) + 1));
                    return result;
                }
            }
        }

        return  result;
    }

    private Result validateObjectives(List<Objective> objectives) {
        Result result = new Result();
        if (objectives.size() <= 0) {
            return result;
        }

        for (Objective objective: objectives) {
            if (objective.getDescription().isEmpty()) {
                result.add(String.format("For the objective no. {0} in the list of objetives the title cannot be empty", objectives.indexOf(objective) + 1));
                return result;
            }
        }

        return  result;
    }


    private Course getEntity(CourseViewModel vm){
        Course resultCourse = mapper.mapToEntity(vm);
        resultCourse.setLastUpdate(new Date());
        if (vm.getId() <= 0) {
            resultCourse.setCreationDate(new Date());
        }
        return resultCourse;
    }

    public List<Course> fetchLastAddedPublicCourses(int amount) {

        Pageable pageable = new PageRequest(0, amount);
        List<Course> lastCreated = courseRepository.findLastCreatedPublicCourses(pageable);

        return lastCreated;
    }

    //TODO: finish
    /*public List<Course> fetchLastAddedPrivateCourses(int amount, String userName) {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        List<User> schoolsAndOrganizations = new ArrayList<>();
        schoolsAndOrganizations.addAll(user.getSchools());
        schoolsAndOrganizations.addAll(user.getOrganizations());

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schoolsAndOrganizations) {
            List<Course> coursesBySubSectionAndSchool = courseRepository.findLastCreatedPrivateCourses(pageable, schoolOrOrganization);
            courses.addAll(coursesBySubSectionAndSchool);
        }

        return courses;
    }*/

    public List<Course> fetchBestRatedPublicCourses(int amount) {
        Pageable pageable = new PageRequest(0, amount);
        List<Course> bestRated = courseRepository.findBestRatedPublicCourses(pageable);

        return bestRated;
    }

    public List<Course> fetchFeaturedPublicCourses(int amount) {
        Pageable pageable = new PageRequest(0, amount);
        List<Course> featureed = courseRepository.findFeaturedPublicCourses(pageable);

        return featureed;
    }

    public List<Course> fetchLastAddedByCategoryPublicCourses(Integer amount, Long categoryId) {
        Pageable pageable = new PageRequest(0, amount);
        Category category = categoryRepository.findOne(categoryId);
        List<Course> lastCreated = courseRepository.findLastCreatedByCategoryPublicCourses(category, pageable);

        return lastCreated;
    }

    public List<Course> fetchLastAddedBySubCategoryPublicCourses(Integer amount, Long subCategoryId) {
        Pageable pageable = new PageRequest(0, amount);
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        List<Course> lastCreated = courseRepository.findLastCreatedBySubCategoryPublicCourses(subCategory, pageable);

        return lastCreated;
    }

    public List<Course> fetchLastAddedBySectionPublicCourses(Integer amount, Long sectionId) {
        Pageable pageable = new PageRequest(0, amount);
        Section section = sectionRepository.findOne(sectionId);
        List<Course> lastCreated = courseRepository.findLastCreatedBySectionPublicCourses(section, pageable);

        return lastCreated;
    }

    public List<Course> fetchLastAddedBySubSectionPublicCourses(Integer amount, Long subSectionId) {
        Pageable pageable = new PageRequest(0, amount);
        SubSection subSection = subSectionRepository.findOne(subSectionId);
        List<Course> lastCreated = courseRepository.findLastCreatedBySubSectionPublicCourses(subSection, pageable);

        return lastCreated;
    }

    public List<Course> fetchBestRatedByCategoryPublicCourses(Integer amount, Long categoryId) {
        Pageable pageable = new PageRequest(0, amount);
        Category category = categoryRepository.findOne(categoryId);
        List<Course> bestRated = courseRepository.findBestRatedByCategoryPublicCourses(category, pageable);

        return bestRated;
    }

    public List<Course> fetchBestRatedBySubCategoryPublicCourses(Integer amount, Long subCategoryId) {
        Pageable pageable = new PageRequest(0, amount);
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        List<Course> bestRated = courseRepository.findBestRatedBySubCategoryPublicCourses(subCategory, pageable);

        return bestRated;
    }

    public List<Course> fetchBestRatedBySectionPublicCourses(Integer amount, Long sectionId) {
        Pageable pageable = new PageRequest(0, amount);
        Section section = sectionRepository.findOne(sectionId);
        List<Course> bestRated = courseRepository.findBestRatedBySectionPublicCourses(section, pageable);

        return bestRated;
    }

    public List<Course> fetchBestRatedBySubSectionPublicCourses(Integer amount, Long subSectionId) {
        Pageable pageable = new PageRequest(0, amount);
        SubSection subSection = subSectionRepository.findOne(subSectionId);
        List<Course> bestRated = courseRepository.findBestRatedBySubSectionPublicCourses(subSection, pageable);

        return bestRated;
    }

    public List<Course> fetchFeaturedByCategoryPublicCourses(Integer amount, Long categoryId) {
        Pageable pageable = new PageRequest(0, amount);
        Category category = categoryRepository.findOne(categoryId);
        List<Course> featured = courseRepository.findFeaturedByCategoryPublicCourses(category, pageable);

        return featured;
    }

    public List<Course> fetchFeaturedBySubCategoryPublicCourses(Integer amount, Long subCategoryId) {
        Pageable pageable = new PageRequest(0, amount);
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        List<Course> featured = courseRepository.findFeaturedBySubCategoryPublicCourses(subCategory, pageable);

        return featured;
    }

    public List<Course> fetchFeaturedBySectionPublicCourses(Integer amount, Long sectionId) {
        Pageable pageable = new PageRequest(0, amount);
        Section section = sectionRepository.findOne(sectionId);
        List<Course> featured = courseRepository.findFeaturedBySectionPublicCourses(section, pageable);

        return featured;
    }

    public List<Course> fetchFeaturedBySubSectionPublicCourses(Integer amount, Long subSectionId) {
        Pageable pageable = new PageRequest(0, amount);
        SubSection subSection = subSectionRepository.findOne(subSectionId);
        List<Course> featured = courseRepository.findFeaturedBySubSectionPublicCourses(subSection, pageable);

        return featured;
    }


    // TODO: Update number of enrolled
    // TODO: Update rating

}
