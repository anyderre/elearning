'use strict';
app.controller('IndexController', ['IndexService','$scope','$window', function(IndexService, $scope, $window) {
    var self = this;

     self.user = angular.fromJson($window.localStorage.getItem('user'));


    fetchUser();
    function fetchUser() {
        IndexService.fetchUser()
            .then(
                function (d) {
                    self.user = d;
                    if(d)
                        localStorage.setItem('user', angular.toJson(d))
                },
                function (errorResponse) {
                    console.error(errorResponse);
                })
    }

    //creating a new Category
    function createCategoryAndTopics(topics) {
        CategoryService.CreateCategoryAndTopics(topics)
            .then(
                fetchAllCategories(),
                function (err) {
                    self.message ='La categorie n\'a pas put etre cree';
                    console.error(err);
                }
            )
    }

    //updating a category
    function updateCategory(category, id) {
       CategoryService.updateCategory(category,id).then(
            fetchAllCategories(),
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
                fetchAllCategories(),
                function (errResponse) {
                    self.message='La categorie n\'a pas put etre eliminee';
                    console.error(errResponse)
                }
            )
    }

}]);


