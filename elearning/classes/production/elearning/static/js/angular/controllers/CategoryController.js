'use strict';
app.controller('CategoryController', ['CategoryService','$scope','$window','$timeout', function(CategoryService, $scope, $window, $timeout) {
    var self = this;
    self.user = angular.fromJson($window.localStorage.getItem('user'));
    self.topics=[];
    self.categories=[];
    self.pageno = 1;
    self.total_count = 0;
    self.itemsPerPage= 10;


    self.category= { id:0, description: '', name: '', parentCategory:null};
    self.message ='';


    self.fetch();
    //Fetching all Categories
    function fetchAllCategories() {
        CategoryService.fetchAllCategories()
            .then(
                function (d) {
                    self.categories = d;
                    console.log(self.categories);
                },
                function (errorResponse) {
                    self.message="Categories inaccessibles "
                    console.error(errorResponse);
                })
    }

    //creating a new Category
    function createCategoryAndTopics(topics) {
        CategoryService.CreateCategoryAndTopics(topics)
            .then(
                $timeout(self.fetch, 200),
                function (err) {
                    self.message ='La categorie n\'a pas put etre cree';
                    console.error(err);
                }
            )
    }
    self.fetch= function () {
        fetchAllCategories();
    };
    //fetching topics
    function fetchTopics(id) {
        CategoryService.fetchTopics(id)
            .then(function (d) {
                    self.topics = d;
                    console.log(self.topics);
                    if(self.topics && self.topics.length >0){
                        self.category = self.topics[0].category;
                    }
                    self.topics.forEach(function (topic) {
                        topic.category = self.category;
                    })
                },
                function (errorResponse) {
                    console.error(errorResponse);
                }
            )
    }

    //updating a category
    function updateCategory(category, id) {
       CategoryService.updateCategory(category,id).then(
           $timeout(self.fetch, 200),
            function (errResponse) {
                self.message='La categorie n\'a pas put etre modifiee';
                console.error(errResponse);
            }
        )
    }

    //deleting a category
    function deleteCategory(id) {
        CategoryService.deleteCategory(id)
            .then  (
                $timeout(self.fetch, 200),
                function (errResponse) {
                    self.message='La categorie n\'a pas put etre eliminee';
                    console.error(errResponse)
                }
            )
    }

    //adding new category and topics
    self.register= function (){
        self.topics.forEach(function (topic) {
            if(topic.category.parentCategory)
                topic.category.parentCategory =  topic.category.parentCategory.id
        });
        createCategoryAndTopics(self.topics);
        self.reset();
    };

    //edit category and topics
    self.edit= function (id) {
        fetchTopics(id);
    };

    self.delete= function (id) {
        deleteCategory(id);
    };
    //resetting category form
    self.reset= function () {
        self.topics = [];
        self.category = {};
        self.categories= [];
        $scope.categoryForm.$setPristine();
        $scope.categoryForm.$setUntouched();
    };

    //Adding a new topics
    self.newTopic = function(topic) {
        return {
            name: topic,
            category: self.category
        };
    };

}]);


