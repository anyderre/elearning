package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Repository.*;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.CourseFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.CourseMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import com.sorbSoft.CabAcademie.exception.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private GenericValidator validator;

    @Autowired
    private SubscriptionPlanValidator subscriptionValidator;

    @Autowired
    private CourseSchoolRepository courseSchoolRepository;

    @Value("${stripe.enabled}")
    private Boolean isStripeEnabled;

    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);

    public Result saveCourseByAdmin(CourseViewModel vm, String adminUsername) throws UserNotFoundExcepion, SchoolNotFoundExcepion, MaxCoursesPerProfessorExceededException, CourseNotFoundExcepion, CoureTitleAlreadyTakenExcepion, PaymentException, SubscriptionPlanDateExpired {
        //TODO: add course add validation. Validate if admin adds course to schools, orgs which he owns
        if(isStripeEnabled) {
            subscriptionValidator.validateAddCourseByAdmin(vm.getUser().getId(), adminUsername);
        }
        return saveCourse(vm, adminUsername);
    }

    public Result saveCourse(CourseViewModel vm, String userName) throws CourseNotFoundExcepion, CoureTitleAlreadyTakenExcepion {
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateCourse(vm, userName);
        } else {
            Course savedCourse = courseRepository.findCourseByTitle(vm.getTitle());

            if ( savedCourse != null){
                result.add("The course you are trying to save already exist");
                return result;
            }

            return save(vm);
        }
    }

    private  Result save (CourseViewModel vm) {
        Result result = new Result();
        Course course = null;
        try {
            course = getEntity(vm);


            if (vm.getId() > 0 || course.getOverview() != null || course.getSchools() != null) {
                Overview overviewEntity = course.getOverview();
                course.setOverview(null);

                List<User> schools = course.getSchools();
                //course.setSchoolsNew(null);

                Course savedCourse = courseRepository.save(course);

                if(vm.getId() > 0) {
                    removeCourseSchools(vm);
                }

                if(schools != null && schools.isEmpty() == false) {

                    List<CourseSchool> schoolsNew = new ArrayList<>();
                    for(User school : vm.getSchools()) {
                        CourseSchool courseSchool = new CourseSchool();
                        courseSchool.setCourse(savedCourse);
                        courseSchool.setSchool(school);
                        courseSchool.setTeacher(savedCourse.getUser());
                        courseSchool.setStatus(vm.getStatus());

                        //CourseSchool courseSchoolSaved = courseSchoolRepository.save(courseSchool);
                        schoolsNew.add(courseSchool);

                    }
                    List<CourseSchool> schoolsNewSaved = courseSchoolRepository.save(schoolsNew);

                    savedCourse.setSchoolsNew(schoolsNewSaved);
                    courseRepository.save(savedCourse);
                }


                if (overviewEntity != null) {
                    overviewEntity.setCourse(savedCourse);
                    Overview overview = overviewRepository.save(overviewEntity);

                    savedCourse.setOverview(overview);

                    courseRepository.save(savedCourse);
                }
            } else {
                course = courseRepository.save(course);
            }
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    private void removeCourseSchools(CourseViewModel vm) {
        User teacher = userRepository.findById(vm.getUser().getId());
        Course existingCourse = courseRepository.findOne(vm.getId());

        courseSchoolRepository.deleteAllByCourseAndTeacher(existingCourse, teacher);
    }

    private Course getEntity(CourseViewModel vm){
        Course resultCourse = mapper.mapToEntity(vm);
        resultCourse.setLastUpdate(new Date());
        if (vm.getId() <= 0) {
            resultCourse.setCreationDate(new Date());
        } else {
            resultCourse.setLastUpdate(new Date());
        }

       /* if(vm.getSchools()!=null) {

            //TODO: check and maybe improve
            List<CourseSchool> schools = new ArrayList<>();
            for(User school : vm.getSchools()) {
                CourseSchool courseSchool = new CourseSchool();
                courseSchool.setCourse(resultCourse);
                courseSchool.setSchool(school);
                courseSchool.setTeacher(vm.getUser());
                courseSchool.setStatus(vm.getStatus());

                schools.add(courseSchool);
            }

            resultCourse.setSchoolsNew(schools);

        }*/
        if(isPublicCourse(vm)){
            resultCourse.setStatus(vm.getStatus());
        }
        if(isPublicCourse(vm) == false) {
            resultCourse.setStatus(null);
        }

        return resultCourse;
    }

    private boolean isPublicCourse(CourseViewModel vm) {
        return vm.getSchools() == null || vm.getSchools().isEmpty();
    }

    private boolean isPublicCourse(Course course) {
        return course.getSchools() == null || course.getSchools().isEmpty();
    }

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

    public Result updateCourse(CourseViewModel vm, String userName) throws CourseNotFoundExcepion, CoureTitleAlreadyTakenExcepion {
        Result result = new Result();
        Course current = courseRepository.findOne(vm.getId());

        if (current == null) {
            throw new CourseNotFoundExcepion("The course you want to update does not exist");
        }

        Course savedCourse = courseRepository.findCourseByTitleAndIdIsNot(vm.getTitle(), vm.getId());
        if (savedCourse != null) {
            throw new CoureTitleAlreadyTakenExcepion("The course name already exist for another definition");
        }
        Course currentCourse = courseRepository.findOne(vm.getId());
        if(currentCourse == null) {
            throw new CourseNotFoundExcepion("The course you are trying to edit does not exist anymore");
        }

        return save(vm);
    }

    //and this
    public boolean exists(Long id) {
        return courseRepository.exists(id);
    }

    public List<CourseViewModel> fetchAllCoursesByUser(Long userId) throws UserNotFoundExcepion {
        validator.validateNull(userId, "userId");
        User user = userRepository.findById(userId);

        validateIfUserWasFound(user, "id: "+userId);

        List<Course> courses = courseRepository.findAllByUser(user);

        List<CourseViewModel> vms = new ArrayList<>(courses.size());

        courses.forEach(c ->  vms.add(mapper.mapToViewModel(c)) );

        return vms;
    }

    public List<CourseViewModel> fetchAllCoursesByUserAndAmount(Long userId, Integer amount) throws UserNotFoundExcepion {

        validator.validateNull(userId, "userId");
        validator.validateNull(amount, "amount");

        User user = userRepository.findById(userId);

        validateIfUserWasFound(user, "id: "+userId);

        Pageable pageable = new PageRequest(0, amount);
        List<Course> courses = courseRepository.findAllByUser(user, pageable);

        List<CourseViewModel> vms = new ArrayList<>(courses.size());

        courses.forEach(c ->  vms.add(mapper.mapToViewModel(c)) );

        return vms;
    }

    public long countUserCourses(Long userId) throws UserNotFoundExcepion {

        validator.validateNull(userId, "userId");

        User user = userRepository.findById(userId);

        validateIfUserWasFound(user, "id: "+userId);

        return courseRepository.countCoursesByUser(user);
    }

    public long countUserCoursesWithStatus(Long userId, CourseStatus status) throws UserNotFoundExcepion, EmptyValueException {

        validator.validateNull(userId, "userId");

        if(status == null) {
            throw new EmptyValueException("Course status can't be null");
        }

        User user = userRepository.findById(userId);

        validateIfUserWasFound(user, "id: "+userId);

        return courseRepository.countCoursesByUserAndStatus(user, status);
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
            vm.setAllSchools(userServices.filterUserByRole(rolSchool));
        }
        vm.setSections(sectionService.fetchAllSection()); // TODO: could be filtered
        vm.setSubSections(subSectionService.fetchAllSubSections()); // TODO: could be filtered
        vm.setSubCategories(subCategoryService.fetchAllSubCategories()); // TODO: could be filtered
        vm.setCategories(categoryService.fetchAllCategories());// TODO: could be filtered
        vm.setUsers(userServices.findAllUser()); // Filtered // TODO: could have more filters
        return vm;
    }

    public List<Course> fetchPublicCourseBySubSection(Long subSectionId) {
        return courseRepository.findAllBySubSectionIdAndSchoolsNewIsNullAndStatus(subSectionId, CourseStatus.APPROVED);
    }

    public List<Course> fetchPrivateCourseBySubSectionAlternative(Long subSectionId, String userName) {
        User user = userRepository.findByUsername(userName);

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            List<Course> coursesBySubSectionAndSchool
                    = courseRepository.findAllBySubSectionIdAndSchoolsIn(subSectionId, schoolOrOrganization);

            List<Course> approvedCoursesAtSchool = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, CourseStatus.APPROVED);

            courses.addAll(approvedCoursesAtSchool);
        }

        return courses;
    }

    private List<Course> getSchoolCoursesByStatus(List<Course> coursesBySubSectionAndSchool, User school, CourseStatus status) {
        List<Course> courses = new ArrayList<>();
        for(Course course : coursesBySubSectionAndSchool) {
            for(CourseSchool cs : course.getSchoolsNew()) {
                if(cs.getStatus() == status
                        && cs.getSchool().equals(school))
                {
                    courses.add(course);
                }
            }
        }

        return courses;
    }


    public List<Course> fetchPublicCourseBySubCategory(Long subCategoryId) {
        return courseRepository.findAllBySubCategoryIdAndSchoolsIsNullAndStatus(subCategoryId, CourseStatus.APPROVED);
    }

    public List<Course> fetchPrivateCourseBySubCategory(Long subCategoryId, String userName) {
        User user = userRepository.findByUsername(userName);

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            List<Course> coursesBySubSectionAndSchool = courseRepository.findAllBySubCategoryIdAndSchoolsIn(subCategoryId, schoolOrOrganization);

            List<Course> approvedCoursesAtSchool = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, CourseStatus.APPROVED);
            courses.addAll(approvedCoursesAtSchool);
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

    public List<Course> fetchLastAddedPublicCourses(int amount) {

        return fetchLastAddedPublicCoursesByStatus(amount, CourseStatus.APPROVED);
    }

    public List<Course> fetchLastAddedPublicCoursesByStatus(int amount, CourseStatus status) {

        Pageable pageable = new PageRequest(0, amount);
        List<Course> lastCreated = null;

        if(status == null) {
            lastCreated = courseRepository.findLastCreatedPublicCoursesWithAnyStatus(pageable);
        } else {
            lastCreated = courseRepository.findLastCreatedPublicCoursesWithStatus(status, pageable);
        }

        return lastCreated;
    }

    //TODO: finish
    public List<Course> fetchLastAddedPrivateCourses(int amount, String userName) {
        return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.APPROVED);
    }

    public List<Course> fetchLastAddedPrivateCoursesByStatus(int amount, String userName, CourseStatus status) {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesByDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesByDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }



    public List<Course> fetchBestRatedPublicCourses(int amount) {
        Pageable pageable = new PageRequest(0, amount);
        List<Course> bestRated = courseRepository.findBestRatedPublicCoursesWithStatus(CourseStatus.APPROVED, pageable);

        return bestRated;
    }

    public List<Course> fetchFeaturedPublicCourses(int amount) {
        Pageable pageable = new PageRequest(0, amount);
        List<Course> featureed = courseRepository.findFeaturedPublicCoursesWithStatus(CourseStatus.APPROVED, pageable);

        return featureed;
    }

    public List<Course> fetchLastAddedByCategoryPublicCourses(Integer amount, Long categoryId) {
        Pageable pageable = new PageRequest(0, amount);
        Category category = categoryRepository.findOne(categoryId);
        List<Course> lastCreated = courseRepository.findLastCreatedByCategoryPublicCoursesWithStatus(category, CourseStatus.APPROVED, pageable);

        return lastCreated;
    }

    public List<Course> fetchLastAddedBySubCategoryPublicCourses(Integer amount, Long subCategoryId) {
        Pageable pageable = new PageRequest(0, amount);
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        List<Course> lastCreated = courseRepository.findLastCreatedBySubCategoryPublicCoursesWithStatus(subCategory, CourseStatus.APPROVED, pageable);

        return lastCreated;
    }

    public List<Course> fetchLastAddedBySectionPublicCourses(Integer amount, Long sectionId) {
        Pageable pageable = new PageRequest(0, amount);
        Section section = sectionRepository.findOne(sectionId);
        List<Course> lastCreated = courseRepository.findLastCreatedBySectionPublicCoursesWithStatus(section, CourseStatus.APPROVED, pageable);

        return lastCreated;
    }

    public List<Course> fetchLastAddedBySubSectionPublicCourses(Integer amount, Long subSectionId) {
        Pageable pageable = new PageRequest(0, amount);
        SubSection subSection = subSectionRepository.findOne(subSectionId);
        List<Course> lastCreated = courseRepository.findLastCreatedBySubSectionPublicCoursesWithStatus(subSection, CourseStatus.APPROVED, pageable);

        return lastCreated;
    }

    public List<Course> fetchBestRatedByCategoryPublicCourses(Integer amount, Long categoryId) {
        Pageable pageable = new PageRequest(0, amount);
        Category category = categoryRepository.findOne(categoryId);
        List<Course> bestRated = courseRepository.findBestRatedByCategoryPublicCoursesWithStatus(category, CourseStatus.APPROVED, pageable);

        return bestRated;
    }

    public List<Course> fetchBestRatedBySubCategoryPublicCourses(Integer amount, Long subCategoryId) {
        Pageable pageable = new PageRequest(0, amount);
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        List<Course> bestRated = courseRepository.findBestRatedBySubCategoryPublicCoursesWithStatus(subCategory, CourseStatus.APPROVED, pageable);

        return bestRated;
    }

    public List<Course> fetchBestRatedBySectionPublicCourses(Integer amount, Long sectionId) {
        Pageable pageable = new PageRequest(0, amount);
        Section section = sectionRepository.findOne(sectionId);
        List<Course> bestRated = courseRepository.findBestRatedBySectionPublicCoursesWithStatus(section, CourseStatus.APPROVED, pageable);

        return bestRated;
    }

    public List<Course> fetchBestRatedBySubSectionPublicCourses(Integer amount, Long subSectionId) {
        Pageable pageable = new PageRequest(0, amount);
        SubSection subSection = subSectionRepository.findOne(subSectionId);
        List<Course> bestRated = courseRepository.findBestRatedBySubSectionPublicCoursesWithStatus(subSection, CourseStatus.APPROVED, pageable);

        return bestRated;
    }

    public List<Course> fetchFeaturedByCategoryPublicCourses(Integer amount, Long categoryId) {
        Pageable pageable = new PageRequest(0, amount);
        Category category = categoryRepository.findOne(categoryId);
        List<Course> featured = courseRepository.findFeaturedByCategoryPublicCoursesWithStatus(category, CourseStatus.APPROVED, pageable);

        return featured;
    }

    public List<Course> fetchFeaturedBySubCategoryPublicCourses(Integer amount, Long subCategoryId) {
        Pageable pageable = new PageRequest(0, amount);
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        List<Course> featured = courseRepository.findFeaturedBySubCategoryPublicCoursesWithStatus(subCategory, CourseStatus.APPROVED, pageable);

        return featured;
    }

    public List<Course> fetchFeaturedBySectionPublicCourses(Integer amount, Long sectionId) {
        Pageable pageable = new PageRequest(0, amount);
        Section section = sectionRepository.findOne(sectionId);
        List<Course> featured = courseRepository.findFeaturedBySectionPublicCoursesWithStatus(section, CourseStatus.APPROVED, pageable);

        return featured;
    }

    public List<Course> fetchFeaturedBySubSectionPublicCourses(Integer amount, Long subSectionId) {
        Pageable pageable = new PageRequest(0, amount);
        SubSection subSection = subSectionRepository.findOne(subSectionId);
        List<Course> featured = courseRepository.findFeaturedBySubSectionPublicCoursesWithStatus(subSection, CourseStatus.APPROVED, pageable);

        return featured;
    }

    public Long countAllCoursesInSchool(String adminUsername) throws SchoolNotFoundExcepion, UserNotFoundExcepion {
        return countCoursesInSchool(adminUsername, null);
    }

    public Long countPendingCoursesInSchool(String adminUsername) throws SchoolNotFoundExcepion, UserNotFoundExcepion {
        return countCoursesInSchool(adminUsername, CourseStatus.PENDING);
    }

    public Long countDeclinedCoursesInSchool(String adminUsername) throws SchoolNotFoundExcepion, UserNotFoundExcepion {
        return countCoursesInSchool(adminUsername, CourseStatus.DECLINED);
    }

    public Long countApprovedCoursesInSchool(String adminUsername) throws SchoolNotFoundExcepion, UserNotFoundExcepion {
        return countCoursesInSchool(adminUsername, CourseStatus.APPROVED);
    }

    public Long countCoursesInSchool(String adminUsername, CourseStatus status) throws SchoolNotFoundExcepion, UserNotFoundExcepion {
        User admin = userRepository.findByUsername(adminUsername);

        if (admin == null) {
            throw new UserNotFoundExcepion("User " + adminUsername + " doesn't exist in system");
        }
        List<User> schools = admin.getSchools();

        if (schools == null) {
            throw new SchoolNotFoundExcepion("Admin " + adminUsername + " doesn't belong to any school or organization");
        }

        long coursesCount = 0;

        for (User school : schools) {
            if(status == null) {
                coursesCount = courseSchoolRepository.countCoursesBySchool(school);
            }

            if(status == CourseStatus.PENDING) {
                coursesCount = courseSchoolRepository.countCoursesBySchoolAndStatus(school, CourseStatus.PENDING);
            }

            if(status == CourseStatus.DECLINED) {
                coursesCount = courseSchoolRepository.countCoursesBySchoolAndStatus(school, CourseStatus.DECLINED);
            }

            if(status == CourseStatus.APPROVED) {
                coursesCount = courseSchoolRepository.countCoursesBySchoolAndStatus(school, CourseStatus.APPROVED);
            }
            return coursesCount;
        }

        return coursesCount;
    }

    public boolean makeCourseStatusPending(Long courseId, String userName) throws CourseNotFoundExcepion, UserNotFoundExcepion, CourseAccessDeniedException, SchoolNotFoundExcepion, CourseUpdateDeniedException {
        User user = userRepository.findByUsername(userName);
        validateIfUserWasFound(user, userName);

        Course course = courseRepository.findOne(courseId);
        validateIfCourseWasFound(course, courseId);

        if(course.getUser().getUsername().equals(user.getUsername())) {
            if(isPublicCourse(course)) {
                return changePublicCourseStatus(courseId, CourseStatus.PENDING, "");
            } else {
                String teacherUsername = course.getUser().getUsername();
                return changeCourseStatusBySchoolAdmin(courseId, teacherUsername, CourseStatus.PENDING, "");
            }

        } else {
            throw new CourseAccessDeniedException("You are not an owner of course which you are trying to publish");
        }

    }

    public boolean makeCourseStatusDraft(Long courseId, String userName) throws CourseNotFoundExcepion, UserNotFoundExcepion, CourseAccessDeniedException, SchoolNotFoundExcepion {
        User user = userRepository.findByUsername(userName);
        validateIfUserWasFound(user, userName);

        Course course = courseRepository.findOne(courseId);
        validateIfCourseWasFound(course, courseId);

        if(course.getUser().getUsername().equals(user.getUsername())) {
            if(isPublicCourse(course)) {
                return changePublicCourseStatus(courseId, CourseStatus.DRAFT, "");
            } else {
                String teacherUsername = course.getUser().getUsername();
                return changeCourseStatusBySchoolAdmin(courseId, teacherUsername, CourseStatus.DRAFT, "");
            }
        } else {
            throw new CourseAccessDeniedException("You are not an owner of course which you are trying to make draft");
        }

    }

    public boolean approveCourse(Long courseId) throws CourseNotFoundExcepion {


        return changePublicCourseStatus(courseId, CourseStatus.APPROVED, "");
    }

    public boolean declineCourse(Long courseId, String declineMessage) throws CourseNotFoundExcepion {
        return changePublicCourseStatus(courseId, CourseStatus.DECLINED, declineMessage);
    }

    private boolean changePublicCourseStatus(Long courseId, CourseStatus status, String declineMessage) throws CourseNotFoundExcepion {

        boolean isStatusChanged = false;

        Course course2Approve = courseRepository.findOne(courseId);
        if (course2Approve == null) {
            throw new CourseNotFoundExcepion("Course with id: " + courseId + " was not found in db");
        }

        if(status == CourseStatus.DRAFT) {

            course2Approve.setStatus(CourseStatus.DRAFT);
            course2Approve.setLastUpdate(new Date());
            courseRepository.save(course2Approve);
            isStatusChanged = true;
            return isStatusChanged;
        }

        if(status == CourseStatus.PENDING) {

            course2Approve.setStatus(CourseStatus.PENDING);
            course2Approve.setLastUpdate(new Date());
            courseRepository.save(course2Approve);
            isStatusChanged = true;
            return isStatusChanged;
        }

        if(status == CourseStatus.APPROVED) {

            course2Approve.setStatus(CourseStatus.APPROVED);
            course2Approve.setLastUpdate(new Date());
            courseRepository.save(course2Approve);
            isStatusChanged = true;
            return isStatusChanged;
        }

        if(status == CourseStatus.DECLINED) {

            course2Approve.setDeclineMessage(declineMessage);
            course2Approve.setStatus(CourseStatus.DECLINED);
            course2Approve.setLastUpdate(new Date());
            courseRepository.save(course2Approve);
            isStatusChanged = true;
            return isStatusChanged;
        }


        return isStatusChanged;
    }



    public boolean approveCourse(Long courseId, String adminUsername) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CourseNotFoundExcepion, MaxCoursesPerProfessorExceededException, CourseAccessDeniedException, PaymentException, SubscriptionPlanDateExpired {

        if(isStripeEnabled) {
            subscriptionValidator.validateCourseApprovment(courseId, adminUsername);
        }
        return changeCourseStatusBySchoolAdmin(courseId, adminUsername, CourseStatus.APPROVED, "");
    }

    public boolean declineCourse(Long courseId, String adminUsername, String declineMessage) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CourseNotFoundExcepion, CourseAccessDeniedException {
        return changeCourseStatusBySchoolAdmin(courseId, adminUsername, CourseStatus.DECLINED, declineMessage);
    }

    private boolean changeCourseStatusBySchoolAdmin(Long courseId, String adminUsername, CourseStatus status, String declineMessage) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CourseNotFoundExcepion, CourseAccessDeniedException {
        User admin = userRepository.findByUsername(adminUsername);
        boolean isStatusChanged = false;

        if (admin == null) {
            throw new UserNotFoundExcepion("User " + adminUsername + " doesn't exist in system");
        }

        List<User> adminSchools = admin.getSchools();

        if (adminSchools == null || adminSchools.isEmpty()) {
            throw new SchoolNotFoundExcepion("Admin " + adminUsername + " doesn't belong to any school");
        }

        Course course2Approve = courseRepository.findOne(courseId);

        if (course2Approve == null) {
            throw new CourseNotFoundExcepion("Course with id: " + courseId + " was not found in db");
        }

        if(isProfessor(admin) || isInstructor(admin)) {
            if(!admin.getUsername().equals(course2Approve.getUser().getUsername())) {
                throw new CourseAccessDeniedException("User with id:"+admin.getUsername()+"try to update course status, but is not a course owner");
            }
        }

        List<CourseSchool> courseSchools = course2Approve.getSchoolsNew();
        if (courseSchools == null || courseSchools.isEmpty()) {
            throw new SchoolNotFoundExcepion("Course with id: " + courseId + " doesn't belong to any school");
        }

        //check if admin belong to school course
        for(User adminSchool : adminSchools) {
            for(CourseSchool courseSchool : courseSchools) {
                if(adminSchool.equals(courseSchool.getSchool())) {

                    if(status == CourseStatus.DRAFT) {

                        courseSchool.setStatus(CourseStatus.DRAFT);
                        course2Approve.setLastUpdate(new Date());
                        courseRepository.save(course2Approve);
                        isStatusChanged = true;
                        return isStatusChanged;
                    }

                    if(status == CourseStatus.PENDING) {

                        courseSchool.setStatus(CourseStatus.PENDING);
                        course2Approve.setLastUpdate(new Date());
                        courseRepository.save(course2Approve);
                        isStatusChanged = true;
                        return isStatusChanged;
                    }

                    if(status == CourseStatus.APPROVED) {

                        courseSchool.setStatus(CourseStatus.APPROVED);
                        course2Approve.setLastUpdate(new Date());
                        courseRepository.save(course2Approve);
                        isStatusChanged = true;
                        return isStatusChanged;
                    }

                    if(status == CourseStatus.DECLINED) {

                        courseSchool.setStatus(CourseStatus.DECLINED);
                        courseSchool.setDeclineMessage(declineMessage);
                        course2Approve.setLastUpdate(new Date());
                        courseRepository.save(course2Approve);
                        isStatusChanged = true;
                        return isStatusChanged;
                    }
                }
            }
        }

        return isStatusChanged;
    }

    private boolean isInstructor(User user) {
        return user.getRole().getRole() == Roles.ROLE_INSTRUCTOR;
    }

    private boolean isProfessor(User user) {
        return user.getRole().getRole() == Roles.ROLE_PROFESSOR;
    }

    public List<Course> getLastAddedPrivateCoursesAnyStatus(Integer amount, String userName) {
        return fetchLastAddedPrivateCoursesByStatus(amount, userName, null);
    }

    public List<Course> getLastAddedPrivateCoursesApproved(Integer amount, String userName) {
        return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.APPROVED);
    }

    public List<Course> getLastAddedPrivateCoursesDeclined(Integer amount, String userName) {
        return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.DECLINED);
    }

    public List<Course> getLastAddedPrivateCoursesPending(Integer amount, String userName) {
        return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.PENDING);
    }

    public List<Course> getLastAddedPrivateCoursesByStatus(Integer amount, String userName, String status) {

        if(status.equalsIgnoreCase("all")){
            return fetchLastAddedPrivateCoursesByStatus(amount, userName, null);
        }
        if(status.equalsIgnoreCase("pending")){
            return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.PENDING);
        }
        if(status.equalsIgnoreCase("approved")){
            return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.APPROVED);
        }
        if(status.equalsIgnoreCase("declined")){
            return fetchLastAddedPrivateCoursesByStatus(amount, userName, CourseStatus.DECLINED);
        }

        return new ArrayList<>();
    }

    public Long countAllPublicCourses() {
        return countPublicCourses(null);
    }

    public long countApprovedPublicCourses(){
        return countPublicCourses(CourseStatus.APPROVED);
    }

    public long countDeclinedPublicCourses(){
        return countPublicCourses(CourseStatus.DECLINED);
    }

    public long countPendingPublicCourses(){
        return countPublicCourses(CourseStatus.PENDING);
    }

    public Long countPublicCourses(CourseStatus status) {

        long coursesCount = 0;


            if(status == null) {
                return courseRepository.countCoursesBySchoolsIsNull();
            }

            if(status == CourseStatus.PENDING) {
                return courseRepository.countCoursesBySchoolsIsNullAndStatus(CourseStatus.PENDING);
            }

            if(status == CourseStatus.DECLINED) {
                return courseRepository.countCoursesBySchoolsIsNullAndStatus(CourseStatus.DECLINED);
            }

            if(status == CourseStatus.APPROVED) {
                return courseRepository.countCoursesBySchoolsIsNullAndStatus(CourseStatus.APPROVED);
            }


        return coursesCount;
    }

    public List<Course> fetchLastAddedPrivateCoursesByCategoryAndStatus(Integer amount, String userName, Long categoryId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CategoryNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        Category category = categoryRepository.getOne(categoryId);

        if(category == null) {
            throw new CategoryNotFoundException("Category with id:"+categoryId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                category,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                category,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchLastAddedPrivateCoursesByCategory(Integer amount, Long categoryId, String userName) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CategoryNotFoundException {
        return fetchLastAddedPrivateCoursesByCategoryAndStatus(amount, userName, categoryId, CourseStatus.APPROVED);
    }

    public List<Course> fetchLastAddedPrivateCoursesBySubCategoryAndStaus(Integer amount, String userName, Long subCategoryId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        SubCategory subCategory = subCategoryRepository.getOne(subCategoryId);

        if(subCategory == null) {
            throw new SubCategoryNotFoundException("SubCategory with id:"+subCategoryId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                subCategory,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                subCategory,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchLastAddedPrivateCoursesBySubCategory(Integer amount, Long subCategoryId, String userName) throws SubCategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchLastAddedPrivateCoursesBySubCategoryAndStaus(amount, userName, subCategoryId, CourseStatus.APPROVED);
    }

    private void validateIfUserWasFound(User user, String userName) throws UserNotFoundExcepion {
        if (user == null) {
            throw new UserNotFoundExcepion("User " + userName + " doesn't exist in system");
        }
    }

    private void validateIfCourseWasFound(Course course, Long courseId) throws CourseNotFoundExcepion {
        if (course == null) {
            throw new CourseNotFoundExcepion("Course with id: " + courseId + " was not found in db");
        }
    }

    private void validateIfSchoolOrOrganizationExists(User user, String userName) throws SchoolNotFoundExcepion {
        if (user.getSchools() == null) {
            throw new SchoolNotFoundExcepion("User " + userName + " doesn't belong to any school");
        }
    }

    public List<Course> fetchLastAddedPrivateCoursesBySectionAndStatus(Integer amount, String userName, Long sectionId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        Section section = sectionRepository.getOne(sectionId);

        if(section == null) {
            throw new SectionNotFoundException("Section with id:"+sectionId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                section,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                section,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchLastAddedPrivateCoursesBySection(Integer amount, Long sectionId, String userName) throws SectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchLastAddedPrivateCoursesBySectionAndStatus(amount, userName, sectionId, CourseStatus.APPROVED);
    }

    public List<Course> fetchLastAddedPrivateCoursesBySubSectionAndStatus(Integer amount, String userName, Long subSectionId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        SubSection subSection = subSectionRepository.getOne(subSectionId);

        if(subSection == null) {
            throw new SubSectionNotFoundException("SubSection with id:"+subSectionId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                subSection,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findLastCreatedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByCreationDateDesc(
                                subSection,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchLastAddedPrivateCoursesBySubSection(Integer amount, Long subSectionId, String userName) throws SubSectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchLastAddedPrivateCoursesBySubSectionAndStatus(amount, userName, subSectionId, CourseStatus.APPROVED);
    }

    public List<Course> fetchBestRatedPrivateCoursesByStatus(Integer amount, String userName, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesByDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesByDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchBestRatedPrivateCourses(Integer amount, String userName) throws UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchBestRatedPrivateCoursesByStatus(amount, userName, CourseStatus.APPROVED);
    }

    public List<Course> fetchBestRatedPrivateCoursesByStatusAndCategory(Integer amount, String userName, Long categoryId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CategoryNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        Category category = categoryRepository.getOne(categoryId);

        if(category == null) {
            throw new CategoryNotFoundException("Category with id:"+categoryId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                category,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                category,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchBestRatedPrivateCoursesByCategory(Integer amount, Long categoryId, String userName) throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchBestRatedPrivateCoursesByStatusAndCategory(amount, userName, categoryId, CourseStatus.APPROVED);
    }

    public List<Course> fetchBestRatedPrivateCoursesByStatusAndSubCategory(Integer amount, String userName, Long subCategoryId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        SubCategory subCategory = subCategoryRepository.getOne(subCategoryId);

        if(subCategory == null) {
            throw new SubCategoryNotFoundException("SubCategory with id:"+subCategoryId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                subCategory,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                subCategory,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchBestRatedPrivateCoursesBySubCategory(Integer amount, Long subCategoryId, String userName) throws SubCategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchBestRatedPrivateCoursesByStatusAndSubCategory(amount, userName, subCategoryId, CourseStatus.APPROVED);
    }

    public List<Course> fetchBestRatedPrivateCoursesByStatusAndSection(Integer amount, String userName, Long sectionId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        Section section = sectionRepository.getOne(sectionId);

        if(section == null) {
            throw new SectionNotFoundException("Section with id:"+sectionId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                section,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                section,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchBestRatedPrivateCoursesBySection(Integer amount, Long sectionId, String userName) throws SectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchBestRatedPrivateCoursesByStatusAndSection(amount, userName, sectionId, CourseStatus.APPROVED);
    }

    public List<Course> fetchBestRatedPrivateCoursesByStatusAndSubSection(Integer amount, String userName, Long subSectionId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        SubSection subSection = subSectionRepository.getOne(subSectionId);

        if(subSection == null) {
            throw new SubSectionNotFoundException("SubSection with id:"+subSectionId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                subSection,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findBestRatedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByRatingsDesc(
                                subSection,
                                schoolOrOrganization,
                                pageable);

                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchBestRatedPrivateCoursesBySubSection(Integer amount, Long subSectionId, String userName) throws SubSectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchBestRatedPrivateCoursesByStatusAndSubSection(amount, userName, subSectionId, CourseStatus.APPROVED);
    }

    public List<Course> fetchFeaturedPrivateCoursesByStatus(Integer amount, String userName, CourseStatus status) throws SchoolNotFoundExcepion, UserNotFoundExcepion {
        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesByDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesByDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchFeaturedPrivateCourses(Integer amount, String userName) throws UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchFeaturedPrivateCoursesByStatus(amount, userName, CourseStatus.APPROVED);
    }

    public List<Course> fetchFeaturedPrivateCoursesByStatusAndCategory(Integer amount, String userName, Long categoryId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CategoryNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        Category category = categoryRepository.getOne(categoryId);

        if(category == null) {
            throw new CategoryNotFoundException("Category with id:"+categoryId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                category,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesByCategoryAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                category,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchFeaturedByCategoryPrivateCourses(Integer amount, Long categoryId, String userName) throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchFeaturedPrivateCoursesByStatusAndCategory(amount, userName, categoryId, CourseStatus.APPROVED);
    }

    public List<Course> fetchFeaturedPrivateCoursesByStatusAndSubCategory(Integer amount, String userName, Long subCategoryId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        SubCategory subCategory = subCategoryRepository.getOne(subCategoryId);

        if(subCategory == null) {
            throw new SubCategoryNotFoundException("SubCategory with id:"+subCategoryId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                subCategory,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesBySubCategoryAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                subCategory,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchFeaturedBySubCategoryPrivateCourses(Integer amount, Long subCategoryId, String userName) throws SubCategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchFeaturedPrivateCoursesByStatusAndSubCategory(amount, userName, subCategoryId, CourseStatus.APPROVED);
    }

    public List<Course> fetchFeaturedPrivateCoursesByStatusAndSection(Integer amount, String userName, Long sectionId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        Section section = sectionRepository.getOne(sectionId);

        if(section == null) {
            throw new SectionNotFoundException("Section with id:"+sectionId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                section,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesBySectionAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                section,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchFeaturedBySectionPrivateCourses(Integer amount, Long sectionId, String userName) throws SectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchFeaturedPrivateCoursesByStatusAndSection(amount, userName, sectionId, CourseStatus.APPROVED);
    }

    public List<Course> fetchFeaturedPrivateCoursesByStatusAndSubSection(Integer amount, String userName, Long subSectionId, CourseStatus status) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        Pageable pageable = new PageRequest(0, amount);
        User user = userRepository.findByUsername(userName);

        validateIfUserWasFound(user, userName);
        validateIfSchoolOrOrganizationExists(user, userName);

        SubSection subSection = subSectionRepository.getOne(subSectionId);

        if(subSection == null) {
            throw new SubSectionNotFoundException("SubSection with id:"+subSectionId+" was not found");
        }

        List<User> schools = user.getSchools();

        List<Course> courses = new ArrayList<>();

        for(User schoolOrOrganization : schools) {
            if(status == null) {
                //fetch courses with any status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                subSection,
                                schoolOrOrganization,
                                pageable);
                courses.addAll(coursesBySubSectionAndSchool);

            } else {
                //find by status
                List<Course> coursesBySubSectionAndSchool = courseRepository.
                        findFeaturedPrivateCoursesBySubSectionAndDeletedFalseAndSchoolsInOrderByEnrolledDesc(
                                subSection,
                                schoolOrOrganization,
                                pageable);
                List<Course> coursesByStatus = getSchoolCoursesByStatus(coursesBySubSectionAndSchool, schoolOrOrganization, status);
                courses.addAll(coursesByStatus);
            }

        }

        return courses;
    }

    public List<Course> fetchFeaturedBySubSectionPrivateCourses(Integer amount, Long subSectionId, String userName) throws SubSectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        return fetchFeaturedPrivateCoursesByStatusAndSubSection(amount, userName, subSectionId, CourseStatus.APPROVED);
    }


    // TODO: Update number of enrolled
    // TODO: Update rating

}
