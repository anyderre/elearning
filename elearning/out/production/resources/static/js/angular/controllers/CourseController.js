app.controller('CourseController', ['CourseService','CategoryService','$scope', '$window', '$timeout', function(CourseService,CategoryService, $scope, $window, $timeout) {
    var self = this;
    self.user = angular.fromJson($window.localStorage.getItem('user'));
    self.categories=[];
    self.courses=[];
    self.course={ id:0, price: '', duration: '', title: '', syllabus:{}, user:{}, startDate: null, category:{}, premium: false};
    self.message ='';
    self.page = 1;
    self.totalCount = 0;
    self.itemsPerPage=5;
    self.filter= '';

    fetchAllCategories();

    function fetchAllCategories() {
        CategoryService.fetchAllCategories()
            .then(
                function (d) {
                    self.categories = d;
                    console.log(self.categories);
                },
                function (errorResponse) {

                    console.error(errorResponse);
                })
    }


    function filterAllCourses(page, itemsPerPage, searchText) {
        CourseService.filterAllCourses(page, itemsPerPage, searchText)
            .then(
                function (d) {

                    d.content.forEach(function (data) {
                        self.courses.push(data);
                    });
                    $scope.start= self.page*$scope.itemsPerPage-$scope.itemsPerPage;
                    self.totalCount = d.totalElements;
                },
                function (errorResponse) {
                    console.error(errorResponse);
                })
    }

    if(self.filter){
        fetchAllCoursesByPage(self.page-1, self.itemsPerPage, self.filter);
    }else{
        fetchAllCoursesByPage(self.page-1, self.itemsPerPage);
    }

    function fetchAllCoursesByPage(page, itemsPerPage) {
        CourseService.fetchAllCoursesByPage(page, itemsPerPage)
            .then(
                function (d) {
                    d.content.forEach(function (data) {
                        self.courses.push(data);
                    });
                    $scope.start= self.page*$scope.itemsPerPage-$scope.itemsPerPage;
                    self.totalCount = d.totalElements;
                },
                function (errorResponse) {
                    console.error(errorResponse);
                })
    }

    //creating a new Course
    function createCourse(course) {
        CourseService.CreateCourse(course)
            .then(
                $timeout(self.fetch, 200),
                function (err) {
                    self.message ='Le cours n\'a pas put etre cree';
                    console.error(err);
                }
            )
    }

    self.fetch= function () {
        fetchAllCategories();
        fetchAllCoursesByPage(self.page-1, self.itemsPerPage);
    };


    //updating a course
    function updateCourse(course, id) {
        CourseService.updateCourse(course,id).then(
            $timeout(self.fetch, 200),
            function (errResponse) {
                self.message='Le cours n\'a pas put etre modifie';
                console.error(errResponse);
            }
        )
    }

    //deleting a course
    function deleteCourse(id) {
        CourseService.deleteCourse(id)
            .then  (
                $timeout(self.fetch, 200),
                function (errResponse) {
                    self.message='Le cours n\'a pas put etre elimine';
                    console.error(errResponse)
                }
            )
    }

    //adding new course
    self.register= function () {
        self.course.user = self.user;
        self.course.price = parseFloat(self.course.price);
        self.course.duration= parseInt(self.course.duration);
        if(!self.course.id){
            createCourse(self.course);
        }else{
            updateCourse(self.course, self.course.id);
        }
        self.reset();
    };

    self.edit = function (id) {
        self.course = self.courses.find(function (course) {
            if(course.id === id)
                return course;
        });
        if(self.course)
            self.course.startDate = new Date( self.course.startDate);
    };

    self.delete = function (id) {
        deleteCourse(id);
        self.reset();
    };

    //resetting category form
    self.reset= function () {
        self.course = {};
        self.courses.length = [];
        self.categories=[];
        $scope.courseForm.$setPristine();
        $scope.courseForm.$setUntouched();
    };

    //when number of items per page change
    self.paginate= function () {
        self.courses = [];
        fetchAllCoursesByPage(self.page-1, self.itemsPerPage);
    };

    // when page number change
    self.pageChange = function (page) {
       self.page = page;
       self.courses = [];
       fetchAllCoursesByPage(page-1, self.itemsPerPage);
    };

    self.filterElements= function () {
        console.log(self.filter);
        self.courses = [];
        filterAllCourses(self.page-1, self.itemsPerPage, self.filter);
    }


}]);


'use strict';
